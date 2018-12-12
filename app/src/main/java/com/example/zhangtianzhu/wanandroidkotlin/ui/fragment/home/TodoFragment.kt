package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.home

import android.os.Bundle
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.TodoEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoResponseData
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.TodoListContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.TodoListPresenter
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil

class TodoFragment : BaseFragment(),TodoListContract.View{

    private val mPresenter:TodoListPresenter by lazy { TodoListPresenter() }

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
    }

    override fun initData() {
        mPresenter.attachView(this)
        mType = arguments?.getInt(Constants.TODO_TYPE)!!
    }

    override fun lazyLoad() {
    }

    override fun showTodoEvent(todoEvent: TodoEvent) {

    }

    override fun showNoTodoList(todoResponseData: TodoResponseData) {
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