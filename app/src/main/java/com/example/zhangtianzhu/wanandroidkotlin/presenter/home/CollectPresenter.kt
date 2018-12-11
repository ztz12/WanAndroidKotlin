package com.example.zhangtianzhu.wanandroidkotlin.presenter.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.CollectContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.home.CollectModel

class CollectPresenter:BasePresenter<CollectContract.View>(),CollectContract.Presenter {
    private var isRefresh:Boolean = true

    private var mCurrentPage:Int = 0

    private val collectModel :CollectModel by lazy { CollectModel() }

    override fun getCollectList(page: Int) {
        mView?.showLoading()
        val disposable = collectModel.getCollectArticles(page)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showCollectList(results.data,isRefresh)
                        }
                        hideLoading()
                    }
                },{
                    t ->
                    mView?.apply {
                        hideLoading()
                        showErrorMsg(ErrorException.handleException(t))
                    }
                })
        addDisposed(disposable)
    }

    override fun removeCollectArticles(id: Int, originalId: Int) {
        mView?.showLoading()
        val disposable = collectModel.removeCollectArticles(id,originalId)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            removeCollectSuccess(true)
                        }
                        hideLoading()
                    }
                },{
                    t ->
                    mView?.apply {
                        hideLoading()
                        ErrorException.handleException(t)
                    }
                })
        addDisposed(disposable)
    }

    override fun autoRefresh() {
        isRefresh = true
        mCurrentPage = 0
        getCollectList(mCurrentPage)
    }

    override fun loadMore() {
        isRefresh = false
        mCurrentPage++
        getCollectList(mCurrentPage)
    }
}