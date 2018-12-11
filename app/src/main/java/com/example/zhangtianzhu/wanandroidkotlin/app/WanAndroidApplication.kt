package com.example.zhangtianzhu.wanandroidkotlin.app

import android.content.Context
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import com.example.zhangtianzhu.wanandroidkotlin.utils.DisplayManager
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import org.litepal.LitePal
import org.litepal.LitePalApplication
import kotlin.properties.Delegates

class WanAndroidApplication :LitePalApplication(){

    private var refWatcher: RefWatcher? = null

    companion object {
        var context: Context by Delegates.notNull()

        fun getRefWatcher(context: Context): RefWatcher? {
            val app = context.applicationContext as WanAndroidApplication
            return app.refWatcher
        }
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        refWatcher = setupLeakCanary()
        DisplayManager.init(this)
        initLitePal()
        setNightModel()
    }

    private fun initLitePal(){
        LitePal.initialize(this)
    }

    private fun setupLeakCanary(): RefWatcher {
        return if (LeakCanary.isInAnalyzerProcess(this)) {
            RefWatcher.DISABLED
        } else LeakCanary.install(this)
    }

    private fun setNightModel(){
        if(ConfigureUtils.getIsNightMode()){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

}
