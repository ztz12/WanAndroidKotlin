package com.example.zhangtianzhu.wanandroidkotlin.contract.home

import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData

interface SearchListContract {
    interface View : CommonContract.View{
        fun showSearchListData(articleData: ArticleData,isRefresh:Boolean)
    }

    interface Presenter : CommonContract.Presenter<View>{
        fun refreshData(key:String)

        fun loadMore(key:String)

        fun querySearchListData(page:Int,key:String)
    }
}