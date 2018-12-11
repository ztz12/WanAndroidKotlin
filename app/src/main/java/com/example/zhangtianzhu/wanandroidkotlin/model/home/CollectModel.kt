package com.example.zhangtianzhu.wanandroidkotlin.model.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.CollectionResponseBody
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class CollectModel :BaseModel(){
    fun getCollectArticles(page:Int):Observable<HttpResult<CollectionResponseBody>>{
        return RetrofitService.service.getCollectList(page)
                .compose(SchedulerUtils.toMain())
    }

    fun removeCollectArticles(id:Int,originId:Int):Observable<HttpResult<Any>>{
        return RetrofitService.service.removeCollectArticle(id,originId)
                .compose(SchedulerUtils.toMain())
    }
}