package com.example.zhangtianzhu.wanandroidkotlin.presenter.knowledge

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.contract.knowledge.KnowledgeContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.knowledge.KnowledgeSystemModel

class KnowledgePresenter : BasePresenter<KnowledgeContract.View>(),KnowledgeContract.Presenter{

    private val knowledgeModel:KnowledgeSystemModel by lazy { KnowledgeSystemModel() }

    override fun getKnowledgeTreeData() {
        mView?.showLoading()
        val disposable = knowledgeModel.getKnowledgeSystemData()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showKnowledgeData(results.data)
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

    override fun refreshKnowledgeSystem() {
        getKnowledgeTreeData()
    }
}