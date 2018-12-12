package com.example.zhangtianzhu.wanandroidkotlin.model.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class AddTodoModel :BaseModel(){
    fun addTodo(map: MutableMap<String,Any>): Observable<HttpResult<Any>> {
        return RetrofitService.service.addTodo(map)
                .compose(SchedulerUtils.toMain())
    }

    fun updateTodo(id:Int,map: MutableMap<String, Any>):Observable<HttpResult<Any>>{
        return RetrofitService.service.updateTodo(id, map)
                .compose(SchedulerUtils.toMain())
    }
}