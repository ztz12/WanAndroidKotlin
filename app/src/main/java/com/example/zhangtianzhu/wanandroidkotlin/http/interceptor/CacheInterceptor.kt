package com.example.zhangtianzhu.wanandroidkotlin.http.interceptor

import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication
import com.example.zhangtianzhu.wanandroidkotlin.http.NetWorkUtils
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        if(!NetWorkUtils.isNetWorkAvailable(WanAndroidApplication.context)){
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build()
        }
        val response = chain.proceed(request)
        if(NetWorkUtils.isNetWorkAvailable(WanAndroidApplication.context)){
            val maxAge = 0
            response.newBuilder()
                    //max-age 指的是缓存存活时长  Cache-Control是服务器返回Response添加的头信息 是告诉客户端是要从本地读取缓存，还是从服务器获取
                    .header("Cache-Control","public,max-age = $maxAge")
                    .removeHeader("Retrofit")//清除头信息，防止服务器不支持，返回干扰信息
                    .build()
        }else {
            val maxStale = 60*60*24*28
            response.newBuilder()
                    //max-stale 客户端可以接收超出超时时长的信息   public 缓存所有数据
                    .header("Cache-Control","public,only-if-cache,max-stale=$maxStale")
                    .removeHeader("Retrofit")
                    .build()
        }
        return response
    }
}