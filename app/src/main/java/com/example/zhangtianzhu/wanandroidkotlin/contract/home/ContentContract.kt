package com.example.zhangtianzhu.wanandroidkotlin.contract.home

interface ContentContract {
    interface View:CommonContract.View

    interface Presenter:CommonContract.Presenter<View>
}