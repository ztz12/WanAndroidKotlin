package com.example.zhangtianzhu.wanandroidkotlin.model.wechat

import com.example.zhangtianzhu.wanandroidkotlin.base.BaseModel
import com.example.zhangtianzhu.wanandroidkotlin.constant.HttpResult
import com.example.zhangtianzhu.wanandroidkotlin.constant.WeChatData
import com.example.zhangtianzhu.wanandroidkotlin.utils.RetrofitService
import com.example.zhangtianzhu.wanandroidkotlin.utils.SchedulerUtils
import io.reactivex.Observable

class WeChatModel: BaseModel(){
    fun getWeChatData():Observable<HttpResult<MutableList<WeChatData>>>{
        return RetrofitService.service.getWeChatData()
                .compose(SchedulerUtils.toMain())
    }
}