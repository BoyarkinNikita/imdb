package com.example.imdb.utils.helper

import android.os.Parcel
import android.os.Parcelable
import android.os.SystemClock.elapsedRealtime
import com.jakewharton.disklrucache.DiskLruCache
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.SerializationUtils
import timber.log.Timber
import java.io.Serializable
import java.util.Formatter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class TimedCache(private val cache: DiskLruCache) {
    private val locks = ConcurrentHashMap<String, Lock>()

    suspend inline fun <reified T : Any> getOrPut(
        key: String,
        crossinline source: suspend () -> T,
        aliveTimeMillisec: Long,
        isSafely: Boolean = false
    ): T {
        val packed = suspend {
            val value = source()
            ArrayList<T>(1).also { it.add(value) }
        }

        return getOrPutList(key, packed, T::class.java, aliveTimeMillisec, isSafely)[0]
    }

    suspend inline fun <reified T : Any> getOrPutList(
        key: String,
        crossinline source: suspend () -> List<T>,
        aliveTimeMillisec: Long,
        isSafely: Boolean = false
    ): List<T> {
        val packed = suspend { source() }
        return getOrPutList(key, packed, T::class.java, aliveTimeMillisec, isSafely)
    }

    suspend fun <T : Any> getOrPutList(
        key: String,
        source: suspend () -> List<T>,
        clazz: Class<T>,
        aliveTimeMillisec: Long,
        isSafely: Boolean = false
    ): List<T> {
        val cacheResult = get(key, clazz)

        if (cacheResult?.second == true) return cacheResult.first

        val before = elapsedRealtime()

        val result = if (cacheResult == null) source() else try {
            source()
        } catch (throwable: Throwable) {
            if (isSafely) {
                return cacheResult.first
            } else throw throwable
        }

        val after = elapsedRealtime()

        Timber.d("CACHE_SOURCE: key - %s, millisec - %d.", key, after - before)

        return result.also {
            val prepared = if (it !is ArrayList) ArrayList(it) else it
            put(key, prepared, clazz, aliveTimeMillisec)
        }
    }

    fun <T : Any> get(
        key: String,
        clazz: Class<T>
    ): Pair<ArrayList<T>, Boolean>? {
        var isSuccessful = false
        var isExpired = false

        val time = elapsedRealtime()
        val lock = locks.getOrPut(key, { ReentrantLock() })

        lock.lock()
        return try {
            val snapshot = cache.get(key) ?: return null

            val expiration = snapshot.getString(1)?.toLongOrNull() ?: return null

            val bytes = snapshot.getInputStream(0)?.use {
                IOUtils.toByteArray(it)
            } ?: return null

            val result = parcelableDeserializator(bytes, clazz)
                ?: serializableDeserializator(bytes, clazz)
                ?: run {
                    Timber.w("CACHE_EXCEPTION: key - %s, message - 'No Deserializator'.", key)
                    cache.remove(key)
                    return null
                }

            isSuccessful = true

            isExpired = expiration < time
            if (isExpired) Timber.d("CACHE_KEY_EXPIRED: key - %s.", key)
            result to !isExpired
        } catch (throwable: Throwable) {
            Timber.w(throwable, "CACHE_EXCEPTION: key - %s.", key)
            cache.remove(key)
            null
        } finally {
            lock.unlock()

            val after = elapsedRealtime()
            Timber.d(
                "CACHE_GET: key - %s, millisec - %d, isSuccessful - %b, isExpired - %b.",
                key, after - time, isSuccessful, isExpired
            )
        }
    }

    fun <T : Any> put(
        key: String,
        value: ArrayList<T>,
        clazz: Class<T>,
        aliveTimeMillisec: Long
    ) {
        var isSuccessful = false

        val time = elapsedRealtime()
        val expiration = time + aliveTimeMillisec
        val lock = locks.getOrPut(key, { ReentrantLock() })

        lock.lock()
        try {
            val bytes = parcelableSerializator(value, clazz)
                ?: serializableSerializator(value, clazz)
                ?: run {
                    Timber.w("CACHE_EXCEPTION: key - %s, message - 'No Serializator'.", key)
                    cache.remove(key)
                    return
                }

            cache.edit(key).apply {
                newOutputStream(0).use {
                    it.write(bytes)
                    it.flush()
                }

                set(1, expiration.toString())
                commit()
            }

            isSuccessful = true
        } catch (throwable: Throwable) {
            Timber.w(throwable, "CACHE_EXCEPTION: key - %s.", key)
            cache.remove(key)
        } finally {
            lock.unlock()

            val after = elapsedRealtime()
            Timber.d(
                "CACHE_PUT: key - %s, millisec - %d, isSuccessful - %b.",
                key, after - time, isSuccessful
            )
        }
    }

    fun remove(key: String) {
        var isSuccessful = false

        val time = elapsedRealtime()

        val lock = locks.getOrPut(key, { ReentrantLock() })

        lock.lock()
        try {
            cache.remove(key)
            isSuccessful = true
        } catch (throwable: Throwable) {
            Timber.w(throwable, "CACHE_EXCEPTION: key - %s.", key)
        } finally {
            lock.unlock()

            val after = elapsedRealtime()
            Timber.d(
                "CACHE_REMOVE: key - %s, millisec - %d, isSuccessful - %b.",
                key, after - time, isSuccessful
            )
        }
    }

    companion object {
        fun cacheKey(prefix: String, vararg arguments: Any): String {
            val bytes = arguments.joinToString(separator = "", prefix = prefix, transform = {
                it.toString()
            }).toByteArray(Charsets.UTF_8)

            val formatter = Formatter()
            for (byte in bytes) {
                formatter.format("%02x", byte)
            }

            return formatter.toString()
        }

        private fun <T : Any, L : ArrayList<T>> serializableSerializator(
            serializable: L,
            clazz: Class<T>
        ): ByteArray? {
            if (!Serializable::class.java.isAssignableFrom(clazz)) return null

            return try {
                SerializationUtils.serialize(serializable)
            } catch (throwable: Throwable) {
                null
            }
        }

        private fun <T : Any, L : ArrayList<T>> serializableDeserializator(
            bytes: ByteArray,
            clazz: Class<T>
        ): L? {
            if (!Serializable::class.java.isAssignableFrom(clazz)) return null

            return try {
                SerializationUtils.deserialize(bytes)
            } catch (throwable: Throwable) {
                null
            }
        }

        private fun <T : Any, L : ArrayList<T>> parcelableSerializator(
            parcelable: L,
            clazz: Class<T>
        ): ByteArray? {
            if (!Parcelable::class.java.isAssignableFrom(clazz)) return null

            return try {
                val parcel = Parcel.obtain()
                parcel.writeList(parcelable as List<T>)
                val bytes = parcel.marshall()
                parcel.recycle()

                bytes
            } catch (throwable: Throwable) {
                null
            }
        }

        private fun <T : Any, L : ArrayList<T>> parcelableDeserializator(
            bytes: ByteArray,
            clazz: Class<T>
        ): L? {
            if (!Parcelable::class.java.isAssignableFrom(clazz)) return null

            return try {
                val parcel = Parcel.obtain()
                parcel.unmarshall(bytes, 0, bytes.size)
                parcel.setDataPosition(0)
                val data = parcel.readArrayList(clazz.classLoader)
                parcel.recycle()
                @Suppress("UNCHECKED_CAST")
                data as? L?
            } catch (throwable: Throwable) {
                null
            }
        }
    }
}