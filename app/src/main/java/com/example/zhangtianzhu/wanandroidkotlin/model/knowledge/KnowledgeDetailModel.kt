package com.example.zhangtianzhu.wanandroidkotlin.model.knowledge

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class KnowledgeDetailModel:BaseModel() {
    fun getKnowledgeDetailData(page:Int,cid:Int):Observable<HttpResult<ArticleData>>{
        return RetrofitService.service.getKnowledgeList(page,cid)
                .compose(SchedulerUtils.toMain())
    }
}