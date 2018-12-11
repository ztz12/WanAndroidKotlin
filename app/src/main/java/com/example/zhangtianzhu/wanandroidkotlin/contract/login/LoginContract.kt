package com.example.zhangtianzhu.wanandroidkotlin.contract.login

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView
import com.example.zhangtianzhu.wanandroidkotlin.constant.LoginData

interface LoginContract {
    interface View :IView{
        fun showLoginData(loginData: LoginData)
    }

    interface Presenter : IPresenter<View>{
        fun loginWanAndroid(username:String,password:String)
    }
}