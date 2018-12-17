package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.project

import android.os.Bundle
import android.support.design.widget.TabLayout
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.project.ProjectPageAdapter
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseMvpFragment
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.ProjectTreeData
import com.example.zhangtianzhu.wanandroidkotlin.contract.project.ProjectContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.project.ProjectPresenter
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_project.*


class ProjectFragment :BaseMvpFragment<ProjectContract.View,ProjectContract.Presenter>(),ProjectContract.View {

    override fun createPresenter(): ProjectContract.Presenter = ProjectPresenter()

    private val mData = mutableListOf<ProjectTreeData>()

    private val mAdapter : ProjectPageAdapter by lazy { ProjectPageAdapter(mData,childFragmentManager) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_project
    }

    override fun getData() {
        vp_project.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(project_tabLayout))
        }

        project_tabLayout.run {
            setupWithViewPager(vp_project)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(vp_project))
            addOnTabSelectedListener(onTabSelectedListener)
        }
        mPresenter?.getProjectData()
        mPresenter?.registerEvent()
    }

    override fun initData() {
    }

    companion object {
        fun getInstance(param1:String,param2:String): ProjectFragment {
            val fragment = ProjectFragment()
            val bundle = Bundle()
            bundle.putString(Constants.PARAM1,param1)
            bundle.putString(Constants.PARAM2,param2)
            fragment.arguments = bundle
            return fragment
        }
    }
    /**
     * onTabSelectedListener
     */
    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            // 默认切换的时候，会有一个过渡动画，设为false后，取消动画，直接显示
            tab?.let {
                vp_project.setCurrentItem(it.position,false)
            }
        }
    }
    override fun scrollTop() {
        if(mAdapter.count==0){
            return
        }
        val fragment:ProjectListFragment = mAdapter.getItem(vp_project.currentItem) as ProjectListFragment
        fragment.scrollTop()
    }

    fun changeProjectData(){
        mPresenter?.getProjectData()
    }

    override fun showProjectData(projectTreeData: MutableList<ProjectTreeData>) {
        projectTreeData.let {
            mData.addAll(projectTreeData)
            vp_project.run {
                adapter = mAdapter
                offscreenPageLimit = mData.size
            }
        }
    }

    override fun onResume() {
        super.onResume()
        changeColor()
    }

    override fun changeColor() {
        if(!ConfigureUtils.getIsNightMode()){
            project_tabLayout.setBackgroundColor(ConfigureUtils.getColor())
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