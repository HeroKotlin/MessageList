package com.github.herokotlin.messagelist.view

import android.text.Selection
import android.text.Spannable
import android.text.method.BaseMovementMethod
import android.util.Log
import android.view.MotionEvent
import android.widget.TextView

internal object LinkMovementMethod: BaseMovementMethod() {

    private var linkSpan: LinkSpan? = null

        set(value) {
            if (value == null) {
                field?.pressTime = 0
            }
            else {
                value.pressTime = System.currentTimeMillis()
            }
            field = value
        }

    private fun getLinkSpan(widget: TextView, text: Spannable, event: MotionEvent): LinkSpan? {

        var x = event.x.toInt()
        var y = event.y.toInt()

        x -= widget.totalPaddingLeft
        y -= widget.totalPaddingTop

        x += widget.scrollX
        y += widget.scrollY

        val layout = widget.layout
        val line = layout.getLineForVertical(y)
        val off = layout.getOffsetForHorizontal(line, x.toFloat())

        val link = text.getSpans(off, off, LinkSpan::class.java)
        if (link.isNotEmpty()) {
            return link[0]
        }

        return null

    }

    override fun onTouchEvent(widget: TextView, text: Spannable, event: MotionEvent?): Boolean {

        val action = event?.actionMasked

        return when (action) {
            MotionEvent.ACTION_DOWN -> {
                val span = getLinkSpan(widget, text, event)
                if (span != null) {
                    Selection.setSelection(text, text.getSpanStart(span), text.getSpanEnd(span))
                }
                linkSpan = span
                linkSpan != null
            }

            MotionEvent.ACTION_MOVE -> {
                val span = getLinkSpan(widget, text, event)
                if (linkSpan != null && span != linkSpan) {
                    linkSpan = null
                    Selection.removeSelection(text)
                }
                linkSpan != null
            }

            MotionEvent.ACTION_UP -> {
                var result = false
                linkSpan?.let {
                    result = true
                    if (System.currentTimeMillis() - it.pressTime > 1000) {
                        it.onLongPress(it.link)
                    }
                    else {
                        it.onClick(it.link)
                    }
                    linkSpan = null
                    Selection.removeSelection(text)
                }
                result
            }

            else -> {
                if (linkSpan != null) {
                    linkSpan = null
                    Selection.removeSelection(text)
                }
                false
            }
        }

    }

    override fun initialize(widget: TextView?, text: Spannable?) {
        Selection.removeSelection(text)
    }

}