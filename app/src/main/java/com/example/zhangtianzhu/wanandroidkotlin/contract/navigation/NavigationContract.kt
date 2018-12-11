package com.example.zhangtianzhu.wanandroidkotlin.contract.navigation

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView
import com.example.zhangtianzhu.wanandroidkotlin.constant.NavigationData

interface NavigationContract {
    interface View : IView {
        fun scrollTop()

        fun showNavaigationData(navigationData: MutableList<NavigationData>)
    }

    interface Presenter : IPresenter<View>{
        fun getNavigationData()
    }
}