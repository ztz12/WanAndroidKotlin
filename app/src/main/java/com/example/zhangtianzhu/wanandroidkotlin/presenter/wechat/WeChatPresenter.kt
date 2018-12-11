package com.example.zhangtianzhu.wanandroidkotlin.presenter.wechat

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.ColorEvent
import com.example.zhangtianzhu.wanandroidkotlin.contract.wechat.WeChatContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.wechat.WeChatModel
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus

class WeChatPresenter:BasePresenter<WeChatContract.View>(),WeChatContract.Presenter {

    private val mModel :WeChatModel by lazy { WeChatModel() }
    override fun getWeChatData() {
        mView?.showLoading()
        val disposable = mModel.getWeChatData()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showWebChatData(results.data)
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