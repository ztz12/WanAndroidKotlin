package com.example.zhangtianzhu.wanandroidkotlin.presenter.project

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.ColorEvent
import com.example.zhangtianzhu.wanandroidkotlin.contract.project.ProjectContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.project.ProjectModel
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus

class ProjectPresenter: BasePresenter<ProjectContract.View>(),ProjectContract.Presenter {

    private val mModel:ProjectModel by lazy { ProjectModel() }
    override fun getProjectData() {
        mView?.showLoading()
        val disposable = mModel.getProjectData()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showProjectData(results.data)
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

    override fun registerEvent() {
        addSubscribe(RxBus.default.toFlowable(ColorEvent::class.java)
                .filter(ColorEvent::isChangeColor)
                .subscribe { mView?.changeColor() })
    }
}