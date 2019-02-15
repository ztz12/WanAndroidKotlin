package com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.home

import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseFragment
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.home.TodoListAdapter
import com.example.zhangtianzhu.wanandroidkotlin.app.WanAndroidApplication
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseMvpFragment
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.TodoEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoBean
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoResponseData
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.TodoListContract
import com.example.zhangtianzhu.wanandroidkotlin.http.NetWorkUtils
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.TodoListPresenter
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.AddTodoActivity
import com.example.zhangtianzhu.wanandroidkotlin.utils.DialogUtil
import com.example.zhangtianzhu.wanandroidkotlin.widget.SpaceItemDecoration
import kotlinx.android.synthetic.main.fragment_todo.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.support.v4.startActivity

class TodoFragment : BaseMvpFragment<TodoListContract.View,TodoListContract.Presenter>(), TodoListContract.View {

    override fun createPresenter(): TodoListContract.Presenter = TodoListPresenter()

    private val mTodoBean = mutableListOf<TodoBean>()

    private lateinit var mAdapter: TodoListAdapter

    private val linearLayoutManager: LinearLayoutManager by lazy { LinearLayoutManager(_mActivity) }

    private val mRecyclerItemDecoration by lazy { SpaceItemDecoration(_mActivity) }

    //完成 true 待完成 false
    private var isDone = false

    private var mType: Int = 0

    companion object {
        fun getInstance(type: Int): TodoFragment {
            val fragment = TodoFragment()
            val bundle = Bundle()
            bundle.putInt(Constants.TODO_TYPE, type)
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
            mRecyclerItemDecoration.let { addItemDecoration(it) }
        }

        mAdapter.run {
            bindToRecyclerView(rl_todo)
            setEmptyView(R.layout.fragment_empty_layout)
            onItemChildClickListener = this@TodoFragment.onItemChildClickListener
        }
        changeData()
        mPresenter?.registerEvent()
        refreshTodoData()
    }

    override fun todoRefreshData(todoType: Int) {
        if (mType == todoType) {
            changeData()
        }
    }

    private fun changeData() {
        if (isDone) {
            mPresenter?.getDoneTodoList(1, mType)
        } else {
            mPresenter?.getNoTodoList(1, mType)
        }
    }

    override fun initData() {
        mType = arguments?.getInt(Constants.TODO_TYPE)!!
        mAdapter = TodoListAdapter(_mActivity, mTodoBean, mType)
    }

    override fun lazyLoad() {
    }

    //TODO 第二次进入TODO 清单界面 点击任意三个按钮会崩溃 取消RxBus 换成EventBus
    override fun showTodoEvent(todoEvent: TodoEvent) {
//        if (mType == todoEvent.index) {
//            when (todoEvent.type) {
//                Constants.TODO_ADD -> {
//                    startActivity<AddTodoActivity>(
//                            Pair(Constants.TYPE_KEY, Constants.ADD_TODO_TYPE_KEY),
//                            Pair(Constants.TODO_TYPE, mType)
//                    )
//                }
//                Constants.TODO_DONE -> {
//                    isDone = true
//                    changeData()
//                }
//                Constants.TODO_NO -> {
//                    isDone = false
//                    changeData()
//                }
//            }
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getTodoEvent(todoEvent: TodoEvent){
        if(mType == todoEvent.index){
            when(todoEvent.type){
                Constants.TODO_ADD -> {
                    startActivity<AddTodoActivity>(
                            Pair(Constants.TYPE_KEY, Constants.ADD_TODO_TYPE_KEY),
                            Pair(Constants.TODO_TYPE, mType)
                    )
                }
                Constants.TODO_DONE -> {
                    isDone = true
                    changeData()
                }
                Constants.TODO_NO -> {
                    isDone = false
                    changeData()
                }
            }
        }
    }

    override fun showTodoList(todoResponseData: TodoResponseData, isRefresh: Boolean) {
        todoResponseData.datas.let {
            mAdapter.run {
                if (isRefresh) {
                    replaceData(it)
                } else {
                    addData(it)
                }
                val size = it.size
                if (size < todoResponseData.size) {
                    loadMoreEnd(isRefresh)
                } else {
                    loadMoreComplete()
                }
            }
        }
    }

    override fun showDeleteSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            DialogUtil.showSnackBar(_mActivity, getString(R.string.delete_success))
        }
        changeData()
    }

    override fun showUpdateSuccess(isSuccess: Boolean) {
        if (isSuccess) {
            DialogUtil.showSnackBar(_mActivity, getString(R.string.completed))
        }
        changeData()
    }

    override fun showErrorMsg(msg: String?) {
        DialogUtil.showSnackBar(_mActivity, msg!!)
    }

    override fun showLoading() {
        mDialog.show()
    }

    override fun hideLoading() {
        if (mDialog.isShowing) {
            mDialog.dismiss()
        }
    }

    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
        if (mTodoBean.size > 0) {
            val data = mTodoBean[position]
            when (view.id) {
                R.id.btn_top -> {
                    mTodoBean.remove(data)
                    mAdapter.notifyItemInserted(0)
                    mTodoBean.add(0, data)
                    mAdapter.notifyItemRemoved(position + 1)
                    if (linearLayoutManager.findFirstVisibleItemPosition() == 0) {
                        rl_todo.smoothScrollToPosition(0)
                    }
                }
                R.id.btn_delete -> {
                    if (!NetWorkUtils.isNetWorkAvailable(WanAndroidApplication.context)) {
                        DialogUtil.showSnackBar(_mActivity, getString(R.string.http_error))
                        return@OnItemChildClickListener
                    } else {
                        mPresenter?.deleteTodoList(data.id)
                        mAdapter.notifyItemRemoved(position)
                    }
                }
                R.id.btn_done -> {
                    if (!NetWorkUtils.isNetWorkAvailable(WanAndroidApplication.context)) {
                        DialogUtil.showSnackBar(_mActivity, getString(R.string.http_error))
                        return@OnItemChildClickListener
                    } else {
                        if (isDone) {
                            mPresenter?.updateTodoList(data.id, 0)
                        } else {
                            mPresenter?.updateTodoList(data.id, 1)
                        }
                        mAdapter.notifyItemRemoved(position)
                    }
                }
                R.id.item_todo_content -> {
                    if (isDone) {
                        startActivity<AddTodoActivity>(
                                Pair(Constants.TYPE_KEY, Constants.SEE_TODO_TYPE_KEY),
                                Pair(Constants.TODO_TYPE, mType),
                                Pair(Constants.TODO_BEAN, data)
                        )
                    } else {
                        startActivity<AddTodoActivity>(
                                Pair(Constants.TYPE_KEY, Constants.EDIT_TODO_TYPE_KEY),
                                Pair(Constants.TODO_TYPE, mType),
                                Pair(Constants.TODO_BEAN, data)
                        )
                    }
                }

            }
        }
    }

    private fun refreshTodoData() {
        todo_refresh.run {
            setOnRefreshListener {
                mPresenter?.refreshData(mType, isDone)
                finishRefresh(1000)
            }
            setOnLoadMoreListener {
                mPresenter?.loadMore(mType, isDone)
                finishLoadMore(1000)
            }
        }
    }

}