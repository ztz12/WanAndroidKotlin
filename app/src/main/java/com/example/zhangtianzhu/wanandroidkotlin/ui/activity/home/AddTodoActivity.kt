package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home

import android.os.Bundle
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.AddTodoContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.AddTodoPresenter
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import kotlinx.android.synthetic.main.activity_add_todo.*
import java.text.SimpleDateFormat
import java.util.*

class AddTodoActivity : BaseSwipeBackActivity(),AddTodoContract.View {

    private val mPresenter:AddTodoPresenter by lazy { AddTodoPresenter() }

    private var mType = 0

    private val mDialogAdd by lazy { DialogUtil.getWaitDialog(this,getString(R.string.save_ing)) }
    override fun getLayoutId(): Int {
        return R.layout.activity_add_todo
    }

    override fun initData() {
        mPresenter.attachView(this)
        mType = intent.extras.getInt(Constants.TODO_TYPE)
    }

    override fun getData() {
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    override fun showAddSuccess(isSuccess: Boolean) {
        if(isSuccess){
            DialogUtil.showSnackBar(this,getString(R.string.save_success))
        }
    }

    override fun showUpdateSuccess(isSuccess: Boolean) {
        if(isSuccess){
            DialogUtil.showSnackBar(this,getString(R.string.update_success))
        }
    }

    override fun getTitleInfo(): String = et_title.text.toString()

    override fun getContent(): String = et_content.text.toString()

    override fun getCurrentDate(): String =formatCurrentDate()

    override fun getStatus(): Int =0

    override fun getType(): Int = mType

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(this,msg!!)
    }

    override fun showLoading() {
        mDialogAdd.show()
    }

    override fun hideLoading() {
        mDialogAdd.dismiss()
    }

    /**
     * 格式化当前日期
     */
    private fun formatCurrentDate(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(Date())
    }

}
