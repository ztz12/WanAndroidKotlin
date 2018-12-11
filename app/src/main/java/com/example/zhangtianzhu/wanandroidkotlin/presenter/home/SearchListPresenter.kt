package com.example.zhangtianzhu.wanandroidkotlin.presenter.home

import com.example.zhangtianzhu.wanandroidkotlin.bean.home.NightModelEvent
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.SearchListContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.home.SearchListModel
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus

class SearchListPresenter : CommonPresenter<SearchListContract.View>(),SearchListContract.Presenter{

    private val searchListModel : SearchListModel by lazy { SearchListModel() }

    private var mCurrentPage = 0

    private var isRefresh = true

    override fun querySearchListData(page: Int, key: String) {
        mView?.showLoading()
        val disposable = searchListModel.getSearchList(page, key)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showSearchListData(results.data,isRefresh)
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

    override fun refreshData(key: String) {
        isRefresh = true
        mCurrentPage = 0
        querySearchListData(mCurrentPage,key)
    }

    override fun loadMore(key: String) {
        isRefresh = false
        mCurrentPage++
        querySearchListData(mCurrentPage,key)
    }

}