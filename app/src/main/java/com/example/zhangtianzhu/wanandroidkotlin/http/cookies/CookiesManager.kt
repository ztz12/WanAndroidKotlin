package com.example.zhangtianzhu.wanandroidkotlin.http.cookies

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl

/**
 * @author lw
 * @date 2018/1/25
 */

class CookiesManager : CookieJar {

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.size > 0) {
            for (item in cookies) {
                COOKIE_STORE.add(url, item)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return COOKIE_STORE[url]
    }

    companion object {

        private val COOKIE_STORE = PersistentCookieStore()

        /**
         * 清除所有cookie
         */
        fun clearAllCookies() {
            COOKIE_STORE.removeAll()
        }

        /**
         * 清除指定cookie
         *
         * @param url HttpUrl
         * @param cookie Cookie
         * @return if clear cookies
         */
        fun clearCookies(url: HttpUrl, cookie: Cookie): Boolean {
            return COOKIE_STORE.remove(url, cookie)
        }

        /**
         * 获取cookies
         *
         * @return List<Cookie>
        </Cookie> */
        val cookies: List<Cookie>
            get() = COOKIE_STORE.getCookies()
    }

}
