package com.github.herokotlin.messagelist.view

import android.text.Selection
import android.text.Spannable
import android.text.method.BaseMovementMethod
import android.view.MotionEvent
import android.widget.TextView

internal object LinkMovementMethod: BaseMovementMethod() {

    private var linkSpan: LinkSpan? = null

    private var longPressTask: Runnable? = null

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

    private fun setLinkSpan(widget: TextView, text: Spannable, span: LinkSpan?) {

        if (span != null) {
            span.isPressed = true
            Selection.setSelection(text, text.getSpanStart(span), text.getSpanEnd(span))
            longPressTask = Runnable {
                linkSpan?.let {
                    it.onLongPress(it.link)
                    setLinkSpan(widget, text, null)
                }
            }
            widget.postDelayed(longPressTask, 500)
        }
        else {
            linkSpan?.isPressed = false
            Selection.removeSelection(text)
            longPressTask?.let {
                widget.removeCallbacks(it)
                longPressTask = null
            }
        }

        linkSpan = span

    }

    override fun onTouchEvent(widget: TextView, text: Spannable, event: MotionEvent?): Boolean {

        val action = event?.actionMasked

        return when (action) {
            MotionEvent.ACTION_DOWN -> {
                setLinkSpan(widget, text, getLinkSpan(widget, text, event))
                linkSpan != null
            }

            MotionEvent.ACTION_MOVE -> {
                linkSpan?.let {
                    val span = getLinkSpan(widget, text, event)
                    if (span != it) {
                        setLinkSpan(widget, text, null)
                    }
                }
                linkSpan != null
            }

            MotionEvent.ACTION_UP -> {
                var result = false
                linkSpan?.let {
                    result = true
                    it.onClick(it.link)
                    setLinkSpan(widget, text, null)
                }
                result
            }

            else -> {
                linkSpan?.let {
                    setLinkSpan(widget, text, null)
                }
                false
            }
        }

    }

    override fun initialize(widget: TextView?, text: Spannable?) {
        Selection.removeSelection(text)
    }

}