package com.example.zhangtianzhu.wanandroidkotlin.http.interceptor

import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.utils.Preference
import okhttp3.Interceptor
import okhttp3.Response

/**
 * @desc HeaderInterceptor: 设置请求头
 */
class HeaderInterceptor : Interceptor {

    /**
     * token
     */
    private var token: String by Preference("token", "")

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val builder = request.newBuilder()

        builder.addHeader("Content-type", "application/json; charset=utf-8")
                // .header("token", token)
                // .method(request.method(), request.body())

        val domain = request.url().host()
        val url = request.url().toString()
        if (domain.isNotEmpty() && (url.contains(Constants.COLLECTIONS_WEBSITE)
                        || url.contains(Constants.UNCOLLECTIONS_WEBSITE)
                        || url.contains(Constants.ARTICLE_WEBSITE)
                        || url.contains(Constants.TODO_WEBSITE))) {
            val spDomain: String by Preference(domain, "")
            val cookie: String = if (spDomain.isNotEmpty()) spDomain else ""
            if (cookie.isNotEmpty()) {
                // 将 Cookie 添加到请求头
                builder.addHeader(Constants.COOKIE_NAME, cookie)
            }
        }

        return chain.proceed(builder.build())
    }

}