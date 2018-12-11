package com.example.zhangtianzhu.wanandroidkotlin.http.interceptor

import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import okhttp3.Interceptor
import okhttp3.Response

class SaveCookieInterceptor :Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)
        val requestUrl = request.url().toString()
        val domain = request.url().host()

        if((requestUrl.contains(Constants.SAVE_LOGIN_KEY)|| requestUrl.contains(Constants.SAVE_REGISTER_KEY))
                        && !response.headers(Constants.SET_COOKIE_KEY).isEmpty()){
            val cookies = response.headers(Constants.SET_COOKIE_KEY)
            val cookie = Constants.encodeCookie(cookies)
            Constants.saveCookie(requestUrl,domain,cookie)
        }
        return response
    }
}