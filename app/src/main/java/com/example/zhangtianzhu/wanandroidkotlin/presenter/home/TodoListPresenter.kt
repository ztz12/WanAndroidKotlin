package com.example.zhangtianzhu.wanandroidkotlin.presenter.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.TodoEvent
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.TodoListContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.home.TodoListModel
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus

class TodoListPresenter:BasePresenter<TodoListContract.View>(),TodoListContract.Presenter {
    private val mModel : TodoListModel by lazy { TodoListModel() }

    override fun registerEvent() {
        addSubscribe(RxBus.default.toFlowable(TodoEvent::class.java)
                .subscribe { todoEvent -> mView?.showTodoEvent(todoEvent) })
    }

    override fun getTodoList(type: Int) {
        mView?.showLoading()
        val disposable = mModel.getTodoList(type)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
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

    override fun getNoTodoList(page: Int, type: Int) {
        mView?.showLoading()
        val disposable = mModel.getNoTodoList(page,type)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showNoTodoList(results.data)
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

    override fun getDoneTodoList(page: Int, type: Int) {
        mView?.showLoading()
        val disposable = mModel.getDoneTodoList(page,type)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showNoTodoList(results.data)
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

    override fun updateTodoList(id: Int, status: Int) {
        mView?.showLoading()
        val disposable = mModel.updateTodoById(id, status)
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

    override fun deleteTodoList(id: Int) {
        mView?.showLoading()
        val disposable = mModel.deleteTodoById(id)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showDeleteSuccess(true)
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