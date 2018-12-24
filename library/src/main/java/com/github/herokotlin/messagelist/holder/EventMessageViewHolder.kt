package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.EventMessage
import com.github.herokotlin.messagelist.view.LinkMovementMethod
import kotlinx.android.synthetic.main.message_event.view.*

internal class EventMessageViewHolder(view: View): MessageViewHolder(view) {

    private val onLinkClick = { link: String ->
        callback.onLinkClick(link)
    }

    override fun create() {
        with (itemView) {
            eventView.maxWidth = getContentMaxWidth().toInt()
            eventView.movementMethod = LinkMovementMethod
        }
    }

    override fun update() {
        val eventMessage = message as EventMessage
        with (itemView) {

            val spannable = formatLinks(eventMessage.event, configuration.linkTextColor, onLinkClick)
            configuration.formatText(eventView, spannable)
            eventView.text = spannable

        }
    }

}