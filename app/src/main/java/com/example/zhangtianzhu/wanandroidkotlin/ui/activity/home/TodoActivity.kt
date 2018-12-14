package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.View
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.home.TodoPagerAdapter
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.TodoEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoTypeBean
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.TodoContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.TodoPresenter
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus
import com.example.zhangtianzhu.wanandroidkotlin.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_todo.*
import org.greenrobot.eventbus.EventBus


class TodoActivity : BaseSwipeBackActivity(),TodoContract.View {

    private lateinit var mPageAdapter: TodoPagerAdapter

    private lateinit var data: MutableList<TodoTypeBean>

    private val mPresenter: TodoPresenter by lazy { TodoPresenter() }

    override fun getLayoutId(): Int {
        return R.layout.activity_todo
    }

    override fun initData() {
        mPresenter.attachView(this)
        todo_toolbar.run {
            title = getString(R.string.nav_todo)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        StatusBarUtil.setStatusColor(window, ContextCompat.getColor(this, R.color.main_status_bar_blue), 1.0f)
        todo_toolbar.setNavigationOnClickListener { onBackPressedSupport() }    }

    override fun getData() {
        data = acquireData()
        mPageAdapter = TodoPagerAdapter(data, supportFragmentManager)
        todo_viewPager.run {
            adapter = mPageAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(todo_tabLayout))
            offscreenPageLimit = data.size
        }
        todo_tabLayout.run {
            setupWithViewPager(todo_viewPager)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(todo_viewPager))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        todo_fab_menu.setClosedOnTouchOutside(true)
        todo_fab_add.setOnClickListener(onClickListener)
        todo_fab_done.setOnClickListener(onClickListener)
        todo_fab_todo.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        val index = todo_viewPager.currentItem
        todo_fab_menu.close(true)
        when (it.id) {
            R.id.todo_fab_add -> {
                EventBus.getDefault().post(TodoEvent(Constants.TODO_ADD,index))
//                RxBus.default.post(TodoEvent(Constants.TODO_ADD, index))
            }
            R.id.todo_fab_done -> {
                EventBus.getDefault().post(TodoEvent(Constants.TODO_DONE,index))

//                RxBus.default.post(TodoEvent(Constants.TODO_DONE, index))
            }
            R.id.todo_fab_todo -> {
                EventBus.getDefault().post(TodoEvent(Constants.TODO_NO,index))

//                RxBus.default.post(TodoEvent(Constants.TODO_NO, index))
            }
        }
    }
    
    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    private val onTabSelectedListener = object : TabLayout.OnTabSelectedListener {
        override fun onTabReselected(tab: TabLayout.Tab?) {
        }

        override fun onTabUnselected(tab: TabLayout.Tab?) {
        }

        override fun onTabSelected(tab: TabLayout.Tab?) {
            tab?.let {
                todo_viewPager.setCurrentItem(it.position, false)
            }
        }
    }

    private fun acquireData(): MutableList<TodoTypeBean> {
        val list = mutableListOf<TodoTypeBean>()
        list.add(TodoTypeBean(0, "只用这一个"))
        list.add(TodoTypeBean(1, "工作"))
        list.add(TodoTypeBean(2, "学习"))
        list.add(TodoTypeBean(3, "娱乐"))
        return list
    }

    override fun updateColor() {
        super.updateColor()
        changeColor()
    }

    override fun changeColor() {
        if (!ConfigureUtils.getIsNightMode()) {
            todo_tabLayout.setBackgroundColor(mThemeColor)

            todo_fab_menu.menuButtonColorNormal = mThemeColor
            todo_fab_menu.menuButtonColorPressed = mThemeColor
            todo_fab_menu.menuButtonColorRipple = mThemeColor

            todo_fab_add.colorNormal = mThemeColor
            todo_fab_add.colorPressed = mThemeColor
            todo_fab_add.colorRipple = mThemeColor

            todo_fab_todo.colorNormal = mThemeColor
            todo_fab_todo.colorPressed = mThemeColor
            todo_fab_todo.colorRipple = mThemeColor

            todo_fab_done.colorNormal = mThemeColor
            todo_fab_done.colorPressed = mThemeColor
            todo_fab_done.colorRipple = mThemeColor
        }
    }

    override fun hideLoading() {

    }

    override fun showErrorMsg(msg: String?) {

    }

    override fun showLoading() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
}
