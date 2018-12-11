package com.example.zhangtianzhu.wanandroidkotlin.utils

import com.example.zhangtianzhu.wanandroidkotlin.BuildConfig
import com.example.zhangtianzhu.wanandroidkotlin.constant.Api
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.http.cookies.CookiesManager
import com.example.zhangtianzhu.wanandroidkotlin.http.interceptor.SaveCookieInterceptor
import com.example.zhangtianzhu.wanandroidkotlin.http.interceptor.CacheInterceptor
import com.example.zhangtianzhu.wanandroidkotlin.http.interceptor.HeaderInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object RetrofitService {
    private var retrofit:Retrofit? =null

    val service :Api by lazy { getRetrofitService()!!.create(Api::class.java) }
    private fun getRetrofitService():Retrofit{
        if(retrofit ==null){
            synchronized(Retrofit::class.java){
                if(retrofit ==null){
                    retrofit = Retrofit.Builder()
                            .baseUrl(Constants.BASE_URl)
                            .client(getOkHttp())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .addConverterFactory(MoshiConverterFactory.create())
                            .build()
                }
            }
        }
        return retrofit!!
    }

    private fun getOkHttp():OkHttpClient{
        val builder = OkHttpClient.Builder()
        val httpInterpolator = HttpLoggingInterceptor()
        if(BuildConfig.DEBUG){
            httpInterpolator.level = HttpLoggingInterceptor.Level.BODY
        }else{
            httpInterpolator.level = HttpLoggingInterceptor.Level.NONE
        }
        val file = File(Constants.PATCH_CACHE)
        val cache = Cache(file,1024*1024*50)
        //run 作用域函数，这里返回本身 builder属性
        builder.run {
            addInterceptor(httpInterpolator)
            addInterceptor(HeaderInterceptor())
            addInterceptor(SaveCookieInterceptor())
            //设置缓存  addInterceptor 添加应用拦截器，应用拦截器优先于网络拦截器执行。addNetworkInterceptor添加网络拦截器
            addNetworkInterceptor(CacheInterceptor())
            addInterceptor(CacheInterceptor())
            cache(cache)
            //设置错误重连
            retryOnConnectionFailure(true)
            //设置认证
            cookieJar(CookiesManager())
            //设置超时
            connectTimeout(10,TimeUnit.SECONDS)
            readTimeout(20,TimeUnit.SECONDS)
            writeTimeout(20,TimeUnit.SECONDS)
        }
        return builder.build()
    }
}