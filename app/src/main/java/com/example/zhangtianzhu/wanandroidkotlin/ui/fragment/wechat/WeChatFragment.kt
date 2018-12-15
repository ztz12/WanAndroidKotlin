package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.wechat

import android.os.Bundle
import android.support.design.widget.TabLayout
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.wechat.WeChatAdapter
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.WeChatData
import com.example.zhangtianzhu.wanandroidkotlin.contract.wechat.WeChatContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.wechat.WeChatPresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.knowledge.KnowledgeDetailFragment
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_wechat.*

class WeChatFragment : BaseFragment(),WeChatContract.View{

    private val mPresenter :WeChatPresenter by lazy { WeChatPresenter() }

    private val mData = mutableListOf<WeChatData>()

    private val mAdapter:WeChatAdapter by lazy { WeChatAdapter(mData,childFragmentManager) }

    companion object {
        fun getInstance(param1:String,param2:String): WeChatFragment {
            val fragment = WeChatFragment()
            val bundle = Bundle()
            bundle.putString(Constants.PARAM1,param1)
            bundle.putString(Constants.PARAM2,param2)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_wechat
    }

    override fun getData() {
        vp_weChat.run {
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(weChat_tabLayout))
        }

        weChat_tabLayout.run {
            setupWithViewPager(vp_weChat)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(vp_weChat))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        mPresenter.getWeChatData()
        mPresenter.registerEvent()
    }

    private val onTabSelectedListener = object :TabLayout.OnTabSelectedListener{
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            p0?.let {
                vp_weChat.setCurrentItem(p0?.position,false)
            }
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }
    }

    override fun initData() {
        mPresenter.attachView(this)
    }

    override fun lazyLoad() {
    }

    override fun scrollTop() {
        if(mAdapter.count==0){
            return
        }

        val fragment = mAdapter.getItem(vp_weChat.currentItem) as KnowledgeDetailFragment
        fragment.scrollTop()
    }

    override fun showWebChatData(weChatData: MutableList<WeChatData>) {
        weChatData.let {
            mData.addAll(it)
            vp_weChat.run {
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
            weChat_tabLayout.setBackgroundColor(ConfigureUtils.getColor())
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
}