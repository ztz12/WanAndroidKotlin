package com.example.zhangtianzhu.wanandroidkotlin.contract.home

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView

interface CommonContract {

    interface View : IView{
        fun collectSuccess(success:Boolean)

        fun cancelCollectSuccess(success: Boolean)
    }

    interface Presenter<in V :View> : IPresenter<V>{
        fun addCollectId(id:Int)

        fun cancelCollectId(id:Int)
    }
}