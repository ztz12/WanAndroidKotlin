package com.example.zhangtianzhu.wanandroidkotlin.presenter.login

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.contract.login.RegisterContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.login.RegisterModel

class RegisterPresenter:BasePresenter<RegisterContract.View>() ,RegisterContract.Presenter{
    private val registerModel:RegisterModel by lazy { RegisterModel() }

    override fun registerWanAndroid(username: String, password: String, repassword: String) {
        mView?.showLoading()
        val compose = registerModel.registerWanAndroid(username, password, repassword)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            getRegisterData(results.data)
                        }
                        hideLoading()
                    }
                },{
                    t ->
                    mView?.apply {
                        hideLoading()
                        showErrorMsg(ErrorException.handleException(t))
                    }
                }
        )
        addDisposed(compose)
    }

}