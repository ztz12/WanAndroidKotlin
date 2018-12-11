package com.example.zhangtianzhu.wanandroidkotlin.contract.login

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView
import com.example.zhangtianzhu.wanandroidkotlin.constant.LoginData

interface RegisterContract {
    interface View :IView{
        fun getRegisterData(loginData: LoginData)
    }

    interface Presenter : IPresenter<View>{
        fun registerWanAndroid(username:String,password:String,repassword:String)
    }
}