package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.home.SearchAdapter
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.HotSearchBean
import com.example.zhangtianzhu.wanandroidkotlin.constant.SearchHistoryBean
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.SearchContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.SearchPresenter
import com.example.zhangtianzhu.wanandroidkotlin.utils.CommonUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.DisplayManager
import com.example.zhangtianzhu.wanandroidkotlin.utils.StatusBarUtil
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.toolbar.*
import org.jetbrains.anko.startActivity

class SearchActivity : BaseSwipeBackActivity(),SearchContract.View {

    private val mPresenter : SearchPresenter by lazy { SearchPresenter() }

    private val mHotData = mutableListOf<HotSearchBean>()

    private val mData = mutableListOf<SearchHistoryBean>()

    private val mAdapter : SearchAdapter by lazy { SearchAdapter(this,mData) }

    private val linearLayoutManager : LinearLayoutManager by lazy { LinearLayoutManager(this) }

    override fun getLayoutId(): Int {
        return R.layout.activity_search
    }

    override fun initData() {
        mPresenter.attachView(this)
        toolbar.run {
            title = getString(R.string.action_search)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        StatusBarUtil.setStatusColor(window, ContextCompat.getColor(this,R.color.main_status_bar_blue),1.0f)
        toolbar.setNavigationOnClickListener{onBackPressedSupport()}
    }

    override fun getData() {
        rl_search.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }

        mAdapter.run {
            bindToRecyclerView(rl_search)
            setEmptyView(R.layout.fragment_empty_layout)
            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
        }

        hot_search_flow.run {
            setOnTagClickListener { view, position, parent ->
                if(mHotData.size>0){
                    val hotSearchBean = mHotData[position]
                    getSearchKey(hotSearchBean.name)
                    true
                }
                false
            }
        }

        search_history_clear_all_tv.setOnClickListener {
            mData.clear()
            mAdapter.replaceData(mData)
            mPresenter.clearAllHistoryData()
        }
        mPresenter.getHotSearchData()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.queryHistory()
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    override fun showHotSearchData(hotSearchList: MutableList<HotSearchBean>) {
        mHotData.addAll(hotSearchList)
        hot_search_flow.adapter = object : TagAdapter<HotSearchBean>(hotSearchList){
            override fun getView(parent: FlowLayout?, position: Int, t: HotSearchBean?): View {
                val tv:TextView = LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout_tv,
                        hot_search_flow,false) as TextView
                val padding :Int = DisplayManager.dip2px(10F)!!
                tv.setPadding(padding,padding,padding,padding)
                tv.text = t?.name
                tv.setTextColor(CommonUtil.randomColor())
                return tv
            }
        }
    }

    override fun showHistoryData(historyBeanList: MutableList<SearchHistoryBean>) {
        mAdapter.replaceData(historyBeanList)
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(this,msg!!)
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search,menu)
        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchView.maxWidth = Int.MAX_VALUE
        searchView.onActionViewExpanded()
        searchView.queryHint = getString(R.string.search_tint)
        searchView.setOnQueryTextListener(onQueryTextListener)
        searchView.isSubmitButtonEnabled = true
        try {
            val field = searchView.javaClass.getDeclaredField("mGoButton")
            field.isAccessible = true
            val mButtonView = field.get(searchView) as ImageView
            mButtonView.setImageResource(R.drawable.ic_search_white_24dp)
        }catch (e:Exception){
            e.printStackTrace()
        }
        return super.onCreateOptionsMenu(menu)
    }

    private val onQueryTextListener = object : SearchView.OnQueryTextListener{
        override fun onQueryTextChange(p0: String?): Boolean {
            return false
        }

        override fun onQueryTextSubmit(p0: String?): Boolean {
            getSearchKey(p0.toString())
            return false
        }
    }

    private fun getSearchKey(key:String){
        mPresenter.saveSearchKey(key)
        startActivity<SearchDataActivity>(
                Pair(Constants.SEARCH_KEY,key)
        )
    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        if(mData.size>0){
            val item = mData[position]
            getSearchKey(item.key)
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        if(mData.size>0){
            val data = mData[position]
            when(view.id){
                R.id.iv_clear ->{
                    mPresenter.deleteId(data.id)
                    mAdapter.remove(position)
                }
            }
        }
    }
}
