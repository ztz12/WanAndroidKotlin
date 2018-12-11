package com.example.zhangtianzhu.wanandroidkotlin.contract.knowledge

import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.CommonContract

interface KnowledgeDetailContract {
    interface View : CommonContract.View{
        fun scrollTop()

        fun showKnowledgeDetail(articleData: ArticleData,isRefresh:Boolean)
    }

    interface Presenter: CommonContract.Presenter<View>{

        fun refreshData(cid:Int)

        fun loadMore(cid:Int)

        fun getKnowledgeDetailData(page:Int,cid:Int)
    }
}