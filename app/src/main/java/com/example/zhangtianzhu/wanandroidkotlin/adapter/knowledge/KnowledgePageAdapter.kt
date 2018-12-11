package com.example.zhangtianzhu.wanandroidkotlin.adapter.knowledge

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.text.Html
import com.example.zhangtianzhu.wanandroidkotlin.constant.KnowledgeData
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.knowledge.KnowledgeDetailFragment

class KnowledgePageAdapter(val list: MutableList<KnowledgeData>,fragmentManager: FragmentManager):FragmentStatePagerAdapter(fragmentManager) {
    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.clear()
        list.forEach {
            fragments.add(KnowledgeDetailFragment.getInstance(it.id))
        }
    }

    override fun getItem(p0: Int): Fragment {
        return fragments[p0]
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return Html.fromHtml(list[position].name)
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
}