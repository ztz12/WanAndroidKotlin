package com.example.zhangtianzhu.wanandroidkotlin.model.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.HotSearchBean
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class SearchModel : BaseModel(){
    fun getHotSearchData() : Observable<HttpResult<MutableList<HotSearchBean>>> {
        return RetrofitService.service.hotSearchData()
                .compose(SchedulerUtils.toMain())
    }
}