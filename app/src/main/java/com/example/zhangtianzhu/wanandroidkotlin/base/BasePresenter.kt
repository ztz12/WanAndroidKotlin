package com.example.zhangtianzhu.wanandroidkotlin.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class BasePresenter<V : IView> :IPresenter<V> ,LifecycleObserver{
    protected var mView:V? =null
    private var compositeDisposable:CompositeDisposable? =null
    override fun attachView(mView: V) {
        this.mView = mView
    }

    override fun detachView() {
        mView = null
        unDisposed()
    }

    override fun addSubscribe(disposable: Disposable) {
        addDisposed(disposable)
    }

    private fun unDisposed(){
        if(compositeDisposable!=null){
            compositeDisposable!!.clear()
            compositeDisposable = null
        }
    }
    open fun addDisposed(disposable: Disposable){
        if(compositeDisposable == null){
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable!!.add(disposable)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner){
        detachView()
        owner.lifecycle.removeObserver(this)
    }
}