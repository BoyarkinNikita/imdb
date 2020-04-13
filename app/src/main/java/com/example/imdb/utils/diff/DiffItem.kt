package com.example.imdb.utils.diff

interface DiffItem {
    fun id(): Any? = hashCode()
}