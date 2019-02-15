package com.example.zhangtianzhu.wanandroidkotlin.adapter.home

import android.content.Context
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticelDetail
import com.example.zhangtianzhu.wanandroidkotlin.utils.ImageLoader

class HomeAdapter(private val context: Context, data:MutableList<ArticelDetail>)
    :BaseQuickAdapter<ArticelDetail,BaseViewHolder>(R.layout.item_home_layout,data){
    override fun convert(helper: BaseViewHolder?, item: ArticelDetail?) {
        helper?:return
        item?:return
        helper.run {
            setText(R.id.tv_article,Html.fromHtml(item.title))
            setText(R.id.tv_home_author,item.author)
            setText(R.id.tv_home_date,item.niceDate)
            setImageResource(R.id.iv_collect,
                    if(item.collect) R.drawable.icon_like else R.drawable.icon_like_article_not_selected)
                    .addOnClickListener(R.id.iv_collect)
        }

        //图片
        if(item.envelopePic.isNotEmpty()){
            helper.getView<ImageView>(R.id.iv_home_pic)
                    .visibility = View.VISIBLE
            context.let {
                ImageLoader.load(it,helper.getView(R.id.iv_home_pic),item.envelopePic)
            }
        }else{
            helper.getView<ImageView>(R.id.iv_home_pic).visibility = View.GONE
        }

        //置顶
        if(item.top=="1"){
            helper.getView<TextView>(R.id.tv_home_top).visibility=View.VISIBLE
        }else{
            helper.getView<TextView>(R.id.tv_home_top).visibility = View.GONE
        }

        //显示"新"
        if(item.fresh){
            helper.getView<TextView>(R.id.tv_home_refresh).visibility = View.VISIBLE
        }else{
            helper.getView<TextView>(R.id.tv_home_refresh).visibility = View.GONE
        }

        //标签
        val articleTag = helper.getView<TextView>(R.id.tv_home_article_tag)
        if(item.tags.size>0){
            articleTag.visibility = View.VISIBLE
            articleTag.text = item.tags[0].name
        }else{
            articleTag.visibility = View.GONE
        }

        //项目分级
        val chapterName = when{
            item.superChapterName.isNotEmpty() and item.chapterName.isNotEmpty() ->
                    "${item.superChapterName}/${item.chapterName}"
            item.superChapterName.isNotEmpty() -> "${item.superChapterName}"
            item.chapterName.isNotEmpty() -> "{${item.chapterName}}"
            else -> ""
        }
        helper.setText(R.id.tv_home_chapterName,chapterName)
    }
}