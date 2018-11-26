package com.github.herokotlin.messagelist.holder

import android.text.method.LinkMovementMethod
import android.view.View
import com.github.herokotlin.messagelist.model.EventMessage
import kotlinx.android.synthetic.main.message_event.view.*

class EventMessageViewHolder(view: View): MessageViewHolder(view) {

    override fun create() {
        with (itemView) {

            eventView.maxWidth = getContentMaxWidth().toInt()

            // 一定要加这句，否则 LinkSpan 的 onClick 不执行
            eventView.movementMethod = LinkMovementMethod.getInstance()

            setOnClickListener {
                message?.let {
                    callback.onListClick()
                }
            }

        }
    }

    override fun update() {
        val eventMessage = message as EventMessage
        with (itemView) {

            val spannable = formatLinks(eventMessage.event, configuration.textMessageLinkColor)
            configuration.formatText(eventView, spannable)
            eventView.text = spannable

        }
    }

}