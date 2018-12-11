package com.example.zhangtianzhu.wanandroidkotlin.utils

import android.graphics.Color
import android.preference.PreferenceManager
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication

object ConfigureUtils {
    private val configure = PreferenceManager.getDefaultSharedPreferences(WanAndroidApplication.context)

    /**
     * 获取是否开启显示首页置顶文章，true: 不显示  false: 显示
     */
    fun getIsShowTopArticle(): Boolean {
        return configure.getBoolean("setting_show_top", false)
    }

    /**
     * 获取是否开启无图模式 false 不开启
     */
    fun getIsNoPhotoMode(): Boolean {
        return configure.getBoolean("setting_NoPhoto", false)
    }

    fun setIsNoPhotoModel(flag:Boolean){
        configure.edit().putBoolean("setting_NoPhoto",flag).apply()
    }


    /**
     * 获取是否开启夜间模式
     */
    fun getIsNightMode(): Boolean {
        return configure.getBoolean("setting_night", false)
    }

    /**
     * 设置夜间模式
     */
    fun setIsNightMode(flag: Boolean) {
        configure.edit().putBoolean("setting_night", flag).apply()
    }
    /**
     * 获取主题颜色
     */
    fun getColor(): Int {
        val defaultColor = WanAndroidApplication.context.resources.getColor(R.color.colorPrimary)
        val color = configure.getInt("color", defaultColor)
        return if (color != 0 && Color.alpha(color) != 255) {
            defaultColor
        } else color
    }

    /**
     * 设置主题颜色
     */
    fun setColor(color: Int) {
        configure.edit().putInt("color", color).apply()
    }
}