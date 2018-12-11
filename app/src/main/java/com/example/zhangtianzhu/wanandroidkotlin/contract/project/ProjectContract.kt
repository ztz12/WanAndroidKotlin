package com.example.zhangtianzhu.wanandroidkotlin.contract.project

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView
import com.example.zhangtianzhu.wanandroidkotlin.constant.ProjectTreeData

interface ProjectContract {
    interface View:IView{
        fun scrollTop()

        fun changeColor()

        fun showProjectData(projectTreeData: MutableList<ProjectTreeData>)
    }

    interface Presenter:IPresenter<View>{
        fun getProjectData()

        fun registerEvent()
    }
}