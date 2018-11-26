package com.github.herokotlin.messagelist.view

import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import com.github.herokotlin.messagelist.MessageListCallback

class LinkSpan(val link: String, val callback: MessageListCallback): ClickableSpan() {

    override fun onClick(widget: View?) {
        callback.onLinkClick(link)
    }

    override fun updateDrawState(ds: TextPaint?) {
        ds?.isUnderlineText = false
    }
}