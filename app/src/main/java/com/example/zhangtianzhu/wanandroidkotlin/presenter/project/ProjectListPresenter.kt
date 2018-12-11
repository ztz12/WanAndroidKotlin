package com.example.zhangtianzhu.wanandroidkotlin.presenter.project

import com.example.zhangtianzhu.wanandroidkotlin.contract.project.ProjectListContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.project.ProjectListModel
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.CommonPresenter

class ProjectListPresenter:CommonPresenter<ProjectListContract.View>(),ProjectListContract.Presenter {
    private var isRefresh = true

    private var mCurrentPage = 0
    private val mModel:ProjectListModel by lazy { ProjectListModel() }

    override fun getProjectList(pageNum: Int, cid: Int) {
        mView?.showLoading()
        val disposable = mModel.getProjectListData(pageNum,cid)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showProjectList(results.data,isRefresh)
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
        getProjectList(mCurrentPage,cid)
    }

    override fun loadMore(cid: Int) {
        isRefresh = false
        mCurrentPage++
        getProjectList(mCurrentPage,cid)
    }
}