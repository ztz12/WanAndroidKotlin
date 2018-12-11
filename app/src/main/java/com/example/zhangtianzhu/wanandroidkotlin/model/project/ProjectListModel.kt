package com.example.zhangtianzhu.wanandroidkotlin.model.project

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class ProjectListModel:BaseModel() {
    fun getProjectListData(page:Int,cid:Int):Observable<HttpResult<ArticleData>>{
        return RetrofitService.service.getProjectListData(page,cid)
                .compose(SchedulerUtils.toMain())
    }
}