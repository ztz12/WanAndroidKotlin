package com.example.zhangtianzhu.wanandroidkotlin.model.navigation

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.constant.NavigationData
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class NavigationModel :BaseModel(){
    fun getNaviagtionModel():Observable<HttpResult<MutableList<NavigationData>>>{
        return RetrofitService.service.getNavigationData()
                .compose(SchedulerUtils.toMain())
    }
}