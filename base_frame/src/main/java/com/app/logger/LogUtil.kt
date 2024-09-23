package com.app.logger

import android.util.Log

object LogUtil {
    private const val DEF_TAG = "DEF_LOG_TAG"

    private var debug = true // 默认打开

    fun initDebug(debug: Boolean) {
        this.debug = debug
    }

    fun d(
        tag: Any? = null,
        msg: Any = "NULL",
    ) {
        if (debug) {
            val message = msg.toString()
            Log.d(getTag(tag), getMsgFormat(message))
        }
    }

    fun e(
        tag: Any? = null,
        msg: Any = "NULL",
    ) {
        val message = msg.toString()
        Log.e(getTag(tag), getMsgFormat(message))
    }

    private fun getTag(tag: Any? = null): String {
        if (tag == null) {
            return DEF_TAG
        }
        return when (tag) {
            is String -> {
                tag
            }

            is Class<*> -> {
                tag.simpleName
            }

            else -> {
                tag.javaClass.simpleName
            }
        }
    }

    /**
     * 获取类名,方法名,行号
     */
    private fun getFunctionName(): String? {
        try {
            val sts = Thread.currentThread().stackTrace
            for (st in sts) {
                if (st.isNativeMethod) {
                    continue
                }
                if (st.className == Thread::class.java.name) {
                    continue
                }
                if (st.className == LogUtil::class.java.name) {
                    continue
                }
                return (
                    "Thread:" + Thread.currentThread().name + "," + st.className + "." + st.methodName +
                        "(" + st.fileName + ":" + st.lineNumber + ")"
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getMsgFormat(msg: String): String {
        return getFunctionName() + "👇\n" + msg
    }
}
