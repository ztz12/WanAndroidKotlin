package com.example.zhangtianzhu.wanandroidkotlin.utils

import android.app.Activity
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.example.zhangtianzhu.wanandroidkotlin.R


fun String.getAgent(activity: Activity, agentGroup: ViewGroup,
                    layoutParams: ViewGroup.LayoutParams,
                    webChromeClient: WebChromeClient,
                    webViewClient: WebViewClient) = AgentWeb.with(activity)//
        .setAgentWebParent(agentGroup, layoutParams)
        .useDefaultIndicator()//设置默认进度条
        .setWebChromeClient(webChromeClient)
        .setWebViewClient(webViewClient)
        .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其它应用，向用户询问是否前往
        .createAgentWeb()
        .ready()
        .go(this)!!