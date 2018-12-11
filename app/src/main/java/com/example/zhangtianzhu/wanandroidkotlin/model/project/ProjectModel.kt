package com.example.zhangtianzhu.wanandroidkotlin.model.project

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.constant.ProjectTreeData
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class ProjectModel:BaseModel() {
    fun getProjectData():Observable<HttpResult<MutableList<ProjectTreeData>>>{
        return RetrofitService.service.getProjectData()
                .compose(SchedulerUtils.toMain())
    }
}