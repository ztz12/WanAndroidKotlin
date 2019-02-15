package com.example.zhangtianzhu.wanandroidkotlin.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.NetWorkChangeEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.http.NetWorkUtils
import org.greenrobot.eventbus.EventBus

class NetWorkReceiver :BroadcastReceiver(){
    private val haveNetWork :Boolean by Preference(Constants.HAVE_NETWORK,true)
    override fun onReceive(context: Context, intent: Intent) {
        val isConnected = NetWorkUtils.isNetWorkConnect(context)
        if(isConnected){
            if(isConnected!=haveNetWork){
                EventBus.getDefault().post(NetWorkChangeEvent(isConnected))
            }
        }else{
            EventBus.getDefault().post(NetWorkChangeEvent(isConnected))
        }
    }
}