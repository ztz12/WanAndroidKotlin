package com.example.zhangtianzhu.wanandroidkotlin.contract.knowledge

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView

interface KnowledgeListContract {
    interface View:IView{
        fun changeColor()
    }

    interface Presenter : IPresenter<View>{
        fun registerEvent()
    }
}