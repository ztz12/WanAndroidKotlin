package com.example.zhangtianzhu.wanandroidkotlin.contract.home

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView
import com.example.zhangtianzhu.wanandroidkotlin.constant.CollectionResponseBody

interface CollectContract {
    interface View : IView{

         fun scrollTop()

        fun showCollectList(collectArticles:CollectionResponseBody,isRefresh:Boolean)

        fun removeCollectSuccess(success:Boolean)
    }

    interface Presenter : IPresenter<View>{
        fun getCollectList(page:Int)

        fun removeCollectArticles(id:Int,originalId: Int)

        fun autoRefresh()

        fun loadMore()

    }
}