package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseMvpSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.UseWebsiteBean
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.UseWebContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.UseWebPresenter
import com.example.zhangtianzhu.wanandroidkotlin.utils.CommonUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.DisplayManager
import com.example.zhangtianzhu.wanandroidkotlin.utils.StatusBarUtil
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_use_web.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.textColor

class UseWebActivity : BaseMvpSwipeBackActivity<UseWebContract.View,UseWebContract.Presenter>(), UseWebContract.View {
    override fun createPresenter(): UseWebContract.Presenter = UseWebPresenter()

    private lateinit var mUseWeb: MutableList<UseWebsiteBean>

    override fun getLayoutId(): Int {
        return R.layout.activity_use_web
    }

    override fun initData() {
        toolbar.run {
            title = getString(R.string.useful_sites)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        StatusBarUtil.setStatusColor(window, ContextCompat.getColor(this, R.color.main_status_bar_blue), 1.0f)
        toolbar.setNavigationOnClickListener { onBackPressedSupport() }
    }

    override fun getData() {
        mPresenter?.getUseWebData()

        use_tag_flow.setOnTagClickListener { view, position, parent ->
            if (mUseWeb.size > 0) {
                val useWebsiteBean = mUseWeb[position]
                startActivity<ContentActivity>(
                        Pair(Constants.CONTENT_TITLE_KEY, useWebsiteBean.name),
                        Pair(Constants.CONTENT_URL_KEY, useWebsiteBean.link),
                        Pair(Constants.CONTENT_ID_KEY, useWebsiteBean.id)
                )
                true
            }
            false
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    override fun showUseWebData(useWebsiteBean: MutableList<UseWebsiteBean>) {
        mUseWeb = mutableListOf()
        mUseWeb.addAll(useWebsiteBean)
        use_tag_flow.adapter = object : TagAdapter<UseWebsiteBean>(useWebsiteBean) {
            override fun getView(parent: FlowLayout?, position: Int, t: UseWebsiteBean?): View {
                val tv = LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout_tv, use_tag_flow, false) as TextView
                val padding = DisplayManager.dip2px(10f)!!
                tv.setPadding(padding, padding, padding, padding)
                tv.textColor = CommonUtil.randomColor()
                tv.text = t?.name
                return tv
            }
        }
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(this, msg!!)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

}
