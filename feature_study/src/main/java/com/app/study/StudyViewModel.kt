package com.app.study

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.app.android.R
import com.app.livedata.MainLiveData

class StudyViewModel : ViewModel() {
    private val _androidStudyData = MainLiveData<List<StudyItem>>()
    val androidStudyData: LiveData<List<StudyItem>> = _androidStudyData

    private val _javaStudyData = MainLiveData<List<StudyItem>>()
    val javaStudyData: LiveData<List<StudyItem>> = _javaStudyData

    private val _kotlinStudyData = MainLiveData<List<StudyItem>>()
    val kotlinStudyData: LiveData<List<StudyItem>> = _kotlinStudyData

    private fun setAndroidStudyData(studyList: List<StudyItem>) {
        _androidStudyData.value = studyList
    }

    fun setJavaStudyData(studyList: List<StudyItem>) {
        _javaStudyData.value = studyList
    }

    fun setKotlinStudyData(studyList: List<StudyItem>) {
        _kotlinStudyData.value = studyList
    }

    fun createAndroidStudyData() {
        arrayListOf<StudyItem>().apply {
            var index = 0
            add(
                StudyItem(
                    id = index++,
                    layout = R.layout.item_study_title,
                    title = "Basic",
                ),
            )
            add(
                StudyItem(
                    id = index++,
                    R.layout.item_study_desc,
                    desc = "Android四大组件",
                    md = "android/Basic-Android.md",
                ),
            )
            add(
                StudyItem(
                    id = index++,
                    layout = R.layout.item_study_desc,
                    desc = "Activity",
                    md = "android/Basic-Activity.md",
                ),
            )
            add(
                StudyItem(
                    id = index++,
                    R.layout.item_study_desc,
                    desc = "Service",
                    md = "android/Basic-Service.md",
                ),
            )
            add(
                StudyItem(
                    id = index++,
                    R.layout.item_study_desc,
                    desc = "BroadCastReceiver",
                    md = "android/Basic-BroadCastReceiver.md",
                ),
            )
            add(
                StudyItem(
                    id = index++,
                    R.layout.item_study_desc,
                    desc = "ContentProvider",
                    md = "android/Basic-ContentProvider.md",
                ),
            )
            add(StudyItem(id = index++, layout = R.layout.item_study_title, title = "View"))
            add(
                StudyItem(
                    id = index++,
                    R.layout.item_study_desc,
                    desc = "View事件",
                    md = "android/View-Event.md",
                ),
            )
            add(StudyItem(id = index++, layout = R.layout.item_study_title, title = "Framework"))
            add(
                StudyItem(
                    id = index++,
                    R.layout.item_study_desc,
                    desc = "系统启动流程",
                    md = "android/Framework-System-Launch.md",
                ),
            )
            add(
                StudyItem(
                    id = index++,
                    R.layout.item_study_desc,
                    desc = "应用启动流程",
                    md = "android/Framework-Activity-Launch.md",
                ),
            )
            add(StudyItem(id = index++, layout = R.layout.item_study_title, title = "Binder"))
            setAndroidStudyData(this)
        }
    }
}
