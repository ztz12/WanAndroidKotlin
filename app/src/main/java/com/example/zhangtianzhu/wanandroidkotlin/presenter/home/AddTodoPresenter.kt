package com.example.zhangtianzhu.wanandroidkotlin.presenter.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.AddTodoContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.home.AddTodoModel

class AddTodoPresenter:BasePresenter<AddTodoContract.View>(),AddTodoContract.Presenter {

    private val mModel : AddTodoModel by lazy { AddTodoModel() }

    override fun add() {
        val title = mView?.getTitleInfo().toString()
        val date = mView?.getCurrentDate().toString()
        val content = mView?.getContent().toString()
        val type = mView?.getType() ?: 0
        val map = mutableMapOf<String,Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date
        mView?.showLoading()
        val disposable = mModel.addTodo(map)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showAddSuccess(true)
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

    override fun update(id: Int) {
        val title = mView?.getTitleInfo().toString()
        val content = mView?.getContent().toString()
        val date = mView?.getCurrentDate().toString()
        val status = mView?.getStatus() ?:0
        val type = mView?.getType() ?:0
        val map = mutableMapOf<String,Any>()
        map["type"] = type
        map["title"] = title
        map["content"] = content
        map["date"] = date
        map["status"] =status
        mView?.showLoading()
        val disposable = mModel.updateTodo(id,map)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showUpdateSuccess(true)
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
}