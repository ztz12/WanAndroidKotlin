package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.home

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.home.TodoListAdapter
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.TodoEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoBean
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoResponseData
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.TodoListContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.TodoListPresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.AddTodoActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import kotlinx.android.synthetic.main.fragment_todo.*
import org.jetbrains.anko.support.v4.startActivity

class TodoFragment : BaseFragment(),TodoListContract.View{

    private val mPresenter:TodoListPresenter by lazy { TodoListPresenter() }

    private val mTodoBean = mutableListOf<TodoBean>()

    private val mAdapter:TodoListAdapter by lazy { TodoListAdapter(_mActivity,mTodoBean) }

    private val linearLayoutManager:LinearLayoutManager by lazy { LinearLayoutManager(_mActivity) }

    //完成 true 待完成 false
    private var isDone = false

    private var mType :Int = 0
    companion object {
        fun getInstance(type:Int):TodoFragment{
            val fragment = TodoFragment()
            val bundle = Bundle()
            bundle.putInt(Constants.TODO_TYPE,type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_todo
    }

    override fun getData() {
        rl_todo.run {
            layoutManager = linearLayoutManager
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }

        mAdapter.run {
            bindToRecyclerView(rl_todo)
            setEmptyView(R.layout.fragment_empty_layout)
        }
        changeData()
    }

    private fun changeData(){
        if(isDone){
            mPresenter.getDoneTodoList(1,mType)
        }else{
            mPresenter.getNoTodoList(1,mType)
        }
    }

    override fun initData() {
        mPresenter.attachView(this)
        mType = arguments?.getInt(Constants.TODO_TYPE)!!
    }

    override fun lazyLoad() {
    }

    override fun showTodoEvent(todoEvent: TodoEvent) {
        if(mType==todoEvent.index){
            when(todoEvent.type){
                Constants.TODO_ADD ->{
                    startActivity<AddTodoActivity>(
                            Pair(Constants.TODO_TYPE,mType)
                    )
                }
                Constants.TODO_DONE ->{
                    isDone = true
                    changeData()
                }
                Constants.TODO_NO ->{
                    isDone = false
                    changeData()
                }
            }
        }
    }

    override fun showTodoList(todoResponseData: TodoResponseData, isRefresh: Boolean) {
        todoResponseData.datas.let {
            mAdapter.run {
                if(isRefresh){
                    replaceData(it)
                }else{
                    addData(it)
                }
                val size = it.size
                if(size<todoResponseData.size){
                    loadMoreEnd(isRefresh)
                }else{
                    loadMoreComplete()
                }
            }
        }
    }

    override fun showDeleteSuccess(isSuccess: Boolean) {
        if(isSuccess){
            DialogUtil.showSnackBar(_mActivity,getString(R.string.delete_success))
        }
    }

    override fun showUpdateSuccess(isSuccess: Boolean) {
        if(isSuccess){
            DialogUtil.showSnackBar(_mActivity,getString(R.string.completed))
        }
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(_mActivity,msg!!)
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        if(mDialog.isShowing){
            mDialog.dismiss()
        }
    }

}