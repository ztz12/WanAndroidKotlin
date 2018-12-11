package com.example.zhangtianzhu.wanandroidkotlin.contract.home

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView
import com.example.zhangtianzhu.wanandroidkotlin.constant.HotSearchBean
import com.example.zhangtianzhu.wanandroidkotlin.constant.SearchHistoryBean

interface SearchContract {
    interface View : IView{
        fun showHotSearchData(hotSearchList : MutableList<HotSearchBean>)

        fun showHistoryData(historyBeanList: MutableList<SearchHistoryBean>)
    }

    interface Presenter : IPresenter<View>{
        fun getHotSearchData()

        fun queryHistory()

        fun clearAllHistoryData()

        fun saveSearchKey(key:String)

        fun deleteId(id:Long)
    }
}