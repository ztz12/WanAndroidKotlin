package com.example.zhangtianzhu.wanandroidkotlin.presenter.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.ColorEvent
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.AboutUsContract
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus

class AboutUsPresenter:BasePresenter<AboutUsContract.View>(),AboutUsContract.Presenter {
    override fun registerEvent() {
        addSubscribe(RxBus.default.toFlowable(ColorEvent::class.java)
                .filter(ColorEvent::isChangeColor)
                .subscribe{mView?.changeColor()})
    }
}