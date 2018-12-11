package com.example.zhangtianzhu.wanandroidkotlin.utils

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseScheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class ToMainScheduler<T> : BaseScheduler<T>(Schedulers.io(),AndroidSchedulers.mainThread())