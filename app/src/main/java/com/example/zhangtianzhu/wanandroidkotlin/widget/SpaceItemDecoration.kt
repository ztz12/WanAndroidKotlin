package com.example.zhangtianzhu.wanandroidkotlin.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View


/**
 *  RecyclerView 分割线
 */
class SpaceItemDecoration(context: Context) : RecyclerView.ItemDecoration() {

    private var mDivider: Drawable? = null
    private val mSectionOffsetV: Int = 0
    private val mSectionOffsetH: Int = 0
    private var mDrawOver = true
    //加载系统自带的分割线（listView用的分割线）
    private var attrs: IntArray = intArrayOf(android.R.attr.listDivider)

    init {
        val a = context.obtainStyledAttributes(attrs)
        mDivider = a.getDrawable(0)
        a.recycle()
    }

    /**
     * 在onDraw方法之后回调，绘制蒙层
     */
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        if (mDivider != null && mDrawOver) {
            draw(c, parent)
        }
    }

    /**
     * 画线
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        if (mDivider != null && mDrawOver) {
            draw(c, parent)
        }
    }

    /**
     * 设置条目周围偏移量
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        if (getOrientation(parent.layoutManager!!) == RecyclerView.VERTICAL) {
            outRect.set(mSectionOffsetH, 0, mSectionOffsetH, mSectionOffsetV)
        } else {
            outRect.set(0, 0, mSectionOffsetV, 0)
        }
    }

    private fun draw(c: Canvas, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight

        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val params = child.layoutParams as RecyclerView.LayoutParams

            val top = child.bottom + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child))
            val bottom = top + if (mDivider!!.intrinsicHeight <= 0) 1 else mDivider!!.intrinsicHeight

            mDivider?.let {
                it.setBounds(left, top, right, bottom)
                it.draw(c)
            }
        }
    }

    private fun getOrientation(layoutManager: RecyclerView.LayoutManager): Int {
        if (layoutManager is LinearLayoutManager) {
            return layoutManager.orientation
        } else if (layoutManager is StaggeredGridLayoutManager) {
            return layoutManager.orientation
        }
        return OrientationHelper.HORIZONTAL
    }
}