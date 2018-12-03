package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.EventMessage
import com.github.herokotlin.messagelist.view.linkMovementMethod
import kotlinx.android.synthetic.main.message_event.view.*

class EventMessageViewHolder(view: View): MessageViewHolder(view) {

    override fun create() {
        with (itemView) {

            eventView.maxWidth = getContentMaxWidth().toInt()

            eventView.movementMethod = linkMovementMethod

            setOnClickListener {
                callback.onListClick()
            }

        }
    }

    override fun update() {
        val eventMessage = message as EventMessage
        with (itemView) {

            val spannable = formatLinks(eventMessage.event, configuration.linkTextColor)
            configuration.formatText(eventView, spannable)
            eventView.text = spannable

        }
    }

}