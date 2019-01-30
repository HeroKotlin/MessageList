package com.github.herokotlin.messagelist.view

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View

internal class LinkSpan(val link: String, val onClick: (String) -> Unit, val onLongPress: (String) -> Unit): ClickableSpan() {

    var pressTime = 0L

    private val bgColorPressed = Color.parseColor("#15000000")

    override fun onClick(widget: View?) {

    }

    override fun updateDrawState(ds: TextPaint?) {
        ds?.isUnderlineText = false
        if (pressTime > 0) {
            ds?.bgColor = bgColorPressed
        }
        else {
            ds?.bgColor = 0
        }
    }
}