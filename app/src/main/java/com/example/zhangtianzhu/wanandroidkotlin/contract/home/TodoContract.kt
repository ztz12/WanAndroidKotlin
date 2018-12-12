package com.example.zhangtianzhu.wanandroidkotlin.contract.home

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView

interface TodoContract {
    interface View:IView{
        fun changeColor()
    }

    interface Presenter:IPresenter<View>{
        fun registerEvent()
    }
}