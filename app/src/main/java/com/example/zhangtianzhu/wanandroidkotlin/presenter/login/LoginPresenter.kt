package com.example.zhangtianzhu.wanandroidkotlin.presenter.login

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.contract.login.LoginContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.login.LoginModel
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService

class LoginPresenter : BasePresenter<LoginContract.View>(),LoginContract.Presenter{

    private val loginModel :LoginModel by lazy { LoginModel() }
    override fun loginWanAndroid(username: String, password: String) {
        mView?.showLoading()
        var dispose = loginModel.login(username, password)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showLoginData(results.data)
                        }
                        hideLoading()
                    }
                },{
                    t->
                    mView?.apply {
                        hideLoading()
                        showErrorMsg(ErrorException.handleException(t))
                    }
                })
        addDisposed(dispose)
    }

}