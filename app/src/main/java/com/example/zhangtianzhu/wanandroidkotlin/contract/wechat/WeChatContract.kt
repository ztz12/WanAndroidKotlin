package com.example.zhangtianzhu.wanandroidkotlin.contract.wechat

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView
import com.example.zhangtianzhu.wanandroidkotlin.constant.WeChatData

interface WeChatContract {
    interface View:IView{
        fun scrollTop()

        fun changeColor()

        fun showWebChatData(weChatData: MutableList<WeChatData>)
    }

    interface Presenter : IPresenter<View>{
        fun getWeChatData()

        fun registerEvent()
    }
}