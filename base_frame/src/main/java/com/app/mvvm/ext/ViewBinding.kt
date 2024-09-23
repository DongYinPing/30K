package com.app.mvvm.ext

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * 通过泛型创建ViewBinding
 */

@Suppress("UNCHECKED_CAST")
@JvmName("inflateBindingWithGeneric")
fun <VB : ViewBinding> Activity.inflateBindingWithGeneric(
    layoutInflater: LayoutInflater,
    pos: Int = 1,
): VB =
    withGenericBindingClass<VB>(this, pos) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }

@Suppress("UNCHECKED_CAST")
@JvmName("inflateBindingWithGeneric")
fun <VB : ViewBinding> Fragment.inflateBindingWithGeneric(
    layoutInflater: LayoutInflater,
    parent: ViewGroup?,
    attachToParent: Boolean,
    index: Int = 1,
): VB =
    withGenericBindingClass<VB>(this, index) { clazz ->
        clazz.getMethod(
            "inflate",
            LayoutInflater::class.java,
            ViewGroup::class.java,
            Boolean::class.java,
        )
            .invoke(null, layoutInflater, parent, attachToParent) as VB
    }

@Keep
@Suppress("UNCHECKED_CAST")
@JvmName("withGenericBindingClass")
private fun <VB : ViewBinding> withGenericBindingClass(
    any: Any,
    index: Int,
    block: (Class<VB>) -> VB,
): VB {
    var genericSuperclass = any.javaClass.genericSuperclass
    var superclass = any.javaClass.superclass
    while (superclass != null) {
        if (genericSuperclass is ParameterizedType) {
            try {
                return block.invoke(genericSuperclass.actualTypeArguments[index] as Class<VB>)
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            } catch (e: ClassCastException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                throw e.targetException
            }
        }
        genericSuperclass = superclass.genericSuperclass
        superclass = superclass.superclass
    }
    throw IllegalArgumentException("There is no generic of ViewBinding.")
}
