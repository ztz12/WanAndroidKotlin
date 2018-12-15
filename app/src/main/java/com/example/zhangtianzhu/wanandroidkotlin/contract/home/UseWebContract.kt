package com.example.zhangtianzhu.wanandroidkotlin.contract.home

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView
import com.example.zhangtianzhu.wanandroidkotlin.constant.UseWebsiteBean

interface UseWebContract {
    interface View:IView{
        fun showUseWebData(useWebsiteBean: MutableList<UseWebsiteBean>)
    }

    interface Presenter:IPresenter<View>{
        fun getUseWebData()
    }
}