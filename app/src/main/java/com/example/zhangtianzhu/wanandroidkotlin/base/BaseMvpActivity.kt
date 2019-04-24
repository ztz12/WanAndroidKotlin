package com.example.zhangtianzhu.wanandroidkotlin.base

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry

@Suppress("UNCHECKED_CAST")
abstract class BaseMvpActivity<in V : IView, P : IPresenter<V>> : BaseActivity(), IView, LifecycleOwner {
    protected var mPresenter: P? = null

    private lateinit var lifecycleRegistry: LifecycleRegistry

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
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
        this.mPresenter = null
    }
}