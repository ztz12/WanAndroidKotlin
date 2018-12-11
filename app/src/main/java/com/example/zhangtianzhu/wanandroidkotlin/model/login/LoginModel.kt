package com.example.zhangtianzhu.wanandroidkotlin.model.login

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.constant.LoginData
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class LoginModel :BaseModel(){
    fun login(username:String,password:String):Observable<HttpResult<LoginData>>{
        return RetrofitService.service.getLoginData(username, password)
                .compose(SchedulerUtils.toMain())
    }
}