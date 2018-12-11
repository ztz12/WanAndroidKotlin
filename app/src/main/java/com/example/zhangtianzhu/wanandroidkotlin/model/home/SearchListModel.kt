package com.example.zhangtianzhu.wanandroidkotlin.model.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class SearchListModel :BaseModel(){
    fun getSearchList(page:Int,key:String): Observable<HttpResult<ArticleData>> {
        return RetrofitService.service.getSearchList(page,key)
                .compose(SchedulerUtils.toMain())
    }
}