package com.example.zhangtianzhu.wanandroidkotlin.widget

import android.view.animation.Interpolator


class ElasticOutInterpolator : Interpolator {

    override fun getInterpolation(t: Float): Float {
        if (t == 0f) {
            return 0f
        }
        if (t >= 1) {
            return 1f
        }
        val p = .3f
        val s = p / 4
        return Math.pow(2.0, (-10 * t).toDouble()).toFloat() * Math.sin(((t - s) * (2 * Math.PI.toFloat()) / p).toDouble()).toFloat() + 1
    }
}