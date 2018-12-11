package com.example.zhangtianzhu.wanandroidkotlin.model.login

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.constant.LoginData
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class RegisterModel :BaseModel(){
    fun registerWanAndroid(username:String,password:String,repassword:String):Observable<HttpResult<LoginData>>{
        return RetrofitService.service.getRegisterData(username, password, repassword)
                .compose(SchedulerUtils.toMain())
    }
}