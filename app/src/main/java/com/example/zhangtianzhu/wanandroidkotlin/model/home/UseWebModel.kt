package com.example.zhangtianzhu.wanandroidkotlin.model.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.constant.UseWebsiteBean
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class UseWebModel:BaseModel() {
    fun getUseWebData(): Observable<HttpResult<MutableList<UseWebsiteBean>>> {
        return RetrofitService.service.getUseWebData()
                .compose(SchedulerUtils.toMain())
    }
}