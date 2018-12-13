package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.TodoRefreshEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoBean
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.AddTodoContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.AddTodoPresenter
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.KeyBordUtil
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus
import com.example.zhangtianzhu.wanandroidkotlin.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_add_todo.*
import java.text.SimpleDateFormat
import java.util.*

class AddTodoActivity : BaseSwipeBackActivity(), AddTodoContract.View {

    private val mPresenter: AddTodoPresenter by lazy { AddTodoPresenter() }

    private var mType = 0

    private var mTypeKey = ""

    private var mTodoBean: TodoBean? = null

    private var mCurrentDate = formatCurrentDate()

    private val mDialogAdd by lazy { DialogUtil.getWaitDialog(this, getString(R.string.save_ing)) }
    override fun getLayoutId(): Int {
        return R.layout.activity_add_todo
    }

    override fun initData() {
        mPresenter.attachView(this)
        intent.extras.let {
            mType = it.getInt(Constants.TODO_TYPE)
            mTypeKey = it.getString(Constants.TYPE_KEY) ?: Constants.ADD_TODO_TYPE_KEY
        }
    }

    override fun getData() {
        addTodo_toolbar.run {
            if (Constants.SEE_TODO_TYPE_KEY == mTypeKey) {
                title = getString(R.string.see)
            } else if (Constants.EDIT_TODO_TYPE_KEY == mTypeKey) {
                title = getString(R.string.edit)
            }
            setSupportActionBar(addTodo_toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        StatusBarUtil.setStatusColor(window, ContextCompat.getColor(this, R.color.main_status_bar_blue), 1.0f)
        addTodo_toolbar.setNavigationOnClickListener { onBackPressedSupport() }

        when (mTypeKey) {
            Constants.SEE_TODO_TYPE_KEY -> {
                mTodoBean = intent.extras.getSerializable(Constants.TODO_BEAN) as TodoBean
                et_title.setText(mTodoBean?.title)
                et_content.setText(mTodoBean?.content)
                tv_date.text = mTodoBean?.dateStr
                et_title.isEnabled = false
                et_content.isEnabled = false
                ll_date.isEnabled = false
                btn_save.visibility = View.GONE
            }
            Constants.EDIT_TODO_TYPE_KEY -> {
                mTodoBean = intent.extras.getSerializable(Constants.TODO_BEAN) as TodoBean
                et_title.setText(mTodoBean?.title)
                et_content.setText(mTodoBean?.content)
                tv_date.text = mTodoBean?.dateStr
            }
        }

        ll_date.setOnClickListener(onClickListener)
        btn_save.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.ll_date -> {
                KeyBordUtil.openKeyBord(et_content, this)
                val now = Calendar.getInstance()
                val date = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val currentMonth = month + 1
                    mCurrentDate = "$year-$currentMonth-$dayOfMonth"
                    tv_date.text = mCurrentDate
                },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                )
                date.show()
            }
            R.id.btn_save -> {
                when (mTypeKey) {
                    Constants.ADD_TODO_TYPE_KEY ->
                        mPresenter.add()
                    Constants.EDIT_TODO_TYPE_KEY ->
                        mPresenter.update(mTodoBean?.id ?: 0)
                }
            }
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    override fun showAddSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            DialogUtil.showSnackBar(this, getString(R.string.save_success))
        }
        RxBus.default.post(TodoRefreshEvent(true, mType))
        finish()
    }

    override fun showUpdateSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            DialogUtil.showSnackBar(this, getString(R.string.update_success))
        }
        RxBus.default.post(TodoRefreshEvent(true, mType))
        finish()
    }

    override fun getTitleInfo(): String = et_title.text.toString()

    override fun getContent(): String = et_content.text.toString()

    override fun getCurrentDate(): String = formatCurrentDate()

    override fun getStatus(): Int = mTodoBean?.status ?: 0

    override fun getType(): Int = mType

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(this, msg!!)
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
