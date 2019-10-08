package com.example.imdb.utils.helper

import android.util.Base64
import android.util.Base64.NO_WRAP
import com.jakewharton.disklrucache.DiskLruCache
import org.apache.commons.lang3.SerializationException
import org.apache.commons.lang3.SerializationUtils
import java.io.Serializable
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

class TimedCache(private val cache: DiskLruCache) {
    private val locks = ConcurrentHashMap<String, Lock>()

    suspend fun <T : Serializable> getOrPut(
        key: String,
        source: suspend () -> T,
        aliveTimeMillisec: Long
    ): T = get(key) ?: source().also {
        put(key, it, aliveTimeMillisec)
    }

    fun <T : Serializable> get(
        key: String
    ): T? {
        val time = System.currentTimeMillis()
        val lock = locks.getOrPut(key, { ReentrantLock() })

        lock.lock()
        return try {
            val snapshot = cache.get(key) ?: return null
            val value = Base64.decode(snapshot.getString(0) ?: return null, NO_WRAP)
            val expiration = snapshot.getString(1)?.toLongOrNull() ?: return null

            if (expiration < time) {
                cache.remove(key)
                null
            } else SerializationUtils.deserialize<T>(value)
        } catch (exception: SerializationException) {
            cache.remove(key)
            return null
        } finally {
            lock.unlock()
        }
    }

    fun <T : Serializable> put(
        key: String,
        value: T,
        aliveTimeMillisec: Long
    ) {
        val expiration = System.currentTimeMillis() + aliveTimeMillisec
        val lock = locks.getOrPut(key, { ReentrantLock() })

        lock.lock()
        try {
            val serialized = SerializationUtils.serialize(value)
            cache.edit(key).apply {
                set(0, Base64.encodeToString(serialized, NO_WRAP))
                set(1, expiration.toString())
                commit()
            }
        } catch (exception: SerializationException) {
            cache.remove(key)
        } finally {
            lock.unlock()
        }
    }

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
}