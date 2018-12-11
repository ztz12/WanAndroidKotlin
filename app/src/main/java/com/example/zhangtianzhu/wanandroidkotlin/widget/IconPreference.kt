package com.example.zhangtianzhu.wanandroidkotlin.widget

import android.content.Context
import android.preference.Preference
import android.util.AttributeSet
import android.view.View
import com.afollestad.materialdialogs.color.CircleView
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils

class IconPreference(context: Context,attributeSet: AttributeSet) :Preference(context,attributeSet){
    private var circleView : CircleView? = null
    init {
        widgetLayoutResource = R.layout.item_icon_preference_preview
    }

    override fun onBindView(view: View?) {
        super.onBindView(view)
        val color = ConfigureUtils.getColor()
        circleView = view?.findViewById(R.id.iv_preview)
        circleView!!.setBackgroundColor(color)
    }

    fun setView(){
        val color = ConfigureUtils.getColor()
        circleView!!.setBackgroundColor(color)
    }
}