package com.github.herokotlin.messagelist.view

import android.graphics.Color
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.github.herokotlin.messagelist.MessageListCallback

class LinkSpan(val link: String, val onClick: (String) -> Unit): ClickableSpan() {

    var isPressed = false

    private val bgColorPressed = Color.parseColor("#15000000")

    override fun onClick(widget: View?) {
        onClick(link)
    }

    override fun updateDrawState(ds: TextPaint?) {
        ds?.isUnderlineText = false
        if (isPressed) {
            ds?.bgColor = bgColorPressed
        }
        else {
            ds?.bgColor = 0
        }
    }
}