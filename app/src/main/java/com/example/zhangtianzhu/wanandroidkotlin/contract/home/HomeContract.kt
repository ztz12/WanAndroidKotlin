package com.example.zhangtianzhu.wanandroidkotlin.contract.home

import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.constant.BannerData

interface HomeContract {
    interface View:CommonContract.View{
        fun scrollTop()

        fun showBannerData(bannerData: List<BannerData>)

        fun showArticleData(articleData: ArticleData,isRefresh:Boolean)

    }

    interface Presenter : CommonContract.Presenter<View>{

        fun getBannerData()

        fun getArticleData(num:Int)

        fun getHomeData()

        fun autoRefresh()

        fun loadMore()

    }
}