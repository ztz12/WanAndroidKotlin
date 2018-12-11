package com.example.zhangtianzhu.wanandroidkotlin.base

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import com.example.zhangtianzhu.wanandroidkotlin.utils.Preference
import me.yokeyword.fragmentation.SupportActivity
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication
import com.example.zhangtianzhu.wanandroidkotlin.utils.CommonUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.StatusBarUtil

abstract class BaseActivity : SupportActivity() {
    protected var isLogin by Preference(Constants.ISLOGIN,false)

    protected val username by Preference(Constants.USERNAME, "")

    protected var mThemeColor = ConfigureUtils.getColor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        init(savedInstanceState)
    }

    private fun init(savedInstanceState: Bundle?){
        initData()
        getData()
        onViewCreated(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        updateColor()
    }

    open fun updateColor(){
        mThemeColor = if(!ConfigureUtils.getIsNightMode()){
            ConfigureUtils.getColor()
        }else{
            resources.getColor(R.color.colorPrimary)
        }
        StatusBarUtil.setColor(this,mThemeColor,0)
        if(this.supportActionBar!=null){
            this.supportActionBar?.setBackgroundDrawable(ColorDrawable(mThemeColor))
        }
    }

    protected abstract fun getLayoutId() :Int

    protected abstract fun initData()

    protected abstract fun getData()

    protected abstract fun onViewCreated(savedInstanceState: Bundle?)

    override fun onDestroy() {
        super.onDestroy()
        CommonUtil.fixInputMethodManagerLeak(this)
        WanAndroidApplication.getRefWatcher(this)?.watch(this)
    }
}
