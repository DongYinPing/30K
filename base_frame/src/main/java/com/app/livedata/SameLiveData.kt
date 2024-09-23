package com.app.livedata

/**
 * LiveData 同值不更新
 */
class SameLiveData<T> : MainLiveData<T>() {
    override fun setValue(value: T?) {
        postValue(value)
    }

    override fun postValue(value: T?) {
        if (isSame(value)) {
            return
        }
        super.postValue(value)
    }

    private fun isSame(value: T?): Boolean {
        val old = getValue()
        return old != null && old == value || old == null && value == null
    }
}
