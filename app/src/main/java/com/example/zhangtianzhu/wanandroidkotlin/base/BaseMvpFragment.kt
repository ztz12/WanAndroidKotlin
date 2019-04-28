package com.example.zhangtianzhu.wanandroidkotlin.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry

@Suppress("UNCHECKED_CAST")
abstract class BaseMvpFragment<in V : IView, P : IPresenter<V>> : BaseFragment(), IView, LifecycleOwner {

    protected var mPresenter: P? = null

    private var lifecycleRegistry: LifecycleRegistry?=null

    protected abstract fun createPresenter(): P

    override fun initView() {
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycle.addObserver(BasePresenter<V>())
        mPresenter = createPresenter()
        if (mPresenter != null) {
            mPresenter?.attachView(this as V)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(lifecycleRegistry!=null) {
            lifecycleRegistry?.markState(Lifecycle.State.DESTROYED)
        }
        this.mPresenter = null
    }
}