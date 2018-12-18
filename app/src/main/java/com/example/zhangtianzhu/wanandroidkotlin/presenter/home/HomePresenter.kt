package com.example.zhangtianzhu.wanandroidkotlin.presenter.home

import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticelDetail
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.HomeContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.home.HomeModel
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import io.reactivex.Observable
import io.reactivex.functions.BiFunction

class HomePresenter :CommonPresenter<HomeContract.View>(),HomeContract.Presenter {

    private val homeMode : HomeModel by lazy { HomeModel() }
    private var isRefresh = true
    private var mCurrentPage = 0
    override fun getBannerData() {
        mView?.showLoading()
        val disposable = homeMode.requestBannerData()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showBannerData(results.data)
                        }
                        hideLoading()
                    }
                },{
                    t->
                    mView?.apply {
                        hideLoading()
                        showErrorMsg(ErrorException.handleException(t))
                    }
                })
        addDisposed(disposable)
    }

    override fun getArticleData(num: Int) {
        if(num ==0)
            mView?.showLoading()
        val disposable = homeMode.requestArticlesData(num)
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showArticleData(results.data,isRefresh)
                        }
                        hideLoading()
                    }
                },{
                    t->
                    mView?.apply {
                        hideLoading()
                        showErrorMsg(ErrorException.handleException(t))
                    }
                })
        addDisposed(disposable)
    }

    override fun getHomeData() {
        mView?.showLoading()
        getBannerData()
        val observable = if(ConfigureUtils.getIsShowTopArticle()){
            homeMode.requestArticlesData(0)
        }else{
            Observable.zip(homeMode.requestTopArticlesData(),homeMode.requestArticlesData(0),
                    BiFunction<HttpResult<MutableList<ArticelDetail>>,HttpResult<ArticleData>,
                            HttpResult<ArticleData>>{t1, t2 ->
                        t1.data.forEach {
                            it.top = "1"
                        }
                        t2.data.datas.addAll(0,t1.data)
                        t2
                    })
        }
        val disposable = observable.retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.apply {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showArticleData(results.data,isRefresh)
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

    override fun autoRefresh() {
        isRefresh = true
        mCurrentPage = 0
        getHomeData()
    }

    override fun loadMore() {
        mCurrentPage++
        isRefresh = false
        getArticleData(mCurrentPage)
    }

}