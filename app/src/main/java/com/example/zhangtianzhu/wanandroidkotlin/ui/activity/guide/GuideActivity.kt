package com.example.zhangtianzhu.wanandroidkotlin.ui.activity.guide

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.view.KeyEvent
import android.view.WindowManager
import com.example.zhangtianzhu.wanandroidkotlin.R
import com.example.zhangtianzhu.wanandroidkotlin.base.BaseActivity
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.home.MainActivity
import com.example.zhangtianzhu.wanandroidkotlin.ui.activity.login.LoginActivity
import kotlinx.android.synthetic.main.activity_guide.*
import org.jetbrains.anko.startActivity


class GuideActivity : BaseActivity() {

    private val SCALE_SIZE = 1.3f
    override fun getLayoutId(): Int {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        return R.layout.activity_guide
    }

    override fun initData() {

    }

    override fun getData() {
        animateImage()
        initCountDownView()
        cdv_time.setOnClickListener{
            cdv_time.stop()
            startMainActivity()
            finish()
        }
    }

    override fun onViewCreated(savedInstanceState: Bundle?) {
    }

    private fun animateImage(){
        val animatorX = ObjectAnimator.ofFloat(iv_guide,"scaleX",1f,SCALE_SIZE)
        val animatorY = ObjectAnimator.ofFloat(iv_guide,"scaleY",1f,SCALE_SIZE)
        val animatorSet = AnimatorSet()
        animatorSet.setDuration(3000).play(animatorX).with(animatorY)
        animatorSet.start()
        animatorSet.addListener(object :AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                startMainActivity()
            }
        })
    }

    private fun startMainActivity(){
        //TODO 取消跳转动画,否则点击倒计时按钮有时会崩溃
//        val options = ActivityOptionsCompat.makeCustomAnimation(this,R.anim.screen_zoom_in,R.anim.screen_zoom_out)
//        startActivity(Intent(this@GuideActivity,MainActivity::class.java),options.toBundle())
        if(isFirstIn){
            startActivity<LoginActivity>()
        }else {
            startActivity<MainActivity>()
        }
        finish()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        //屏蔽返回键
        if(keyCode == KeyEvent.KEYCODE_BACK){
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initCountDownView(){
        cdv_time.run {
            setTime(3)
            start()
            setOnLoadingFinishListener { startMainActivity() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(cdv_time.isShown){
            cdv_time.stop()
        }
    }

}
