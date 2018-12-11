package com.example.zhangtianzhu.wanandroidkotlin.contract.knowledge

import com.example.zhangtianzhu.wanandroidkotlin.base.IPresenter
import com.example.zhangtianzhu.wanandroidkotlin.base.IView
import com.example.zhangtianzhu.wanandroidkotlin.constant.KnowledgeTreeData

interface KnowledgeContract {
    interface View : IView{

        fun scrollTop()

        fun showKnowledgeData(knowledgeTreeData: List<KnowledgeTreeData>)
    }

    interface Presenter : IPresenter<View>{
        fun getKnowledgeTreeData()

        fun refreshKnowledgeSystem()
    }
}