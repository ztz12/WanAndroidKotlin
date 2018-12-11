package com.example.zhangtianzhu.wanandroidkotlin.model.knowledge

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.constant.KnowledgeTreeData
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class KnowledgeSystemModel :BaseModel(){
    fun getKnowledgeSystemData(): Observable<HttpResult<List<KnowledgeTreeData>>> {
        return  RetrofitService.service.getKnowledgeTree()
                .compose(SchedulerUtils.toMain())
    }
}