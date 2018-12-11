package com.example.zhangtianzhu.wanandroidkotlin.presenter.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.ColorEvent
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.LoadPhoto
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.LoadTopArticle
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.NightModelEvent
import com.example.zhangtianzhu.wanandroidkotlin.bean.login.LoginEvent
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.MainContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.home.MainModel
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus

class MainPresenter : BasePresenter<MainContract.View>(), MainContract.Presenter {

    private val model: MainModel by lazy { MainModel() }
    override fun loginOut() {
        mView?.showLoading()
        val disposable = model.loginOut()
                .retryWhen(RetryWithDelay())
                .subscribe({ results ->
                    mView?.apply {
                        if (results.errorCode != 0) {
                            showErrorMsg(results.errorMsg)
                        } else {
                            loginOutSuccess(true)
                        }
                        hideLoading()
                    }
                }, { t ->
                    mView?.apply {
                        hideLoading()
                        showErrorMsg(ErrorException.handleException(t))
                    }
                })
        addDisposed(disposable)
    }

    override fun registerEvent() {
        //登录
        addSubscribe(RxBus.default.toFlowable(LoginEvent::class.java)
                .filter(LoginEvent::isLogin)
                .subscribe { mView?.showLoginView() })

        //退出登录
        addSubscribe(RxBus.default.toFlowable(LoginEvent::class.java)
                .filter { (isLogin) -> !isLogin }
                .subscribe { mView?.showLoginOutView() })

        //改变颜色
        addSubscribe(RxBus.default.toFlowable(ColorEvent::class.java)
                .filter(ColorEvent::isChangeColor)
                .subscribe { mView?.changeColor() })

        //首页置顶文章
        addSubscribe(RxBus.default.toFlowable(LoadTopArticle::class.java)
                .filter(LoadTopArticle::isLoadTopArticle)
                .subscribe { mView?.loadTopArticle() })

        //是否设置夜间模式
        addSubscribe(RxBus.default.toFlowable(NightModelEvent::class.java)
                .map(NightModelEvent::isNightModel)
                .subscribe { isNightModel -> run { mView?.acceptNightModel(isNightModel) } })

        //是否加载图片
        addSubscribe(RxBus.default.toFlowable(LoadPhoto::class.java)
                .map(LoadPhoto::isLoadPhoto)
                .subscribe { isLoadPhoto -> run { mView?.acceptLoadPhoto(isLoadPhoto) } })

    }
}