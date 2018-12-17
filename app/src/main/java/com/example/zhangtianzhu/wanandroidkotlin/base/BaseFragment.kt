package com.example.zhangtianzhu.wanandroidkotlin.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import me.yokeyword.fragmentation.SupportFragment
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication
import com.example.zhangtianzhu.wanandroidkotlin.utils.Preference
import com.scwang.smartrefresh.layout.SmartRefreshLayout

abstract class BaseFragment : SupportFragment(){
    private var clickTime :Long = 0

    private var isHasLoad = false
    private var isPrepared = false

    private var themeCount = 0

    protected val mDialog by lazy { DialogUtil.getWaitDialog(_mActivity,getString(R.string.loading)) }

    protected var isLogin by Preference(Constants.ISLOGIN,false)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(getLayoutId(),container,false)
        return view
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser){
            lazyIfPrepare()
        }
    }

    override fun onBackPressedSupport(): Boolean {
        if(childFragmentManager.backStackEntryCount>1){
            popChild()
        }else {
            val currentTime = System.currentTimeMillis()
            /**
            a + b	a.plus(b)
            a - b	a.minus(b)
            a * b	a.times(b)
            a / b	a.div(b)
            a % b	a.rem(b)、 a.mod(b) （已弃用）
            a..b	a.rangeTo(b)
             */
            if (currentTime.minus(clickTime)>Constants.DELAY_TIME) {
                DialogUtil.showSnackBar(_mActivity, getString(R.string.double_click_exit_tint))
                clickTime = System.currentTimeMillis()
            }else{
                _mActivity.finish()
            }
        }
        return true
    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        init()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        init()
//        isPrepared = true
//        lazyIfPrepare()
    }

    private fun init(){
        initView()
        initData()
        getData()
    }

    private fun lazyIfPrepare(){
        if(isPrepared&&userVisibleHint&&!isHasLoad){
            lazyLoad()
            isHasLoad = true
        }
    }

    /**
     * change refresh color
     */
    protected fun setRefreshThemeColor(refreshLayout: SmartRefreshLayout) {
        themeCount++
        when {
            themeCount % Constants.FOUR === Constants.ONE ->
                refreshLayout.setPrimaryColorsId(Constants.BLUE_THEME, R.color.white)
            themeCount % Constants.FOUR === Constants.TWO ->
                refreshLayout.setPrimaryColorsId(Constants.GREEN_THEME, R.color.white)
            themeCount % Constants.FOUR === Constants.THREE ->
                refreshLayout.setPrimaryColorsId(Constants.RED_THEME, R.color.white)
            themeCount % Constants.FOUR === Constants.ZERO ->
                refreshLayout.setPrimaryColorsId(Constants.ORANGE_THEME, R.color.white)
        }
    }

    protected abstract fun getLayoutId() :Int

    protected abstract fun initView()

    protected abstract fun getData()

    protected abstract fun initData()

    protected abstract fun lazyLoad()

    override fun onDestroy() {
        super.onDestroy()
        _mActivity.let { WanAndroidApplication.getRefWatcher(it)?.watch(it) }
    }

}