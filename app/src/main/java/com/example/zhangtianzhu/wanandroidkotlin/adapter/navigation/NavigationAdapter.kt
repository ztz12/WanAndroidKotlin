package com.example.zhangtianzhu.wanandroidkotlin.adapter.navigation

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.constant.ArticelDetail
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.NavigationData
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.ContentActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.CommonUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.DisplayManager
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout

class NavigationAdapter(context: Context,data:MutableList<NavigationData>)
    :BaseQuickAdapter<NavigationData,BaseViewHolder>(R.layout.item_navigation,data){
    override fun convert(helper: BaseViewHolder?, item: NavigationData?) {
        helper ?: return
        item ?: return
        helper?.setText(R.id.item_navigation_tv,item.name)
        val flowLayout : TagFlowLayout  = helper?.getView(R.id.item_navigation_flow_layout)
        val articles : MutableList<ArticelDetail> = item.articles
        flowLayout.run {
            adapter = object : TagAdapter<ArticelDetail>(articles){
                override fun getView(parent: FlowLayout?, position: Int, t: ArticelDetail?): View {
                    val tv :TextView  = LayoutInflater.from(parent?.context).inflate(R.layout.flow_layout_tv,parent,false) as TextView

                    val padding = DisplayManager.dip2px(10f)!!
                    tv.setPadding(padding,padding,padding,padding)
                    tv.setTextColor(CommonUtil.randomColor())
                    tv.text = articles[position].title
                    setOnTagClickListener { view, position, parent ->
                        val options = ActivityOptions.makeScaleUpAnimation(view,
                                view.width/2,
                                view.height/2,
                                0,0)
                        val data = articles[position]
                        Intent(context,ContentActivity::class.java).run {
                            putExtra(Constants.CONTENT_URL_KEY, data.link)
                            putExtra(Constants.CONTENT_TITLE_KEY, data.title)
                            putExtra(Constants.CONTENT_ID_KEY, data.id)
                            context.startActivity(this, options.toBundle())
                        }
                        true
                    }
                    return tv
                }
            }
        }
    }
}