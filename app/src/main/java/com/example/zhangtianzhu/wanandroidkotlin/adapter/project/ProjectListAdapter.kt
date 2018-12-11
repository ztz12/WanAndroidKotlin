package com.example.zhangtianzhu.wanandroidkotlin.adapter.project

import android.content.Context
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication.Companion.context
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticelDetail
import com.example.zhangtianzhu.wanandroidkotlin.utils.ImageLoader

class ProjectListAdapter(context:Context,datas:MutableList<ArticelDetail>)
    :BaseQuickAdapter<ArticelDetail,BaseViewHolder>(R.layout.item_project_list,datas){
    override fun convert(helper: BaseViewHolder?, item: ArticelDetail?) {
        helper ?: return
        item ?: return
        helper.setText(R.id.item_project_list_title_tv, Html.fromHtml(item.title))
                .setText(R.id.item_project_list_content_tv, Html.fromHtml(item.desc))
                .setText(R.id.item_project_list_time_tv, item.niceDate)
                .setText(R.id.item_project_list_author_tv, item.author)
                .setImageResource(R.id.item_project_list_like_iv,
                        if (item.collect) R.drawable.icon_like else R.drawable.icon_like_article_not_selected
                )
                .addOnClickListener(R.id.item_project_list_like_iv)
        context.let {
            ImageLoader.load(it, helper.getView(R.id.item_project_list_iv),item.envelopePic)
        }
    }

}