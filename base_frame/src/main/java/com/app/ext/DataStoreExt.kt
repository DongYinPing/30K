package com.app.ext

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.app.app.App
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * 封装DataStore，方便存取
 */
private val Context.defDataStore: DataStore<Preferences> by preferencesDataStore(name = "DefDataStore")

val appDataStore: DataStore<Preferences> by lazy {
    // 依赖AppHelper.app初始化
    App.app.defDataStore
}

suspend inline fun <reified T> putFlowData(
    key: String,
    defaultValue: T,
) {
    putFlowValue(key, defaultValue)
}

suspend inline fun <reified T> getFlowData(
    key: String,
    defaultValue: T,
): T {
    return getFlowValue(key, defaultValue).first()
}

suspend inline fun <reified T> getObjData(key: String): T? {
    val json = getFlowData(key, "")
    if (json.isEmpty()) {
        return null
    }
    return json.toObj()
}

suspend inline fun <reified T> getListObjData(key: String): List<T>? {
    val json = getFlowData(key, "")
    if (json.isEmpty()) {
        return null
    }
    return json.toListObj()
}

suspend inline fun <reified T> putFlowValue(
    key: String,
    value: T,
) {
    when (value) {
        is Int -> putDataFlow(intPreferencesKey(key), value)
        is Long -> putDataFlow(longPreferencesKey(key), value)
        is String -> putDataFlow(stringPreferencesKey(key), value)
        is Boolean -> putDataFlow(booleanPreferencesKey(key), value)
        is Float -> putDataFlow(floatPreferencesKey(key), value)
        is Double -> putDataFlow(doublePreferencesKey(key), value)
        else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
    }
}

inline fun <reified T> getFlowValue(
    key: String,
    defaultValue: T,
): Flow<T> {
    val data =
        when (defaultValue) {
            is Int -> getDataFlow(intPreferencesKey(key), defaultValue)
            is Long -> getDataFlow(longPreferencesKey(key), defaultValue)
            is String -> getDataFlow(stringPreferencesKey(key), defaultValue)
            is Boolean -> getDataFlow(booleanPreferencesKey(key), defaultValue)
            is Float -> getDataFlow(floatPreferencesKey(key), defaultValue)
            is Double -> getDataFlow(doublePreferencesKey(key), defaultValue)
            else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
        }
    return data as Flow<T>
}

fun <T> getDataFlow(
    key: Preferences.Key<T>,
    defaultValue: T,
): Flow<T> {
    val dataFlow: Flow<T> =
        appDataStore.data.map {
            it[key] ?: defaultValue
        }
    return dataFlow
}

suspend fun <T> putDataFlow(
    key: Preferences.Key<T>,
    value: T,
) {
    appDataStore.edit {
        it[key] = value
    }
}

suspend inline fun <reified T> clearFlowData(
    key: String,
    value: T,
) {
    when (value) {
        is Int ->
            appDataStore.edit {
                it.remove(intPreferencesKey(key))
            }

        is Long ->
            appDataStore.edit {
                it.remove(longPreferencesKey(key))
            }

        is String ->
            appDataStore.edit {
                it.remove(stringPreferencesKey(key))
            }

        is Boolean ->
            appDataStore.edit {
                it.remove(booleanPreferencesKey(key))
            }

        is Float ->
            appDataStore.edit {
                it.remove(floatPreferencesKey(key))
            }

        is Double ->
            appDataStore.edit {
                it.remove(doublePreferencesKey(key))
            }

        else -> throw IllegalArgumentException("This type cannot be saved to the Data Store")
    }
}

/** 清除dataStore数据 */
suspend fun clearDataStore(context: Context) {
    appDataStore.edit {
        it.clear()
    }
}
