package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.MessageListCallback
import com.github.herokotlin.messagelist.MessageListConfiguration
import com.github.herokotlin.messagelist.model.EventMessage
import kotlinx.android.synthetic.main.message_event.view.*

class EventMessageViewHolder(view: View): MessageViewHolder(view) {

    override fun create(configuration: MessageListConfiguration, callback: MessageListCallback) {
        with (itemView) {
            eventView.maxWidth = getContentMaxWidth(configuration).toInt()

            setOnClickListener {
                message?.let {
                    callback.onListClick()
                }
            }
        }
    }

    override fun update(configuration: MessageListConfiguration) {
        val eventMessage = message as EventMessage
        with (itemView) {
            configuration.formatEvent(eventView, eventMessage.event)
        }
    }

}