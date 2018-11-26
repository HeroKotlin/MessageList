package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.EventMessage
import kotlinx.android.synthetic.main.message_event.view.*

class EventMessageViewHolder(view: View): MessageViewHolder(view) {

    override fun create() {
        with (itemView) {

            eventView.maxWidth = getContentMaxWidth().toInt()

            setLinkClickListener(eventView)

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