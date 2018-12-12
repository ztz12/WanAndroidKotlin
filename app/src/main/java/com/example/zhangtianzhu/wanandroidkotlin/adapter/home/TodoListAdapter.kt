package com.example.zhangtianzhu.wanandroidkotlin.adapter.home

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoBean
import com.example.zhangtianzhu.wanandroidkotlin.R

class TodoListAdapter(val context: Context,data:MutableList<TodoBean>)
    :BaseQuickAdapter<TodoBean,BaseViewHolder>(R.layout.item_todolist,data){
    override fun convert(helper: BaseViewHolder?, item: TodoBean?) {
        helper ?: return
        item ?: return
        helper.setText(R.id.tv_todo_date,item.dateStr)
                .setText(R.id.tv_todo_title,item.title)
                .addOnClickListener(R.id.btn_delete)
                .addOnClickListener(R.id.btn_done)

        val todoDesc = helper.getView<TextView>(R.id.tv_todo_desc)
        if(item.content.isNotEmpty()){
            todoDesc.text = item.content
            todoDesc.visibility = View.VISIBLE
        }else{
            todoDesc.visibility =View.INVISIBLE
        }

        val btnDone = helper.getView<Button>(R.id.btn_done)
        if(item.status==0){
            btnDone.text = context.getString(R.string.mark_done)
        }else if(item.status==1){
            btnDone.text = context.getString(R.string.restore)
        }
    }
}