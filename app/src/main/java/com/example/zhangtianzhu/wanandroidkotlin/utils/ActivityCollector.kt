package com.example.zhangtianzhu.wanandroidkotlin.utils

import android.app.Activity
import java.lang.ref.WeakReference

/**
 * 一键销毁所有Activity
 */
object ActivityCollector {
    private val activities = ArrayList<WeakReference<Activity>?>()

    fun add(activity: WeakReference<Activity>?) {
        activities.add(activity)
    }

    fun remove(activity: WeakReference<Activity>?) {
        activities.remove(activity)
    }

    fun finishAll() {
        if (activities.isNotEmpty()) {
            for (activityWealReference in activities) {
                val activity = activityWealReference?.get()
                if (activity != null && !activity.isFinishing) {
                    activity.finish()
                }
            }
            activities.clear()
        }
    }
}