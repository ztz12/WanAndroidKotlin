package com.example.zhangtianzhu.wanandroidkotlin.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent

open class BaseModel :LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    internal fun onDestroy(owner: LifecycleOwner){
        owner.lifecycle.removeObserver(this)
    }
}