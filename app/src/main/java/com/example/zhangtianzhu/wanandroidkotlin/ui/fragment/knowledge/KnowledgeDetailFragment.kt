package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.knowledge

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.knowledge.KnowledgeDetailAdapter
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseMvpFragment
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticelDetail
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.contract.knowledge.KnowledgeDetailContract
import com.example.zhangtianzhu.wanandroidkotlin.http.NetWorkUtils
import com.example.zhangtianzhu.wanandroidkotlin.presenter.knowledge.KnowledgeDetailPresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.ContentActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_knowledge_detail.*


class KnowledgeDetailFragment :BaseMvpFragment<KnowledgeDetailContract.View,KnowledgeDetailContract.Presenter>(),KnowledgeDetailContract.View{

    override fun createPresenter(): KnowledgeDetailContract.Presenter = KnowledgeDetailPresenter()

    private var articleDetailData = mutableListOf<ArticelDetail>()

    private val linearManager : LinearLayoutManager by lazy { LinearLayoutManager(_mActivity) }

    private val mAdapter:KnowledgeDetailAdapter by lazy { KnowledgeDetailAdapter(_mActivity,articleDetailData) }

    private var cid : Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_knowledge_detail
    }

    companion object {
        fun getInstance(cid:Int):KnowledgeDetailFragment{
            val fragment = KnowledgeDetailFragment()
            val bundle = Bundle()
            bundle.putInt(Constants.CONTENT_CID_KEY,cid)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getData() {
        rl_knowledge_detail.run {
            layoutManager = linearManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }
        mAdapter.run {
            bindToRecyclerView(rl_knowledge_detail)
            setEmptyView(R.layout.fragment_empty_layout)
            onItemClickListener = this@KnowledgeDetailFragment.onItemClickListener
            onItemChildClickListener = this@KnowledgeDetailFragment.onItemChildClickListener
        }
        refreshData()
        mPresenter?.getKnowledgeDetailData(0,cid)
    }

    override fun initData() {
        cid = arguments?.getInt(Constants.CONTENT_CID_KEY) ?:0
    }

    override fun lazyLoad() {
    }

    override fun scrollTop() {
        rl_knowledge_detail.run {
            if(linearManager.findFirstVisibleItemPosition()>20){
                scrollToPosition(0)
            }else{
                smoothScrollToPosition(0)
            }
        }
    }

    override fun showKnowledgeDetail(articleData: ArticleData, isRefresh: Boolean) {
        articleData.datas.let {
            mAdapter.run {
                if(isRefresh){
                    replaceData(it)
                }else{
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

    override fun collectSuccess(success: Boolean) {
        if(success){
            DialogUtil.showSnackBar(_mActivity,getString(R.string.collect_success))
        }
    }

    override fun cancelCollectSuccess(success: Boolean) {
        if(success){
            DialogUtil.showSnackBar(_mActivity,getString(R.string.cancel_collect_success))
        }
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        if(articleDetailData.size>0){
            val data = articleDetailData[position]
            Intent(_mActivity, ContentActivity::class.java).run {
                putExtra(Constants.CONTENT_URL_KEY,data.link)
                putExtra(Constants.CONTENT_TITLE_KEY,data.title)
                putExtra(Constants.CONTENT_ID_KEY,data.id)
                startActivity(this)
            }
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        if(articleDetailData.size>0){
            val data = articleDetailData[position]
            when(view.id){
                R.id.iv_knowledge ->
                    if(isLogin){
                        if(!NetWorkUtils.isNetWorkAvailable(WanAndroidApplication.context)){
                            DialogUtil.showSnackBar(_mActivity,getString(R.string.http_error))
                            return@OnItemChildClickListener
                        }
                        val collect = data.collect
                        data.collect = !collect
                        mAdapter.setData(position,data)
                        if(collect){
                            mPresenter?.cancelCollectId(data.id)
                        }else{
                            mPresenter?.addCollectId(data.id)
                        }
                    }else{
                        DialogUtil.showSnackBar(_mActivity,getString(R.string.login_tint))
                    }
            }
        }
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(_mActivity,msg!!)
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        if(mDialog.isShowing){
            mDialog.dismiss()
        }
    }

    private fun refreshData(){
        knowledge_refresh_detail.run {
            setOnRefreshListener {
                setRefreshThemeColor(knowledge_refresh_detail)
                mPresenter?.refreshData(cid)
                finishRefresh(1000)
            }
        }
        knowledge_refresh_detail.run {
            setOnLoadMoreListener {
                mPresenter?.loadMore(cid)
                finishLoadMore(1000)
            }
        }
    }

}