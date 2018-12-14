package com.example.zhangtianzhu.wanandroidkotlin.constant

import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication
import com.example.zhangtianzhu.wanandroidkotlin.utils.Preference
import java.io.File

object Constants {

    const val BUGLY_ID = "963067f3a0"

    const val BASE_URl = "http://www.wanandroid.com/"

    const val USERNAME = "username"

    const val ISLOGIN = "isLogin"

    const val ISFIRSTIN = "isFirstIn"

    const val SAVE_LOGIN_KEY = "user/login"

    const val SAVE_REGISTER_KEY = "user/register"

    const val COLLECTIONS_WEBSITE = "lg/collect"
    const val UNCOLLECTIONS_WEBSITE = "lg/uncollect"
    const val ARTICLE_WEBSITE = "article"
    const val TODO_WEBSITE = "lg/todo"

    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"
    private val PATCH_FILE: String = WanAndroidApplication.context.cacheDir.absolutePath + File.separator + "data"

    val PATCH_CACHE = "$PATCH_FILE/NetCache"

    const val DELAY_TIME: Long = 2000

    /**
     * Refresh theme color
     */
    const val BLUE_THEME = R.color.colorPrimary

    const val GREEN_THEME = android.R.color.holo_green_light

    const val RED_THEME = android.R.color.holo_red_light

    const val ORANGE_THEME = android.R.color.holo_orange_light

    const val ZERO = 0

    const val ONE = 1

    const val TWO = 2

    const val THREE = 3

    const val FOUR = 4

    const val PARAM1 = "param1"

    const val PARAM2 = "param2"

    const val TYPE_HOME = 0

    const val TYPE_KNOWLEDGE = 1

    const val TYPE_WECHAT = 2

    const val TYPE_NAVIGATION = 3

    const val TYPE_PROJECT = 4

    const val TYPE_COLLECT = 5

    /**
     * url key
     */
    const val CONTENT_URL_KEY = "url"
    /**
     * title key
     */
    const val CONTENT_TITLE_KEY = "title"
    /**
     * id key
     */
    const val CONTENT_ID_KEY = "id"
    /**
     * id key
     */
    const val CONTENT_CID_KEY = "cid"
    /**
     * share key
     */
    const val CONTENT_SHARE_TYPE = "text/plain"
    /**
     * content data key
     */
    const val CONTENT_DATA_KEY = "content_data"

    const val TYPE_KEY = "type"

    const val SEARCH_KEY = "search_key"

    const val TODO_TYPE = "todo_type"
    const val TODO_BEAN = "todo_bean"
    const val TODO_NO = "todo_no"
    const val TODO_ADD = "todo_add"
    const val TODO_DONE = "todo_done"

    const val ADD_TODO_TYPE_KEY = "add_todo_type_key"
    const val SEE_TODO_TYPE_KEY = "see_todo_type_key"
    const val EDIT_TODO_TYPE_KEY = "edit_todo_type_key"

    fun encodeCookie(cookies: List<String>): String {
        val sb = StringBuilder()
        val set = HashSet<String>()
        cookies
                .map { cookie ->
                    cookie.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                }
                .forEach {
                    it.filterNot { set.contains(it) }.forEach { set.add(it) }
                }
        val it = set.iterator()
        while (it.hasNext()) {
            val cookie = it.next()
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        return sb.toString()
    }

    fun saveCookie(url: String?, domain: String?, cookie: String) {
        url ?: return
        var spUrl by Preference(url, cookie)
        @Suppress("UNUSED_VALUE")
        spUrl = cookie

        domain ?: return
        var spDomain by Preference(domain, cookie)
        @Suppress("UNUSED_VALUE")
        spDomain = cookie

    }
}