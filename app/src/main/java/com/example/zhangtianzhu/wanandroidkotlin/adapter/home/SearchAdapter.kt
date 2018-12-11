package com.example.zhangtianzhu.wanandroidkotlin.adapter.home

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.constant.SearchHistoryBean

class SearchAdapter(private val context: Context,historyData:MutableList<SearchHistoryBean>)
    :BaseQuickAdapter<SearchHistoryBean,BaseViewHolder>(R.layout.item_search_history,historyData){

    override fun convert(helper: BaseViewHolder?, item: SearchHistoryBean?) {
        helper ?: return
        item ?: return

        helper?.setText(R.id.tv_search_key,item.key)
                .addOnClickListener(R.id.iv_clear)
    }
}