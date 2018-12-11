package com.example.zhangtianzhu.wanandroidkotlin.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication
import com.example.zhangtianzhu.wanandroidkotlin.http.NetWorkUtils
import com.example.zhangtianzhu.wanandroidkotlin.R


object ImageLoader {

    //是否开启无图模式，开启后不是Wi-Fi下不加载图片
    private val isLoadImage = !ConfigureUtils.getIsNoPhotoMode() || NetWorkUtils.isWifi(WanAndroidApplication.context)

    fun load(context: Context,imageView: ImageView,imageUrl:String?){
        if(isLoadImage){
            Glide.with(context).clear(imageView)
            val options = RequestOptions
                    .diskCacheStrategyOf(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.bg_placeholder)
            Glide.with(context)
                    .load(imageUrl)
                    .transition(DrawableTransitionOptions().crossFade())
                    .apply(options)
                    .into(imageView)
        }
    }
}