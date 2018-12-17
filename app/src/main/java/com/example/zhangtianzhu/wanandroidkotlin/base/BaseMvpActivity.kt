package com.example.zhangtianzhu.wanandroidkotlin.base

@Suppress("UNCHECKED_CAST")
abstract class BaseMvpActivity<in V:IView,P:IPresenter<V>>:BaseActivity(),IView {
    protected var mPresenter:P? =null

    protected abstract fun createPresenter():P

    override fun initView() {
        mPresenter = createPresenter()
        if(mPresenter!=null){
            mPresenter?.attachView(this as V)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mPresenter!=null){
            mPresenter?.detachView()
        }
        this.mPresenter = null
    }
}