package com.example.zhangtianzhu.wanandroidkotlin.presenter.knowledge

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.ColorEvent
import com.example.zhangtianzhu.wanandroidkotlin.contract.knowledge.KnowledgeListContract
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus

class KnowledgeListPresenter:BasePresenter<KnowledgeListContract.View>(),KnowledgeListContract.Presenter {
    override fun registerEvent() {
        addSubscribe(RxBus.default.toFlowable(ColorEvent::class.java)
                .filter(ColorEvent::isChangeColor)
                .subscribe { mView?.changeColor() })
    }
}