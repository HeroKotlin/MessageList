package com.github.herokotlin.messagelist.view

import android.graphics.drawable.ColorDrawable
import android.support.v4.widget.PopupWindowCompat
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.github.herokotlin.messagelist.R

internal class MenuWindow: PopupWindow() {

    init {
        width = ViewGroup.LayoutParams.WRAP_CONTENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT

        isFocusable = true
        isOutsideTouchable = true

        // 加这行的原因看这里 https://juejin.im/entry/575134aa6be3ff006bda6d4c
        setBackgroundDrawable(ColorDrawable(0x00000000))
    }

    private val offset: Int by lazy {
        contentView.resources.getDimensionPixelSize(R.dimen.message_list_menu_offset)
    }

    private fun getMeasureSpec(spec: Int): Int {
        return View.MeasureSpec.makeMeasureSpec(
            View.MeasureSpec.getSize(spec),
            if (spec == ViewGroup.LayoutParams.WRAP_CONTENT) {
                View.MeasureSpec.UNSPECIFIED
            }
            else {
                View.MeasureSpec.EXACTLY
            }
        )
    }

    fun show(view: View, contentView: View) {
        this.contentView = contentView
        contentView.measure(
            getMeasureSpec(width),
            getMeasureSpec(height)
        )
        val offsetX = (view.width - contentView.measuredWidth) / 2
        val offsetY = -(contentView.measuredHeight + view.height + offset)
        PopupWindowCompat.showAsDropDown(this, view, offsetX, offsetY, Gravity.START)
    }

    fun hide() {
        this.dismiss()
    }

}