package com.example.zhangtianzhu.wanandroidkotlin.presenter.navigation

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.contract.navigation.NavigationContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.navigation.NavigationModel

class NavigationPresenter : BasePresenter<NavigationContract.View>(),NavigationContract.Presenter{
    private val mModel:NavigationModel by lazy { NavigationModel() }

    override fun getNavigationData() {
        mView?.showLoading()
        val disposed = mModel.getNaviagtionModel()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showNavaigationData(results.data)
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
        addDisposed(disposed)
    }
}