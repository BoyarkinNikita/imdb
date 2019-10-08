package com.example.imdb.utils.helper.diff

interface DiffItem {
    fun id(): Any? = hashCode()
}