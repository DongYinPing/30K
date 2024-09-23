package com.app.mvvm.ext

import androidx.annotation.Keep
import java.lang.reflect.ParameterizedType

/**
 * 通过泛型获取Class
 */

@Keep
@Suppress("UNCHECKED_CAST")
fun <T> getTClazz(
    obj: Any,
    pos: Int = 0,
): T = (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[pos] as T
