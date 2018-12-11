package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.knowledge

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.knowledge.KnowledgeSystemAdapter
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.KnowledgeTreeData
import com.example.zhangtianzhu.wanandroidkotlin.contract.knowledge.KnowledgeContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.knowledge.KnowledgePresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.knowledge.KnowledgeListActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_knowledge.*
import org.jetbrains.anko.support.v4.startActivity


class KnowledgeSystemFragment :BaseFragment(),KnowledgeContract.View{

    private val mPresenter:KnowledgePresenter by lazy { KnowledgePresenter() }

    private val linearLayoutManager : LinearLayoutManager by lazy { LinearLayoutManager(_mActivity) }

    private val knowledgeTreeData = mutableListOf<KnowledgeTreeData>()

    private val mAdapter : KnowledgeSystemAdapter by lazy { KnowledgeSystemAdapter(_mActivity,knowledgeTreeData) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_knowledge
    }

    override fun getData() {
        rl_knowledge.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }

        mAdapter.run {
            bindToRecyclerView(rl_knowledge)
            setEmptyView(R.layout.fragment_empty_layout)
            onItemClickListener = this@KnowledgeSystemFragment.onItemClickListener
        }

        mPresenter.getKnowledgeTreeData()

        refreshData()
    }

    override fun initData() {
        mPresenter.attachView(this)
    }

    companion object {
        fun getInstance(param1:String,param2:String):KnowledgeSystemFragment{
            val fragment = KnowledgeSystemFragment()
            val bundle = Bundle()
            bundle.putString(Constants.PARAM1,param1)
            bundle.putString(Constants.PARAM2,param2)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun scrollTop() {
        if(linearLayoutManager.findFirstVisibleItemPosition()>20){
            rl_knowledge.scrollToPosition(0)
        }else{
            rl_knowledge.smoothScrollToPosition(0)
        }
    }

    override fun showKnowledgeData(knowledgeTreeData: List<KnowledgeTreeData>) {
        mAdapter.run {
            replaceData(knowledgeTreeData)
        }
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(_mActivity,msg!!)
    }

    private fun refreshData(){
        knowledge_refresh.run {
            setOnRefreshListener {
                setRefreshThemeColor(knowledge_refresh)
                mPresenter.refreshKnowledgeSystem()
                finishRefresh(1000)
            }
        }
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        if(knowledgeTreeData.size>0){
            val data = knowledgeTreeData[position]
            startActivity<KnowledgeListActivity>(
                    Pair(Constants.CONTENT_TITLE_KEY,data.name),
                    Pair(Constants.CONTENT_DATA_KEY,data)
            )
        }
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        if(mDialog.isShowing){
            mDialog.dismiss()
        }
    }

    override fun lazyLoad() {

    }
}