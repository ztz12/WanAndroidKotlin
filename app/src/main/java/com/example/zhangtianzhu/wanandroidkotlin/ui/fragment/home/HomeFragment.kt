package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.ImageView
import cn.bingoogolapple.bgabanner.BGABanner
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.home.HomeAdapter
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseMvpFragment
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticelDetail
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticleData
import com.example.zhangtianzhu.wanandroidkotlin.constant.BannerData
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.HomeContract
import com.example.zhangtianzhu.wanandroidkotlin.http.NetWorkUtils
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.HomePresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.ContentActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.ImageLoader
import com.example.zhangtianzhu.wanandroidkotlin.utils.Preference
import io.reactivex.Observable
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment :BaseMvpFragment<HomeContract.View,HomeContract.Presenter>(),HomeContract.View{

    override fun createPresenter(): HomeContract.Presenter = HomePresenter()

    private lateinit var bannerData: List<BannerData>

    private var isFirstDialog = true

    private var bannerView :View? = null

    private var articleDetailData = mutableListOf<ArticelDetail>()

    private val linearManager :LinearLayoutManager by lazy { LinearLayoutManager(_mActivity) }

    private val mAdapter:HomeAdapter by lazy { HomeAdapter(_mActivity,articleDetailData) }

    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun getData() {
        home_rl.run {
            layoutManager = linearManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }
        mAdapter.run {
            bindToRecyclerView(home_rl)
            setEmptyView(R.layout.fragment_empty_layout)
            onItemClickListener = this@HomeFragment.onItemClickListener
            onItemChildClickListener = this@HomeFragment.onItemChildClickListener
            addHeaderView(bannerView)
        }
        refreshData()
    }

    override fun initData() {
        mPresenter?.getHomeData()
        bannerView = layoutInflater.inflate(R.layout.item_bannerview,null)
        bannerView?.findViewById<BGABanner>(R.id.banner)?.run {
            setDelegate(delegate)
        }
    }

    fun changeData(){
        mPresenter?.getHomeData()
    }

    override fun lazyLoad() {
    }

    companion object {
        fun getInstance(param1:String,param2:String):HomeFragment{
            val fragment =HomeFragment()
            val bundle = Bundle()
            bundle.putString(Constants.PARAM1,param1)
            bundle.putString(Constants.PARAM2,param2)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun scrollTop() {
        home_rl.run {
            if(linearManager.findFirstVisibleItemPosition()>20){
                scrollToPosition(0)
            }else{
                smoothScrollToPosition(0)
            }
        }
    }

    private val bannerAdapter:BGABanner.Adapter<ImageView,String> by lazy {
        BGABanner.Adapter<ImageView,String>{banner,ImageView,ImageUrl,position ->
            ImageLoader.load(_mActivity,ImageView,ImageUrl)
        }
    }

    override fun showBannerData(bannerData: List<BannerData>) {
        this.bannerData = bannerData
        val imagePic = ArrayList<String>()
        val imageTitle = ArrayList<String>()
        Observable.fromIterable(bannerData)
                .subscribe({
                    imagePic.add(it.imagePath)
                    imageTitle.add(it.title)
                })
        bannerView?.findViewById<BGABanner>(R.id.banner)?.run {
            setAutoPlayAble(imagePic.size>1)
            setData(imagePic,imageTitle)
            setAdapter(bannerAdapter)
        }

    }

    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
        if(articleDetailData.size>0){
            val data = articleDetailData[position]
            Intent(_mActivity,ContentActivity::class.java).run {
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
                R.id.iv_collect ->
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

    private val delegate = BGABanner.Delegate<ImageView,String>{
        banner, itemView, model, position ->
        if(bannerData.size>0){
            val data = bannerData[position]
            Intent(_mActivity,ContentActivity::class.java).run {
                putExtra(Constants.CONTENT_URL_KEY,data.url)
                putExtra(Constants.CONTENT_TITLE_KEY,data.title)
                putExtra(Constants.CONTENT_ID_KEY,data.id)
                startActivity(this)
            }
        }
    }

    override fun showArticleData(articleData: ArticleData,isRefresh:Boolean) {
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


    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(_mActivity,msg!!)
    }

    override fun showLoading() {
        if(isFirstDialog) {
            mDialog.show()
        }
    }

    override fun hideLoading() {
        if(isFirstDialog && mDialog.isShowing){
            mDialog.dismiss()
        }
        isFirstDialog = false
    }

    override fun collectSuccess(success: Boolean) {
        DialogUtil.showSnackBar(_mActivity,getString(R.string.collect_success))
    }

    override fun cancelCollectSuccess(success: Boolean) {
        DialogUtil.showSnackBar(_mActivity,getString(R.string.cancel_collect_success))
    }

    private fun refreshData(){
        home_refresh.setOnRefreshListener{
            setRefreshThemeColor(home_refresh)
            mPresenter?.autoRefresh()
            home_refresh.finishRefresh(1000)
        }
        home_refresh.setOnLoadMoreListener {
            mPresenter?.loadMore()
            home_refresh.finishLoadMore(1000)
        }
    }
}