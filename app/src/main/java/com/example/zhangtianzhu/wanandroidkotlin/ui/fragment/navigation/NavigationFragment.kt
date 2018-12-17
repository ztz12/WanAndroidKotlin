package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.navigation

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.navigation.NavigationAdapter
import com.example.zhangtianzhu.wanandroidkotlin.adapter.navigation.NavigationTabAdapter
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseMvpFragment
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.NavigationData
import com.example.zhangtianzhu.wanandroidkotlin.contract.navigation.NavigationContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.navigation.NavigationPresenter
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_navigation.*
import q.rorbin.verticaltablayout.VerticalTabLayout
import q.rorbin.verticaltablayout.widget.TabView


class NavigationFragment :BaseMvpFragment<NavigationContract.View,NavigationContract.Presenter>(),NavigationContract.View{

    override fun createPresenter(): NavigationContract.Presenter = NavigationPresenter()

    private val mData = mutableListOf<NavigationData>()

    private val mAdapter : NavigationAdapter by lazy { NavigationAdapter(_mActivity,mData) }

    private val linearLayoutManager : LinearLayoutManager by lazy { LinearLayoutManager(_mActivity) }

    private var needScroll  = false
    private var isTagClick  = false
    private var mCurrentIndex = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_navigation
    }

    override fun getData() {
        rl_navigation.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
            setHasFixedSize(true)
        }
        mAdapter.run {
            bindToRecyclerView(rl_navigation)
        }
        mPresenter?.getNavigationData()
        leftRightLink()
    }

    override fun initData() {
    }

    /**
     * Left TabLayout and right RecyclerView Link
     */
    private fun leftRightLink(){
        rl_navigation.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if(needScroll&&newState ==RecyclerView.SCROLL_STATE_IDLE){
                    scrollRecyclerView()
                }
                rightLinkLeft(newState)
            }
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if(needScroll){
                    scrollRecyclerView()
                }
            }
        })

        navigation_vertical_tab.addOnTabSelectedListener(object :VerticalTabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabView?, position: Int) {
                isTagClick = true
                selectTab(position)
            }

            override fun onTabReselected(tab: TabView?, position: Int) {

            }
        })
    }

    private fun scrollRecyclerView(){
        needScroll = false
        val indexDistance:Int = mCurrentIndex - linearLayoutManager.findFirstVisibleItemPosition()
        if(indexDistance>0 && indexDistance<rl_navigation.childCount){
            val top = rl_navigation.getChildAt(indexDistance).top
            rl_navigation.smoothScrollBy(0,top)
        }
    }

    /**
     * Right RecyclerView Link Left TabLayout
     */
    private fun rightLinkLeft(newState:Int){
        if(newState == RecyclerView.SCROLL_STATE_IDLE){
            if(isTagClick){
                isTagClick = false
                return
            }
            val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
            if(firstPosition!=mCurrentIndex){
                mCurrentIndex = firstPosition
                setChecked(mCurrentIndex)
            }
        }
    }

    /**
     * Smooth Right RecyclerView to Select Left TabLayout
     */
    private fun setChecked(position:Int){
        if(isTagClick){
            isTagClick = false
            return
        }else{
            navigation_vertical_tab.setTabSelected(mCurrentIndex)
        }
        mCurrentIndex = position
    }

    /**
     * Select Left TabLayout to Smooth Right RecyclerView
     */
    private fun selectTab(position:Int){
        mCurrentIndex = position
        rl_navigation.stopScroll()
        smoothScrollToPosition(position)
    }

    private fun smoothScrollToPosition(position:Int){
        val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val lastPosition = linearLayoutManager.findLastVisibleItemPosition()
        when{
            position < firstPosition -> {
                rl_navigation.smoothScrollToPosition(position)
            }
            position <= lastPosition ->{
                val top = rl_navigation.getChildAt(position-firstPosition).top
                rl_navigation.smoothScrollBy(0,top)
            }
            else ->{
                rl_navigation.smoothScrollToPosition(position)
                needScroll = true
            }
        }
    }
    companion object {
        fun getInstance(param1:String,param2:String): NavigationFragment {
            val fragment = NavigationFragment()
            val bundle = Bundle()
            bundle.putString(Constants.PARAM1,param1)
            bundle.putString(Constants.PARAM2,param2)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun scrollTop() {
        navigation_vertical_tab.setTabSelected(0)
    }

    override fun showNavaigationData(navigationData: MutableList<NavigationData>) {
        navigationData.let {
            navigation_vertical_tab.run {
                setTabAdapter(NavigationTabAdapter(_mActivity,navigationData))
            }
            mAdapter.run {
                replaceData(it)
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

    override fun lazyLoad() {

    }
}