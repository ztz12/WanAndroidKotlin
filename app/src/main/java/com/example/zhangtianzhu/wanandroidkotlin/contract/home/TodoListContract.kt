package com.example.zhangtianzhu.wanandroidkotlin.contract.home

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.TodoEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoResponseData

interface TodoListContract {
    interface View:IView{

        fun showTodoEvent(todoEvent: TodoEvent)

        fun showTodoList(todoResponseData: TodoResponseData,isRefresh:Boolean)

        fun showDeleteSuccess(isSuccess:Boolean)

        fun showUpdateSuccess(isSuccess:Boolean)

        fun todoRefreshData(mType:Int)

    }

    interface Presenter:IPresenter<View>{

        fun registerEvent()

        fun getTodoList(type:Int)

        fun getNoTodoList(page:Int,type: Int)

        fun getDoneTodoList(page: Int,type: Int)

        fun updateTodoList(id:Int,status:Int)

        fun deleteTodoList(id:Int)

        fun refreshData(type: Int,isDone:Boolean)

        fun loadMore(type: Int,isDone: Boolean)
    }
}