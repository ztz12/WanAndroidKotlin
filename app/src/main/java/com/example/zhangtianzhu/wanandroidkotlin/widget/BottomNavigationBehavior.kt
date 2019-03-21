package com.example.zhangtianzhu.wanandroidkotlin.widget

import android.animation.ObjectAnimator
import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.View


class BottomNavigationBehavior constructor(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<View>(context, attrs) {
    private var outAnimator: ObjectAnimator? = null
    private var inAnimator: ObjectAnimator? = null

    // 垂直滑动
    override fun onStartNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, directTargetChild: View, target: View, nestedScrollAxes: Int): Boolean {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL
    }

    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray) {
        if (dy > 0) {// 上滑隐藏
            if (outAnimator == null) {
                //kotlin不支持隐式转换，也就是int类型直接提升为float类型，float向下包容，只能强制性类型转换调用toFloat方法
                outAnimator = ObjectAnimator.ofFloat(child, "translationY", 0f, child.height.toFloat())
                outAnimator!!.duration = 200
            }
            if (!outAnimator!!.isRunning && child.translationY <= 0) {
                outAnimator!!.start()
            }
        } else if (dy < 0) {// 下滑显示
            if (inAnimator == null) {
                inAnimator = ObjectAnimator.ofFloat(child, "translationY", child.height.toFloat(), 0f)
                inAnimator!!.duration = 200
            }
            if (!inAnimator!!.isRunning && child.translationY >= child.height) {
                inAnimator!!.start()
            }
        }
    }

}
