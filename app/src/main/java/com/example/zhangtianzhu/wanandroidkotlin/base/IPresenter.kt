package com.example.zhangtianzhu.wanandroidkotlin.base

import io.reactivex.disposables.Disposable

interface IPresenter<in V:IView> {

    fun attachView(mView:V)

    fun detachView()

    fun addSubscribe(disposable: Disposable)

}