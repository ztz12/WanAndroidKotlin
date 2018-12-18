package com.example.zhangtianzhu.wanandroidkotlin.app

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import com.example.zhangtianzhu.wanandroidkotlin.BuildConfig
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.utils.CommonUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import com.example.zhangtianzhu.wanandroidkotlin.utils.DisplayManager
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.squareup.leakcanary.LeakCanary
import com.squareup.leakcanary.RefWatcher
import org.litepal.LitePal
import org.litepal.LitePalApplication
import kotlin.properties.Delegates
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import android.os.Process.THREAD_PRIORITY_BACKGROUND




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
        DisplayManager.init(this)
        registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks)
        setNightModel()
        initThirdService()
    }

    private fun initThirdService(){
        Thread(Runnable {
            //子线程初始化第三方组件
            Thread.sleep(1000)//延迟初始化
            refWatcher = setupLeakCanary()
            initLitePal()
            initLogger()
            initBugly()
        }).start()

    }

    /**
     * 初始化Logger日志打印
     */
    private fun initLogger(){
        val formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // 是否显示线程信息，默认 显示
                .methodCount(0)         // 方法栈打印的个数，默认是 2
                .methodOffset(7)        // 设置调用堆栈的函数偏移值，默认是 5
                .tag("Yif_TAG")   // 设置全局TAG，默认是 PRETTY_LOGGER
                .build()
        Logger.addLogAdapter(object :AndroidLogAdapter(formatStrategy){
            override fun isLoggable(priority: Int, tag: String?): Boolean {
                return BuildConfig.DEBUG
            }
        })
    }

    /**
     * 配置Bugly
     */
    private fun initBugly(){
        // 获取当前包名
        val packageName = context.packageName
        // 获取当前进程名
        val processName = CommonUtil.getProcessName(android.os.Process.myPid())
        // 设置是否为上报进程
        val strategy = UserStrategy(context)
        strategy.isUploadProcess = false || processName == packageName
        // 初始化Bugly
        CrashReport.initCrashReport(context, Constants.BUGLY_ID, BuildConfig.DEBUG, strategy)
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

    /**
     * ActivityLifecycleCallbacks 监听Activity 生命周期回调
     */
    private val mActivityLifecycleCallbacks = object : ActivityLifecycleCallbacks{
        override fun onActivityPaused(p0: Activity?) {
        }

        override fun onActivityResumed(p0: Activity?) {
        }

        override fun onActivityStarted(p0: Activity?) {
        }

        override fun onActivityDestroyed(p0: Activity?) {
            Logger.d("onDestroy",p0?.componentName?.className)
        }

        override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
        }

        override fun onActivityStopped(p0: Activity?) {
        }

        override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
            Logger.d("onCreate",p0?.componentName?.className)
        }

    }

}
