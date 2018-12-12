package com.example.zhangtianzhu.wanandroidkotlin.contract.home

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView

interface AddTodoContract {
    interface View:IView{
        fun showAddSuccess(isSuccess:Boolean)

        fun showUpdateSuccess(isSuccess: Boolean)


        fun getTitleInfo():String
        fun getContent():String
        fun getCurrentDate():String
        fun getStatus():Int
        fun getType():Int

    }

    interface Presenter:IPresenter<View>{
        fun add()

        fun update(id:Int)
    }
}