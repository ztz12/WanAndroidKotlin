package com.example.zhangtianzhu.wanandroidkotlin.model.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.AllTodoResponseData
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoResponseData
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class TodoListModel:BaseModel() {
    fun getTodoList(type:Int):Observable<HttpResult<AllTodoResponseData>>{
        return RetrofitService.service.getTodoList(type)
                .compose(SchedulerUtils.toMain())
    }

    fun getNoTodoList(page:Int,type:Int):Observable<HttpResult<TodoResponseData>>{
        return RetrofitService.service.getNoTodoList(page, type)
                .compose(SchedulerUtils.toMain())
    }

    fun getDoneTodoList(page: Int,type: Int):Observable<HttpResult<TodoResponseData>>{
        return RetrofitService.service.getDoneList(page, type)
                .compose(SchedulerUtils.toMain())
    }

    fun updateTodoById(id:Int,status:Int):Observable<HttpResult<Any>>{
        return RetrofitService.service.updateTodoById(id,status)
                .compose(SchedulerUtils.toMain())
    }

    fun deleteTodoById(id:Int):Observable<HttpResult<Any>>{
        return RetrofitService.service.deleteTodoById(id)
                .compose(SchedulerUtils.toMain())
    }

}