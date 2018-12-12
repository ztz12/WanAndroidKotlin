package com.example.zhangtianzhu.wanandroidkotlin.constant

import io.reactivex.Observable
import retrofit2.http.*

interface Api {

    /**
     * 登录
     */
    @POST("user/login")
    @FormUrlEncoded
    fun getLoginData(@Field("username") username: String,
                     @Field("password") password: String): Observable<HttpResult<LoginData>>


    /**
     * 注册
     */
    @POST("user/register")
    @FormUrlEncoded
    fun getRegisterData(@Field("username") username: String,
                        @Field("password") password: String,
                        @Field("repassword") repassword: String): Observable<HttpResult<LoginData>>

    /**
     * 退出登录
     * http://www.wanandroid.com/user/logout/json
     */
    @GET("user/logout/json")
    fun logout(): Observable<HttpResult<Any>>

    /**
     * 获取轮播图
     */
    @GET("banner/json")
    fun getBannerData(): Observable<HttpResult<List<BannerData>>>


    /**
     * 获取首页置顶文章列表
     * http://www.wanandroid.com/article/top/json
     */
    @GET("article/top/json")
    fun getTopArticles(): Observable<HttpResult<MutableList<ArticelDetail>>>

    /**
     * 获取文章列表
     * http://www.wanandroid.com/article/list/0/json
     * @param pageNum
     */
    @GET("article/list/{pageNum}/json")
    fun getArticles(@Path("pageNum") pageNum: Int): Observable<HttpResult<ArticleData>>

    /**
     *  获取收藏列表
     *  http://www.wanandroid.com/lg/collect/list/0/json
     *  @param page
     */
    @GET("lg/collect/list/{page}/json")
    fun getCollectList(@Path("page") page: Int): Observable<HttpResult<CollectionResponseBody>>

    /**
     * 收藏站内文章
     * http://www.wanandroid.com/lg/collect/1165/json
     * @param id article id
     */
    @POST("lg/collect/{id}/json")
    fun addCollectArticle(@Path("id") id: Int): Observable<HttpResult<Any>>

    /**
     * 收藏站外文章
     * http://www.wanandroid.com/lg/collect/add/json
     * @param title
     * @param author
     * @param link
     */
    @POST("lg/collect/add/json")
    @FormUrlEncoded
    fun addCoolectOutsideArticle(@Field("title") title: String,
                                 @Field("author") author: String,
                                 @Field("link") link: String): Observable<HttpResult<Any>>

    /**
     * 文章列表中取消收藏文章
     * http://www.wanandroid.com/lg/uncollect_originId/2333/json
     * @param id
     */
    @POST("lg/uncollect_originId/{id}/json")
    fun cancelCollectArticle(@Path("id") id: Int): Observable<HttpResult<Any>>

    /**
     * 我的收藏列表中取消收藏文章
     * http://www.wanandroid.com/lg/uncollect/2805/json
     * @param id
     * @param originId
     */
    @POST("lg/uncollect/{id}/json")
    @FormUrlEncoded
    fun removeCollectArticle(@Path("id") id: Int,
                             @Field("originId") originId: Int = -1): Observable<HttpResult<Any>>

    /**
     * 热搜
     */
    @GET("hotkey/json")
    fun hotSearchData(): Observable<HttpResult<MutableList<HotSearchBean>>>

    /**
     * 搜索
     */
    @POST("article/query/{page}/json")
    @FormUrlEncoded
    fun getSearchList(@Path("page") page: Int,
                      @Field("k") key: String): Observable<HttpResult<ArticleData>>

    /**
     * 知识体系
     */
    @GET("tree/json")
    fun getKnowledgeTree(): Observable<HttpResult<List<KnowledgeTreeData>>>

    /**
     * 知识体系下的文章
     */
    @GET("article/list/{pageNum}/json")
    fun getKnowledgeList(@Path("pageNum") pageNum: Int,
                         @Query("cid") cid: Int): Observable<HttpResult<ArticleData>>

    /**
     * 导航
     */
    @GET("navi/json")
    fun getNavigationData(): Observable<HttpResult<MutableList<NavigationData>>>

    /**
     * 项目分类
     */
    @GET("project/tree/json")
    fun getProjectData(): Observable<HttpResult<MutableList<ProjectTreeData>>>

    /**
     * 项目列表
     */
    @GET("project/list/{pageNum}/json")
    fun getProjectListData(@Path("pageNum") pageNum: Int,
                           @Query("cid") cid: Int): Observable<HttpResult<ArticleData>>

    /**
     * 微信列表
     */
    @GET("wxarticle/chapters/json")
    fun getWeChatData(): Observable<HttpResult<MutableList<WeChatData>>>

    /**
     * 获取TODO列表数据
     * http://wanandroid.com/lg/todo/list/0/json
     * @param type
     */
    @POST("/lg/todo/list/{type}/json")
    fun getTodoList(@Path("type") type: Int): Observable<HttpResult<AllTodoResponseData>>

    /**
     * 获取未完成Todo列表
     * http://wanandroid.com/lg/todo/listnotdo/0/json/1
     * @param type 类型拼接在链接上，目前支持0,1,2,3
     * @param page 拼接在链接上，从1开始
     */
    @POST("/lg/todo/listnotdo/{type}/json/{page}")
    fun getNoTodoList(@Path("page") page: Int, @Path("type") type: Int): Observable<HttpResult<TodoResponseData>>

    /**
     * 获取已完成Todo列表
     * http://www.wanandroid.com/lg/todo/listdone/0/json/1
     * @param type 类型拼接在链接上，目前支持0,1,2,3
     * @param page 拼接在链接上，从1开始
     */
    @POST("/lg/todo/listdone/{type}/json/{page}")
    fun getDoneList(@Path("page") page: Int, @Path("type") type: Int): Observable<HttpResult<TodoResponseData>>

    /**
     * 仅更新完成状态Todo
     * http://www.wanandroid.com/lg/todo/done/80/json
     * @param id 拼接在链接上，为唯一标识
     * @param status 0或1，传1代表未完成到已完成，反之则反之
     */
    @POST("/lg/todo/done/{id}/json")
    @FormUrlEncoded
    fun updateTodoById(@Path("id") id: Int, @Field("status") status: Int): Observable<HttpResult<Any>>

    /**
     * 删除一条Todo
     * http://www.wanandroid.com/lg/todo/delete/83/json
     * @param id
     */
    @POST("/lg/todo/delete/{id}/json")
    fun deleteTodoById(@Path("id") id: Int): Observable<HttpResult<Any>>

    /**
     * 新增一条Todo
     * http://www.wanandroid.com/lg/todo/add/json
     * @param body
     *          title: 新增标题
     *          content: 新增详情
     *          date: 2018-08-01
     *          type: 0
     */
    @POST("/lg/todo/add/json")
    @FormUrlEncoded
    fun addTodo(@FieldMap map: MutableMap<String, Any>): Observable<HttpResult<Any>>

    /**
     * 更新一条Todo内容
     * http://www.wanandroid.com/lg/todo/update/83/json
     * @param body
     *          title: 新增标题
     *          content: 新增详情
     *          date: 2018-08-01
     *          status: 0 // 0为未完成，1为完成
     *          type: 0
     */
    @POST("/lg/todo/update/{id}/json")
    @FormUrlEncoded
    fun updateTodo(@Path("id") id:Int, @FieldMap map: MutableMap<String, Any>): Observable<HttpResult<Any>>
}