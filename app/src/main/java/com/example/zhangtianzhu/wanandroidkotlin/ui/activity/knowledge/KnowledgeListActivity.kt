package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.knowledge

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.knowledge.KnowledgePageAdapter
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseMvpSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.KnowledgeData
import com.example.zhangtianzhu.wanandroidkotlin.constant.KnowledgeTreeData
import com.example.zhangtianzhu.wanandroidkotlin.contract.knowledge.KnowledgeListContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.knowledge.KnowledgeListPresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.knowledge.KnowledgeDetailFragment
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_knowledge_list.*


class KnowledgeListActivity : BaseMvpSwipeBackActivity<KnowledgeListContract.View,KnowledgeListContract.Presenter>() ,KnowledgeListContract.View{

    override fun createPresenter(): KnowledgeListContract.Presenter = KnowledgeListPresenter()

    private var knowledgeDetailData = mutableListOf<KnowledgeData>()

    private var toolbarTitle: String? = ""

    private val mViewPageAdapter: KnowledgePageAdapter by lazy { KnowledgePageAdapter(knowledgeDetailData, supportFragmentManager) }

    override fun getLayoutId(): Int {
        return R.layout.activity_knowledge_list
    }

    override fun initData() {
        intent?.extras.let {
            toolbarTitle = it?.getString(Constants.CONTENT_TITLE_KEY)
            it?.getSerializable(Constants.CONTENT_DATA_KEY)?.let {
                val data = it as KnowledgeTreeData
                data.children.let { children ->
                    knowledgeDetailData.addAll(children)
                }
            }
        }
    }

    override fun getData() {
        toolbar.run {
            title = toolbarTitle
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        StatusBarUtil.setStatusColor(window, ContextCompat.getColor(this, R.color.main_status_bar_blue), 1.0f)
        toolbar.setNavigationOnClickListener { onBackPressedSupport() }

        vp_knowledge.run {
            adapter = mViewPageAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_knowledge))
            offscreenPageLimit = knowledgeDetailData.size
        }

        tab_knowledge.run {
            setupWithViewPager(vp_knowledge)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(vp_knowledge))
            addOnTabSelectedListener(onTabSelectListener)
        }

        fab_knowledge.setOnClickListener(fabOnItemClickListener)
        mPresenter?.registerEvent()
    }

    private val fabOnItemClickListener = View.OnClickListener {
        if (mViewPageAdapter.count == 0) {
            return@OnClickListener
        }

        val fragment: KnowledgeDetailFragment = mViewPageAdapter.getItem(vp_knowledge.currentItem) as KnowledgeDetailFragment
        fragment.scrollTop()
    }

    private val onTabSelectListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(p0: TabLayout.Tab?) {

        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            //取消viewPage切换动画
            p0?.let {
                vp_knowledge.setCurrentItem(it.position, false)
            }
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {
        }
    }

    override fun updateColor() {
        super.updateColor()
        changeColor()
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_share_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,
                            getString(
                                    R.string.share_type_url,
                                    getString(R.string.app_name),
                                    knowledgeDetailData[tab_knowledge.selectedTabPosition].name,
                                    knowledgeDetailData[tab_knowledge.selectedTabPosition].id.toString()
                            ))
                    type = Constants.CONTENT_SHARE_TYPE
                    startActivity(Intent.createChooser(this, getString(R.string.share)))
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //解决点击后退键弹出退出整个APP的提示
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun changeColor() {
        if(!ConfigureUtils.getIsNightMode()){
            tab_knowledge.setBackgroundColor(ConfigureUtils.getColor())
            fab_knowledge.backgroundTintList = ColorStateList.valueOf(mThemeColor)
        }
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(this,msg!!)
    }

    override fun showLoading() {

    }

    override fun hideLoading() {
    }
}
