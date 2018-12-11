package com.example.zhangtianzhu.wanandroidkotlin.contract.home

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView

interface MainContract {
    interface View : IView {
        fun showLoginView()

        fun showLoginOutView()

        fun loginOutSuccess(success: Boolean)

        fun changeColor()

        fun loadTopArticle()

        fun acceptNightModel(isNightModel:Boolean)

        fun acceptLoadPhoto(isLoadPhoto:Boolean)

    }

    interface Presenter : IPresenter<View> {
        fun registerEvent()

        fun loginOut()
    }
}