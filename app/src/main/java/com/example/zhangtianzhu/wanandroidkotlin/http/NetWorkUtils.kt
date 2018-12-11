package com.example.zhangtianzhu.wanandroidkotlin.http

import android.content.Context
import android.net.ConnectivityManager

class NetWorkUtils {
    companion object {

        fun isNetWorkAvailable(context: Context):Boolean{
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
            var info  = manager.activeNetworkInfo
            return !(info==null||!info.isAvailable)
        }

        fun isNetWorkConnect(context: Context):Boolean{
            val manager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                    as ConnectivityManager
            val info = manager.activeNetworkInfo
            return !(info==null||!info.isConnected)
        }

        /**
         * isWifi
         *
         * @param context
         * @return boolean
         */
        fun isWifi(context: Context): Boolean {
            val connectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetInfo = connectivityManager.activeNetworkInfo
            return activeNetInfo != null && activeNetInfo.type == ConnectivityManager.TYPE_WIFI
        }
    }
}