package com.example.zhangtianzhu.wanandroidkotlin.presenter.knowledge

import com.example.zhangtianzhu.wanandroidkotlin.contract.knowledge.KnowledgeDetailContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.knowledge.KnowledgeDetailModel
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.CommonPresenter

class KnowledgeDetailPresenter : CommonPresenter<KnowledgeDetailContract.View>(),KnowledgeDetailContract.Presenter{

    private val mModel : KnowledgeDetailModel by lazy { KnowledgeDetailModel() }

    private var mCurrentPage = 0

    private var isRefresh = true

    override fun getKnowledgeDetailData(page: Int, cid: Int) {
        mView?.showLoading()
        val disposable = mModel.getKnowledgeDetailData(page,cid)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showKnowledgeDetail(results.data,isRefresh)
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

    override fun refreshData(cid: Int) {
        isRefresh = true
        mCurrentPage = 0
        getKnowledgeDetailData(mCurrentPage,cid)
    }

    override fun loadMore(cid: Int) {
        isRefresh = false
        mCurrentPage++
        getKnowledgeDetailData(mCurrentPage,cid)
    }

}