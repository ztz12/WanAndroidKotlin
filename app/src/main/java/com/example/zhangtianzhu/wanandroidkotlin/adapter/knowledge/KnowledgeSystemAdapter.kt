package com.example.zhangtianzhu.wanandroidkotlin.adapter.knowledge

import android.content.Context
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.zhangtianzhu.wanandroidkotlin.constant.KnowledgeTreeData
import com.example.zhangtianzhu.wanandroidkotlin.R

class KnowledgeSystemAdapter(private val context: Context,data:MutableList<KnowledgeTreeData>)
    :BaseQuickAdapter<KnowledgeTreeData,BaseViewHolder>(R.layout.item_knowledge_system,data){
    override fun convert(helper: BaseViewHolder?, item: KnowledgeTreeData?) {
        helper ?: return
        item ?: return

        helper?.setText(R.id.first_title,item.name)

        item?.children.let {
            helper?.setText(R.id.second_title,
                    // joinToString() - 简单处理字符串拼接方法
                    // 看joinToString的源代码如下，已经默认自定义好分隔符，前缀，后缀等属性，可以直接拿来修改使用
                    //用"  "隔开字符串
                    it?.joinToString("  ",transform = {child->
                        Html.fromHtml(child.name)
                    }))
        }
    }
}