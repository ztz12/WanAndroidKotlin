package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.home.CollectAdapter
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseMvpFragment
import com.example.zhangtianzhu.wanandroidkotlin.constant.CollectionArticle
import com.example.zhangtianzhu.wanandroidkotlin.constant.CollectionResponseBody
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.CollectContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.CollectPresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.ContentActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_collect.*


class CollectFragment : BaseMvpFragment<CollectContract.View,CollectContract.Presenter>(), CollectContract.View {
    override fun createPresenter(): CollectContract.Presenter = CollectPresenter()

    private val mData = mutableListOf<CollectionArticle>()

    private val mAdapter by lazy { CollectAdapter(_mActivity, data = mData) }

    private val linearLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(_mActivity) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_collect
    }

    override fun getData() {
        rl_collect.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }

        mAdapter.run {
            bindToRecyclerView(rl_collect)
            setEmptyView(R.layout.fragment_empty_layout)
            onItemClickListener = this@CollectFragment.onItemClickListener
            onItemChildClickListener = this@CollectFragment.onItemChildClickListener
        }
        refreshData()
    }

    override fun initData() {
        mPresenter?.getCollectList(0)
    }

    override fun scrollTop() {
        rl_collect.run {
            if(linearLayoutManager.findFirstVisibleItemPosition()>20){
                scrollToPosition(0)
            }else {
                smoothScrollToPosition(0)
            }
        }
    }

    companion object {
        fun getInstance(param1: String, param2: String): CollectFragment {
            val fragment = CollectFragment()
            val bundle = Bundle()
            bundle.putString(Constants.PARAM1, param1)
            bundle.putString(Constants.PARAM2, param2)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun showCollectList(collectArticles: CollectionResponseBody, isRefresh: Boolean) {
        collectArticles.datas.let {
            mAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                val size = it.size
                if (size < collectArticles.size) {
                    loadMoreEnd(isRefresh)
                } else {
                    loadMoreComplete()
                }
            }
        }
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        if(mData.size>0){
            val data = mData[position]
            Intent(_mActivity,ContentActivity::class.java).run {
                putExtra(Constants.CONTENT_ID_KEY,data.id)
                putExtra(Constants.CONTENT_URL_KEY,data.link)
                putExtra(Constants.CONTENT_TITLE_KEY,data.title)
                startActivity(this)
            }
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        if(mData.size>0){
            val data = mData[position]
            when(view.id){
                R.id.iv_collect ->{
                    mAdapter.remove(position)
                    mPresenter?.removeCollectArticles(data.id,data.originId)
                }
            }
        }
    }
    override fun removeCollectSuccess(success: Boolean) {
        if (success) {
            DialogUtil.showSnackBar(_mActivity, getString(R.string.cancel_collect_success))
        }
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(_mActivity, msg!!)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun lazyLoad() {

    }

    fun requestData(){
        mPresenter?.getCollectList(0)
    }

    private fun refreshData() {
        collect_refresh.setOnRefreshListener {
            setRefreshThemeColor(collect_refresh)
            mPresenter?.autoRefresh()
            collect_refresh.finishRefresh(1000)
        }

        collect_refresh.setOnLoadMoreListener {
            mPresenter?.loadMore()
            collect_refresh.finishLoadMore(1000)
        }
    }
}