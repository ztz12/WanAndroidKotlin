package com.example.zhangtianzhu.wanandroidkotlin.contract.project

import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.CommonContract

interface ProjectListContract {
    interface View:CommonContract.View{
        fun scrollTop()

        fun showProjectList(articleData: ArticleData,isRefresh:Boolean)
    }

    interface Presenter:CommonContract.Presenter<View>{
        fun refreshData(cid:Int)

        fun loadMore(cid:Int)

        fun getProjectList(pageNum:Int,cid:Int)
    }
}