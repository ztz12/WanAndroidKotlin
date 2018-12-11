package com.example.zhangtianzhu.wanandroidkotlin.adapter.navigation

import android.content.Context
import android.support.v4.content.ContextCompat
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.constant.NavigationData
import q.rorbin.verticaltablayout.adapter.TabAdapter
import q.rorbin.verticaltablayout.widget.ITabView

class NavigationTabAdapter(context: Context,navigationData: MutableList<NavigationData>):TabAdapter {
    private val context:Context =context
    private var navigationData = mutableListOf<NavigationData>()

    init {
        this.navigationData = navigationData
    }
    override fun getIcon(position: Int): ITabView.TabIcon? = null

    override fun getBadge(position: Int): ITabView.TabBadge? = null

    override fun getBackground(position: Int): Int = -1
    override fun getTitle(position: Int): ITabView.TabTitle {
        return ITabView.TabTitle.Builder()
                .setContent(navigationData[position].name)
                .setTextColor(ContextCompat.getColor(context, R.color.colorPrimary),
                        ContextCompat.getColor(context,R.color.Grey400))
                .build()
    }

    override fun getCount(): Int = navigationData.size
}