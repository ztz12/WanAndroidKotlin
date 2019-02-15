package com.example.zhangtianzhu.wanandroidkotlin.base

import android.app.Activity
import android.content.IntentFilter
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import me.yokeyword.fragmentation.SupportActivity
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.NetWorkChangeEvent
import com.example.zhangtianzhu.wanandroidkotlin.utils.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

abstract class BaseActivity : SupportActivity() {
    protected var isLogin by Preference(Constants.ISLOGIN, false)

    protected var isFirstIn by Preference(Constants.ISFIRSTIN, true)

    protected val username by Preference(Constants.USERNAME, "")

    protected var mThemeColor = ConfigureUtils.getColor()

    private var activityWeakReference: WeakReference<Activity>? = null

    private var haveNetWork :Boolean by Preference(Constants.HAVE_NETWORK,true)

    private var mNetworkChangeReceiver : NetWorkReceiver? = null

    open fun doRetryConnectNetWork(){
        initData()
        getData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        EventBus.getDefault().register(this)
        activityWeakReference = WeakReference(this)
        ActivityCollector.add(activityWeakReference)
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?) {
        onViewCreated(savedInstanceState)
        initView()
        initData()
        getData()
    }

    override fun onResume() {
        //动态注册广播变化
        val filter = IntentFilter()
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        mNetworkChangeReceiver = NetWorkReceiver()
        registerReceiver(mNetworkChangeReceiver,filter)
        super.onResume()
        updateColor()
    }

    override fun onPause() {
        if(mNetworkChangeReceiver!=null){
            unregisterReceiver(mNetworkChangeReceiver)
            mNetworkChangeReceiver = null
        }
        super.onPause()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onNetWorkChange(netWorkChangeEvent: NetWorkChangeEvent){
        haveNetWork = netWorkChangeEvent.isConnect
        if(netWorkChangeEvent.isConnect){
            doRetryConnectNetWork()
        }
    }

    open fun updateColor() {
        mThemeColor = if (!ConfigureUtils.getIsNightMode()) {
            ConfigureUtils.getColor()
        } else {
            resources.getColor(R.color.colorPrimary)
        }
        StatusBarUtil.setColor(this, mThemeColor, 0)
        if (this.supportActionBar != null) {
            this.supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
        }
    }

    protected abstract fun getLayoutId(): Int

    protected abstract fun initData()

    protected abstract fun initView()

    protected abstract fun getData()

    protected abstract fun onViewCreated(savedInstanceState: Bundle?)

    override fun onDestroy() {
        super.onDestroy()
        CommonUtil.fixInputMethodManagerLeak(this)
        ActivityCollector.remove(activityWeakReference)
        EventBus.getDefault().unregister(this)
//        WanAndroidApplication.getRefWatcher(this)?.watch(this)
    }
}
