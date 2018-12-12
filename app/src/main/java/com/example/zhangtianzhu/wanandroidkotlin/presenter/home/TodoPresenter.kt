package com.example.zhangtianzhu.wanandroidkotlin.presenter.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.ColorEvent
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.TodoContract
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus

class TodoPresenter :BasePresenter<TodoContract.View>(),TodoContract.Presenter{
    override fun registerEvent() {
        addSubscribe(RxBus.default.toFlowable(ColorEvent::class.java)
                .filter(ColorEvent::isChangeColor)
                .subscribe { mView?.changeColor() })
    }
}