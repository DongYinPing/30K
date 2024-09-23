package com.app.api

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

data class ApiResponse<T>(
    val code: Int? = null,
    val message: String? = null,
    val data: T? = null,
) {
    val isSuccess: Boolean
        get() = code == 0 || "SUCCESS".equals(message, true)

    val isFailed: Boolean
        get() = !isSuccess

    val isEmpty: Boolean
        get() = data == null || data is List<*> && (data as List<*>).isEmpty()

    override fun toString(): String {
        return "ApiResponse(data=$data, code=$code, message=$message)"
    }
}

inline fun <reified T> ApiResponse<T>.onMessage(action: (message: String?) -> Unit): ApiResponse<T> {
    if (isSuccess && isEmpty) {
        action.invoke(message)
    }
    return this
}

inline fun <reified T> ApiResponse<T>.onSuccess(action: (data: T?) -> Unit): ApiResponse<T> {
    if (isSuccess) {
        action.invoke(data)
    }
    return this
}

inline fun <reified T> ApiResponse<T>.onFailed(action: (code: Int?, message: String?) -> Unit): ApiResponse<T>? {
    if (isFailed) {
        action.invoke(code, message)
    }
    return this
}

inline fun <reified T> ApiResponse<T>.onIntercept(): ApiResponse<T>? {
    // 拦截
    if (code == 404) {
        // 被拦截，返回null，则不继续执行
        return null
    }
    // 未被拦截，返回this，继续执行
    return this
}

inline fun Throwable.onError(action: (message: String?) -> Unit) {
    action.invoke(message)
}

// 若Api请求失败，onFailed不回调，用统一UI显示错误信息
inline fun <reified T> CoroutineScope.request(
    crossinline request: suspend () -> ApiResponse<T>,
    crossinline success: (data: T?) -> Unit,
): Job {
    return launch(Dispatchers.Main) {
        flow {
            emit(request())
        }.flowOn(Dispatchers.IO)
            .catch {
                // 不关心错误信息的请求
            }.onEach {
                it.onIntercept()?.onSuccess(success)?.onFailed { _, message ->
                    message?.let {
                        // 通用Toast提示请求返回的错误信息
                    }
                }
            }
    }
}

// 若Api请求成功，但是无数据，回调message
inline fun <reified T> CoroutineScope.request(
    crossinline request: suspend () -> ApiResponse<T>,
    crossinline success: (data: T?) -> Unit,
    crossinline message: (message: String?) -> Unit,
): Job {
    return launch(Dispatchers.Main) {
        flow {
            emit(request())
        }.flowOn(Dispatchers.IO)
            .catch {
                // 不关心错误信息的请求
            }.onEach {
                it.onIntercept()?.onSuccess(success)?.onMessage(message)
            }.collect()
    }
}

// 若Api请求失败，onFailed执行回调，需单独处理错误信息
inline fun <reified T> CoroutineScope.request(
    crossinline request: suspend () -> ApiResponse<T>,
    crossinline success: (data: T?) -> Unit,
    crossinline failed: (code: Int?, message: String?) -> Unit,
): Job {
    return launch(Dispatchers.Main) {
        flow {
            emit(request())
        }.flowOn(Dispatchers.IO)
            .catch {
                // 不关心错误信息的请求
            }.onEach {
                it.onIntercept()?.onSuccess(success)?.onFailed(failed)
            }.collect()
    }
}

// 完整的Api请求回调，
inline fun <reified T> CoroutineScope.request(
    crossinline request: suspend () -> ApiResponse<T>,
    crossinline start: () -> Unit,
    crossinline success: (T?) -> Unit,
    crossinline failed: (code: Int?, message: String?) -> Unit,
    crossinline error: (message: String?) -> Unit,
    crossinline completion: () -> Unit,
): Job {
    return launch(Dispatchers.Main) {
        flow {
            emit(request())
        }.flowOn(Dispatchers.IO)
            .onStart {
                start.invoke()
            }.onCompletion {
                completion.invoke()
            }.catch {
                it.onError { message ->
                    error.invoke(message)
                }
            }.onEach {
                it.onIntercept()?.onSuccess(success)?.onFailed(failed)
            }.collect()
    }
}
