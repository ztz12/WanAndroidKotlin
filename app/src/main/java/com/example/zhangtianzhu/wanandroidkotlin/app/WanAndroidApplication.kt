package com.example.zhangtianzhu.wanandroidkotlin.app

import android.content.Context
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import com.example.zhangtianzhu.wanandroidkotlin.utils.DisplayManager
import org.litepal.LitePal
import org.litepal.LitePalApplication
import kotlin.properties.Delegates

class WanAndroidApplication :LitePalApplication(){

    companion object {
        var context: Context by Delegates.notNull()
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        DisplayManager.init(this)
        initLitePal()
        setNightModel()
    }

    private fun initLitePal(){
        LitePal.initialize(this)
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
