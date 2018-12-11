package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatDelegate
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.home.HomeAdapter
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticelDetail
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.SearchListContract
import com.example.zhangtianzhu.wanandroidkotlin.http.NetWorkUtils
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.SearchListPresenter
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_search_data.*
import kotlinx.android.synthetic.main.toolbar.*

class SearchDataActivity : BaseSwipeBackActivity() , SearchListContract.View{

    private val mPresenter : SearchListPresenter by lazy { SearchListPresenter() }

    private val mSearchList = mutableListOf<ArticelDetail>()

    private val mAdapter : HomeAdapter by lazy { HomeAdapter(this,mSearchList) }

    private val linearLayoutManager :LinearLayoutManager by lazy { LinearLayoutManager(this) }

    private var key : String = ""

    override fun getLayoutId(): Int {
        return R.layout.activity_search_data
    }

    override fun initData() {
        mPresenter.attachView(this)
        val bundle = intent.extras
        key = bundle.getString(Constants.SEARCH_KEY)
        toolbar.run {
            title = key
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        StatusBarUtil.setStatusColor(window, ContextCompat.getColor(this,R.color.main_status_bar_blue),1.0f)
        toolbar.setNavigationOnClickListener{onBackPressedSupport()}
    }

    override fun getData() {
        rl_searchList.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }

        mAdapter.run {
            bindToRecyclerView(rl_searchList)
            setEmptyView(R.layout.fragment_empty_layout)
            onItemClickListener = this@SearchDataActivity.onItemClickListener
            onItemChildClickListener = this@SearchDataActivity.onItemChildClickListener
        }

        mPresenter.querySearchListData(0,key)

        refreshData()

        fab_search.setOnClickListener{scrollTop()}
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    override fun showSearchListData(articleData: ArticleData, isRefresh: Boolean) {
        articleData.datas.let {
            mAdapter.run {
                if(isRefresh){
                    replaceData(it)
                }else {
                    addData(it)
                }
                val size = it.size
                if(size<articleData.size){
                    loadMoreEnd(isRefresh)
                }else{
                    loadMoreComplete()
                }
            }
        }
    }

    private fun scrollTop(){
        if(linearLayoutManager.findFirstVisibleItemPosition()>20){
            rl_searchList.scrollToPosition(0)
        }else{
            rl_searchList.smoothScrollToPosition(0)
        }
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        if(mSearchList.size>0){
            val data = mSearchList[position]
            Intent(this,ContentActivity::class.java).run {
                putExtra(Constants.CONTENT_URL_KEY,data.link)
                putExtra(Constants.CONTENT_TITLE_KEY,data.title)
                putExtra(Constants.CONTENT_ID_KEY,data.id)
                startActivity(this)
            }
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        if(mSearchList.size>0){
            val data = mSearchList[position]
            when(view.id){
                R.id.iv_collect ->
                    if(isLogin){
                        if(!NetWorkUtils.isNetWorkAvailable(WanAndroidApplication.context)){
                            DialogUtil.showSnackBar(this,getString(R.string.http_error))
                            return@OnItemChildClickListener
                        }
                        val collect = data.collect
                        data.collect = !collect
                        mAdapter.setData(position,data)
                        if(collect){
                            mPresenter.cancelCollectId(data.id)
                        }else{
                            mPresenter.addCollectId(data.id)
                        }
                    }else{
                        DialogUtil.showSnackBar(this,getString(R.string.login_tint))
                    }
            }
        }
    }
    override fun collectSuccess(success: Boolean) {
        if(success){
            DialogUtil.showSnackBar(this,getString(R.string.collect_success))
        }
    }

    override fun cancelCollectSuccess(success: Boolean) {
        if(success){
            DialogUtil.showSnackBar(this,getString(R.string.cancel_collect_success))
        }
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(this,msg!!)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    private fun refreshData(){
        search_refresh.run {
            setOnRefreshListener {
                mPresenter.refreshData(key)
                finishRefresh(1000)
            }

            setOnLoadMoreListener {
                mPresenter.loadMore(key)
                finishLoadMore(1000)
            }
        }
    }
}
