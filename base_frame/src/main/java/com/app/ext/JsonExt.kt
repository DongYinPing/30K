package com.app.ext

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * gson减少创建
 */
val gson: Gson by lazy {
    Gson()
}

fun Any.toJson(): String {
    return gson.toJson(this)
}

inline fun <reified T> String.toObj(): T? {
    return try {
        gson.fromJson(this, T::class.java)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T> String.toListObj(): List<T>? {
    if (this.isEmpty()) {
        return null
    }
    return gson.fromJson(this, object : TypeToken<List<T>>() {}.type)
}
