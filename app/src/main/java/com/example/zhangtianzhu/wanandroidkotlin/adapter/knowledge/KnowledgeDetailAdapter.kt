package com.example.zhangtianzhu.wanandroidkotlin.adapter.knowledge

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticelDetail
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.utils.ImageLoader

class KnowledgeDetailAdapter(val context: Context,data:MutableList<ArticelDetail>)
    : BaseQuickAdapter<ArticelDetail, BaseViewHolder>(R.layout.item_knowledge_detail,data){
    override fun convert(helper: BaseViewHolder?, item: ArticelDetail?) {
        helper ?: return
        item ?: return

        helper.run {
            setText(R.id.tv_knowledge_author,item.author)
            setText(R.id.tv_knowledge_date,item.niceDate)
            setText(R.id.tv_knowledge_chapterName,item.chapterName)
            setText(R.id.tv_article,item.title)
            setImageResource(R.id.iv_knowledge,
                    if(item.collect) R.drawable.icon_like else R.drawable.icon_like_article_not_selected)
                    .addOnClickListener(R.id.iv_knowledge)
        }

        val image = helper?.getView<ImageView>(R.id.iv_knowledge_pic)
        if(item.envelopePic.isNotEmpty()){
            image.visibility = View.VISIBLE
            context?.let {
                ImageLoader.load(it,image,item.envelopePic)
            }
        }else{
            image.visibility = View.GONE
        }
    }
}