package com.example.zhangtianzhu.wanandroidkotlin.model.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class CommonModel : BaseModel(){
    fun addCollectId(id:Int):Observable<HttpResult<Any>>{
        return RetrofitService.service.addCollectArticle(id)
                .compose(SchedulerUtils.toMain())
    }

    fun cancelCollectId(id:Int):Observable<HttpResult<Any>>{
        return RetrofitService.service.cancelCollectArticle(id)
                .compose(SchedulerUtils.toMain())
    }
}