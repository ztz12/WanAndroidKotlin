package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.project

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.project.ProjectListAdapter
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticelDetail
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.contract.project.ProjectListContract
import com.example.zhangtianzhu.wanandroidkotlin.http.NetWorkUtils
import com.example.zhangtianzhu.wanandroidkotlin.presenter.project.ProjectListPresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.ContentActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_project_list.*


class ProjectListFragment: BaseFragment(), ProjectListContract.View{

    private var cid:Int = 0

    private val mPresenter : ProjectListPresenter by lazy { ProjectListPresenter() }

    private val mData = mutableListOf<ArticelDetail>()

    private val mAdapter :ProjectListAdapter by lazy { ProjectListAdapter(_mActivity,mData) }

    private val linearLayoutManager :LinearLayoutManager by lazy { LinearLayoutManager(_mActivity) }

    companion object {
        fun getInstance(cid:Int):ProjectListFragment {
            val fragment = ProjectListFragment()
            val bundle = Bundle()
            bundle.putInt(Constants.CONTENT_CID_KEY,cid)
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_project_list
    }

    override fun getData() {
        rl_projectList.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }

        mAdapter.run {
            bindToRecyclerView(rl_projectList)
            setEmptyView(R.layout.fragment_empty_layout)
            onItemClickListener = this@ProjectListFragment.onItemClickListener
            onItemChildClickListener = this@ProjectListFragment.onItemChildClickListener
        }
        mPresenter.getProjectList(0,cid)

        refreshData()
    }

    override fun initData() {
        mPresenter.attachView(this)
        cid = arguments?.getInt(Constants.CONTENT_CID_KEY)!!
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        if(mData.size>0){
            val data = mData[position]
            Intent(_mActivity, ContentActivity::class.java).run {
                putExtra(Constants.CONTENT_URL_KEY,data.link)
                putExtra(Constants.CONTENT_TITLE_KEY,data.title)
                putExtra(Constants.CONTENT_ID_KEY,data.id)
                startActivity(this)
            }
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        if(mData.size>0){
            val data = mData[position]
            when(view.id){
                R.id.item_project_list_like_iv ->
                    if(isLogin){
                        if(!NetWorkUtils.isNetWorkAvailable(WanAndroidApplication.context)){
                            DialogUtil.showSnackBar(_mActivity,getString(R.string.http_error))
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
                        DialogUtil.showSnackBar(_mActivity,getString(R.string.login_tint))
                    }
            }
        }
    }

    override fun lazyLoad() {
    }

    override fun scrollTop() {
        if(linearLayoutManager.findFirstVisibleItemPosition()>20){
            rl_projectList.scrollToPosition(0)
        }else{
            rl_projectList.smoothScrollToPosition(0)
        }
    }

    override fun showProjectList(articleData: ArticleData, isRefresh: Boolean) {
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
        project_refresh.setOnRefreshListener{
            setRefreshThemeColor(project_refresh)
            mPresenter.refreshData(cid)
            project_refresh.finishRefresh(1000)
        }
        project_refresh.setOnLoadMoreListener {
            mPresenter.loadMore(cid)
            project_refresh.finishLoadMore(1000)
        }
    }
}