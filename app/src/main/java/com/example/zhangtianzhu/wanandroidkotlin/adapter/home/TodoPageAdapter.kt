package com.example.zhangtianzhu.wanandroidkotlin.adapter.home

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.text.Html
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoTypeBean
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.home.TodoFragment

class TodoPageAdapter(val list: MutableList<TodoTypeBean>,fragmentManager: FragmentManager):FragmentStatePagerAdapter(fragmentManager) {

    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        list.forEach {
            fragments.add(TodoFragment.getInstance(it.type))
        }
    }
    override fun getItem(p0: Int): Fragment = fragments[p0]

    override fun getCount(): Int = list.size

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return list[position].name
    }
}