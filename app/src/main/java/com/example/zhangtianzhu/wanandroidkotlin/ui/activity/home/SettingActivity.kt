package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home

import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.afollestad.materialdialogs.color.ColorChooserDialog
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseSwipeBackActivity
import com.example.zhangtianzhu.wanandroidkotlin.bean.home.ColorEvent
import com.example.zhangtianzhu.wanandroidkotlin.ui.fragment.home.SettingPrefFragment
import com.example.zhangtianzhu.wanandroidkotlin.utils.ConfigureUtils
import com.example.zhangtianzhu.wanandroidkotlin.utils.RxBus
import com.example.zhangtianzhu.wanandroidkotlin.utils.StatusBarUtil
import kotlinx.android.synthetic.main.toolbar.*


class SettingActivity : BaseSwipeBackActivity(),ColorChooserDialog.ColorCallback {

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun initData() {
        toolbar.run {
            title = getString(R.string.setting)
            setSupportActionBar(toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        StatusBarUtil.setStatusColor(window, ContextCompat.getColor(this,R.color.main_status_bar_blue),1.0f)
        toolbar.setNavigationOnClickListener{onBackPressedSupport()}
        val transition = fragmentManager.beginTransaction()
        val fragment = SettingPrefFragment.getInstance()
        transition.replace(R.id.fl_setting,fragment)
        transition.commitAllowingStateLoss()
    }

    override fun getData() {
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    override fun onColorSelection(dialog: ColorChooserDialog, selectedColor: Int) {
        if(!dialog.isAccentMode){
            ConfigureUtils.setColor(selectedColor)
        }
        updateColor()
        RxBus.default.post(ColorEvent(true))
    }

    override fun onColorChooserDismissed(dialog: ColorChooserDialog) {
    }

    override fun initView() {

    }

}
