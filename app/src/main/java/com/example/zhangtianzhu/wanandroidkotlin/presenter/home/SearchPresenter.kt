package com.example.zhangtianzhu.wanandroidkotlin.presenter.home

import com.example.zhangtianzhu.wanandroidkotlin.base.BasePresenter
import com.example.zhangtianzhu.wanandroidkotlin.constant.SearchHistoryBean
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.SearchContract
import com.example.zhangtianzhu.wanandroidkotlin.http.ErrorException
import com.example.zhangtianzhu.wanandroidkotlin.http.RetryWithDelay
import com.example.zhangtianzhu.wanandroidkotlin.model.home.SearchModel
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.litepal.LitePal
import org.litepal.extension.delete
import org.litepal.extension.deleteAll
import org.litepal.extension.find
import org.litepal.extension.findAll

class SearchPresenter : BasePresenter<SearchContract.View>(),SearchContract.Presenter{
    private val searchModel : SearchModel by lazy { SearchModel() }
    override fun getHotSearchData() {
        mView?.showLoading()
        val disposable = searchModel.getHotSearchData()
                .retryWhen(RetryWithDelay())
                .subscribe({
                    results ->
                    mView?.run {
                        if(results.errorCode!=0){
                            showErrorMsg(results.errorMsg)
                        }else{
                            showHotSearchData(results.data)
                        }
                        hideLoading()
                    }
                },{
                    t ->
                    mView?.run {
                        hideLoading()
                        showErrorMsg(ErrorException.handleException(t))
                    }
                })
        addDisposed(disposable)
    }

    override fun queryHistory() {
        doAsync {
            val historyBean = LitePal.findAll<SearchHistoryBean>()
            historyBean.reverse()
            uiThread {
                mView?.showHistoryData(historyBean)
            }
        }
    }

    override fun clearAllHistoryData() {
        doAsync {
            LitePal.deleteAll<SearchHistoryBean>()
        }
    }

    override fun saveSearchKey(key: String) {
        doAsync {
            val historyBean = SearchHistoryBean(key.trim())
            val bean = LitePal.where("key = '${key.trim()}'").find<SearchHistoryBean>()
            if(bean.isEmpty()){
                historyBean.save()
            }else{
                deleteId(bean[0].id)
                historyBean.save()
            }
        }
    }

    override fun deleteId(id: Long) {
        doAsync {
            LitePal.delete<SearchHistoryBean>(id)
        }
    }
}