package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.view.KeyEvent
import android.view.View
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.adapter.home.TodoPageAdapter
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.TodoEvent
import com.example.zhangtianzhu.wanandroidkotlin.constant.Constants
import com.example.zhangtianzhu.wanandroidkotlin.constant.TodoTypeBean
import com.example.zhangtianzhu.wanandroidkotlin.contract.home.TodoContract
import com.example.zhangtianzhu.wanandroidkotlin.presenter.home.TodoPresenter
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus
import com.example.zhangtianzhu.wanandroidkotlin.utils.StatusBarUtil
import kotlinx.android.synthetic.main.activity_knowledge_list.*
import kotlinx.android.synthetic.main.activity_todo.*
import kotlinx.android.synthetic.main.activity_todo.view.*

class TodoActivity : BaseSwipeBackActivity(),TodoContract.View {

    private lateinit var data:MutableList<TodoTypeBean>

    private lateinit var mTodoPagerAdapter:TodoPageAdapter

    private val mPresenter:TodoPresenter by lazy { TodoPresenter() }

    override fun getLayoutId(): Int {
        return R.layout.activity_todo
    }

    override fun initData() {
        mPresenter.attachView(this)
        todo_toolbar.run {
            title = getString(R.string.nav_todo)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        StatusBarUtil.setStatusColor(window, ContextCompat.getColor(this, R.color.main_status_bar_blue), 1.0f)
        todo_toolbar.setNavigationOnClickListener { onBackPressedSupport() }

        data = acquireData()
    }

    override fun getData() {
        mTodoPagerAdapter = TodoPageAdapter(data,supportFragmentManager)
        vp_todo.run {
            adapter = mTodoPagerAdapter
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tab_todo))
            offscreenPageLimit = data.size
        }

        tab_todo.run {
            setupWithViewPager(vp_todo)
            addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(vp_todo))
            addOnTabSelectedListener(onTabSelectedListener)
        }

        fab_menu.setClosedOnTouchOutside(true)
        fab_add.setOnClickListener(onClickListener)
        fab_done.setOnClickListener(onClickListener)
        fab_todo.setOnClickListener(onClickListener)
    }

    private val onClickListener = View.OnClickListener {
        var index = vp_todo.currentItem
        fab_menu.close(true)
        when(it.id){
            R.id.fab_add ->{
                RxBus.default.post(TodoEvent(Constants.TODO_ADD,index))
            }
            R.id.fab_done ->{
                RxBus.default.post(TodoEvent(Constants.TODO_DONE,index))
            }
            R.id.fab_todo ->{
                RxBus.default.post(TodoEvent(Constants.TODO_NO,index))
            }
        }
    }

    private val onTabSelectedListener = object :TabLayout.OnTabSelectedListener{
        override fun onTabReselected(p0: TabLayout.Tab?) {
        }

        override fun onTabSelected(p0: TabLayout.Tab?) {
            p0?.let {
                vp_todo.setCurrentItem(it.position,false)
            }
        }

        override fun onTabUnselected(p0: TabLayout.Tab?) {

        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    private fun acquireData():MutableList<TodoTypeBean>{
        val list = mutableListOf<TodoTypeBean>()
        list.add(TodoTypeBean(0,"只用这一个"))
        list.add(TodoTypeBean(1,"工作"))
        list.add(TodoTypeBean(2,"学习"))
        list.add(TodoTypeBean(3,"娱乐"))
        return list
    }

    override fun updateColor() {
        super.updateColor()
        changeColor()
    }

    override fun changeColor() {
      if(!ConfigureUtils.getIsNightMode()){
          tab_todo.setBackgroundColor(mThemeColor)

          fab_menu.menuButtonColorNormal = mThemeColor
          fab_menu.menuButtonColorPressed = mThemeColor
          fab_menu.menuButtonColorRipple = mThemeColor

          fab_add.colorNormal = mThemeColor
          fab_add.colorPressed = mThemeColor
          fab_add.colorRipple = mThemeColor

          fab_todo.colorNormal = mThemeColor
          fab_todo.colorPressed = mThemeColor
          fab_todo.colorRipple = mThemeColor

          fab_done.colorNormal = mThemeColor
          fab_done.colorPressed = mThemeColor
          fab_done.colorRipple = mThemeColor
      }
    }

    override fun hideLoading() {

    }

    override fun showErrorMsg(msg: String?) {

    }

    override fun showLoading() {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode ==KeyEvent.KEYCODE_BACK){
            finish()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

}
