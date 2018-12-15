package com.example.zhangtianzhu.wanandroidkotlin.presenter.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.UseWebContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.home.UseWebModel

class UseWebPresenter:BasePresenter<UseWebContract.View>(),UseWebContract.Presenter {

    private val mModel : UseWebModel by lazy { UseWebModel() }

    override fun getUseWebData() {
        mView?.showLoading()
        val disposable = mModel.getUseWebData()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showUseWebData(results.data)
                        }
                        hideLoading()
                    }
                },{
                    t ->
                    mView?.apply {
                        hideLoading()
                        showErrorMsg(ErrorException.handleException(t))
                    }
                })
        addDisposed(disposable)
    }
}