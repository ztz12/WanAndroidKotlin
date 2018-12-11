package com.example.zhangtianzhu.wanandroidkotlin.model.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticelDetail
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.constant.BannerData
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class HomeModel :BaseModel(){
    fun requestBannerData():Observable<HttpResult<List<BannerData>>>{
        return RetrofitService.service.getBannerData()
                .compose(SchedulerUtils.toMain())
    }

    fun requestTopArticlesData():Observable<HttpResult<MutableList<ArticelDetail>>>{
        return RetrofitService.service.getTopArticles()
                .compose(SchedulerUtils.toMain())
    }

    fun requestArticlesData(pageNum:Int):Observable<HttpResult<ArticleData>>{
        return RetrofitService.service.getArticles(pageNum)
                .compose(SchedulerUtils.toMain())
    }
}