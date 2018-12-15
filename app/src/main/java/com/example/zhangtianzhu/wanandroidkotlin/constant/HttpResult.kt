package com.example.zhangtianzhu.wanandroidkotlin.constant

import com.squareup.moshi.Json
import org.litepal.crud.LitePalSupport
import java.io.Serializable

data class HttpResult<T>(@Json(name = "data") val data:T,
                         @Json(name = "errorCode") val errorCode:Int,
                         @Json(name = "errorMsg") val errorMsg:String)

// 登录 注册数据
data class LoginData(
        @Json(name = "collectIds") val collectIds: List<Any>,
        @Json(name = "email") val email: String,
        @Json(name = "icon") val icon: String,
        @Json(name = "id") val id: Int,
        @Json(name = "password") val password: String,
        @Json(name = "type") val type: Int,
        @Json(name = "username") val username: String
)

//首页banner数据
data class BannerData(
        @Json(name = "desc") val desc: String,
        @Json(name = "id") val id: Int,
        @Json(name = "imagePath") val imagePath: String,
        @Json(name = "isVisible") val isVisible:Int,
        @Json(name = "order") val order:Int,
        @Json(name = "title") val title:String,
        @Json(name = "type") val type:Int,
        @Json(name = "url") val url:String
)

//首页数据
data class ArticleData(
        @Json(name = "curPage") val curPage:Int,
        @Json(name = "datas") val datas:MutableList<ArticelDetail>,
        @Json(name = "offset") val offset:Int,
        @Json(name = "over") val over:Boolean,
        @Json(name = "pageCount") val pageCount:Int,
        @Json(name = "size") val size:Int,
        @Json(name = "total") val total:Int
)
data class ArticelDetail(
     @Json(name = "apkLink") val apkLink:String,
     @Json(name = "author") val author:String,
     @Json(name = "chapterId") val chapterId:Int,
     @Json(name = "chapterName") val chapterName: String,
     @Json(name = "collect") var collect:Boolean,
     @Json(name = "courseId") val courseId:Int,
     @Json(name = "desc") val desc:String,
     @Json(name = "envelopePic") val envelopePic:String,
     @Json(name = "fresh") val fresh:Boolean,
     @Json(name = "id") val id:Int,
     @Json(name = "link") val link:String,
     @Json(name = "niceDate") val niceDate:String,
     @Json(name = "origin") val origin:String,
     @Json(name = "projectLink") val projectLink:String,
     @Json(name = "publishTime") val publishTime:Long,
     @Json(name = "superChapterId") val superChapterId:Int,
     @Json(name = "superChapterName") val superChapterName:String,
     @Json(name = "tags") val tags:MutableList<Tag>,
     @Json(name = "title") val title:String,
     @Json(name = "type") val type:Int,
     @Json(name = "userId") val userId:Int,
     @Json(name = "visible") val visible:Int,
     @Json(name = "zan") val zan:Int,
     @Json(name = "top") var top:String
)

data class Tag(
        @Json(name = "name") val name:String,
        @Json(name = "url") val url:String
)

data class CollectionResponseBody(
        @Json(name = "curPage") val curPage: Int,
        @Json(name = "datas") val datas: List<CollectionArticle>,
        @Json(name = "offset") val offset: Int,
        @Json(name = "over") val over: Boolean,
        @Json(name = "pageCount") val pageCount: Int,
        @Json(name = "size") val size: Int,
        @Json(name = "total") val total: Int
)

data class CollectionArticle(
        @Json(name = "author") val author: String,
        @Json(name = "chapterId") val chapterId: Int,
        @Json(name = "chapterName") val chapterName: String,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "desc") val desc: String,
        @Json(name = "envelopePic") val envelopePic: String,
        @Json(name = "id") val id: Int,
        @Json(name = "link") val link: String,
        @Json(name = "niceDate") val niceDate: String,
        @Json(name = "origin") val origin: String,
        @Json(name = "originId") val originId: Int,
        @Json(name = "publishTime") val publishTime: Long,
        @Json(name = "title") val title: String,
        @Json(name = "userId") val userId: Int,
        @Json(name = "visible") val visible: Int,
        @Json(name = "zan") val zan: Int
)

//常用网站
data class UseWebsiteBean(
        @Json(name = "icon")val icon:String,
        @Json(name = "id")val id:Int,
        @Json(name = "link")val link:String,
        @Json(name = "name")val name:String,
        @Json(name = "order")val order:Int,
        @Json(name = "visible")val visible:Int
)

//热搜
data class HotSearchBean(
        @Json(name = "id") val id:Int,
        @Json(name = "link") val link :String,
        @Json(name = "name") val name:String,
        @Json(name = "order") val order:Int,
        @Json(name = "visible") val visible :Int
)

data class SearchHistoryBean(val key : String) :LitePalSupport(){
     val id :Long = 0
}

//知识体系
data class KnowledgeTreeData(
        @Json(name = "children") val children: MutableList<KnowledgeData>,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "id") val id: Int,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "parentChapterId") val parentChapterId: Int,
        @Json(name = "visible") val visible: Int
) : Serializable

data class KnowledgeData(
        @Json(name = "children") val children: List<Any>,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "id") val id: Int,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "parentChapterId") val parentChapterId: Int,
        @Json(name = "visible") val visible: Int
) : Serializable

//导航
data class NavigationData(
        @Json(name = "articles") val articles : MutableList<ArticelDetail>,
        @Json(name = "cid") val cid:Int,
        @Json(name = "name") val name:String
)

// 项目
data class ProjectTreeData(
        @Json(name = "children") val children: MutableList<Any>,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "id") val id: Int,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "parentChapterId") val parentChapterId: Int,
        @Json(name = "visible") val visible: Int
)

// 公众号列表
data class WeChatData(
        @Json(name = "children") val children: MutableList<String>,
        @Json(name = "courseId") val courseId: Int,
        @Json(name = "id") val id: Int,
        @Json(name = "name") val name: String,
        @Json(name = "order") val order: Int,
        @Json(name = "parentChapterId") val parentChapterId: Int,
        @Json(name = "userControlSetTop") val userControlSetTop: Boolean,
        @Json(name = "visible") val visible: Int
)

// TODO工具 类型
data class TodoTypeBean(
        val type: Int,
        val name: String
)

// TODO实体类
data class TodoBean(
        @Json(name = "id") val id: Int,
        @Json(name = "completeDate") val completeDate: String,
        @Json(name = "completeDateStr") val completeDateStr: String,
        @Json(name = "content") val content: String,
        @Json(name = "date") val date: Long,
        @Json(name = "dateStr") val dateStr: String,
        @Json(name = "status") val status: Int,
        @Json(name = "title") val title: String,
        @Json(name = "type") val type: Int,
        @Json(name = "userId") val userId: Int
) : Serializable

data class TodoListBean(
        @Json(name = "date") val date: Long,
        @Json(name = "todoList") val todoList: MutableList<TodoBean>
)

// 所有TODO，包括待办和已完成
data class AllTodoResponseData(
        @Json(name = "type") val type: Int,
        @Json(name = "doneList") val doneList: MutableList<TodoListBean>,
        @Json(name = "todoList") val todoList: MutableList<TodoListBean>
)

data class TodoResponseData(
        @Json(name = "curPage") val curPage: Int,
        @Json(name = "datas") val datas: MutableList<TodoBean>,
        @Json(name = "offset") val offset: Int,
        @Json(name = "over") val over: Boolean,
        @Json(name = "pageCount") val pageCount: Int,
        @Json(name = "size") val size: Int,
        @Json(name = "total") val total: Int
)
