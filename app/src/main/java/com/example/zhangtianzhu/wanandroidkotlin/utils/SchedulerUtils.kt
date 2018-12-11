package com.example.zhangtianzhu.wanandroidkotlin.utils

object SchedulerUtils {
    fun <T> toMain():ToMainScheduler<T>{
        return ToMainScheduler()
    }
}