package com.github.herokotlin.messagelist.holder

import android.text.Spannable
import android.view.MotionEvent
import android.view.View
import com.github.herokotlin.messagelist.model.TextMessage
import com.github.herokotlin.messagelist.view.linkMovementMethod
import kotlinx.android.synthetic.main.message_text_left.view.*

class TextMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    override fun create() {
        with (itemView) {

            val contentMaxWidth = getContentMaxWidth().toInt()
            val isUserNameVisible = configuration.leftUserNameVisible && !isRightMessage || configuration.rightUserNameVisible && isRightMessage

            if (isUserNameVisible) {
                nameView.maxWidth = contentMaxWidth
                nameView.setOnClickListener {
                    message?.let {
                        callback.onUserNameClick(it)
                    }
                }
            }
            else {
                nameView.visibility = View.GONE
            }

            textView.maxWidth = contentMaxWidth

            setOnClickListener {
                message?.let {
                    callback.onListClick()
                }
            }

            avatarView.setOnClickListener {
                message?.let {
                    callback.onUserAvatarClick(it)
                }
            }

            nameView.setOnClickListener {
                message?.let {
                    callback.onUserNameClick(it)
                }
            }

            textView.setOnTouchListener { _, event ->

                val text = textView.text

                if (text is Spannable && event.action == MotionEvent.ACTION_UP) {
                    val linkSpan = linkMovementMethod.getLinkSpan(textView, text, event)
                    if (linkSpan != null) {
                        linkSpan.onClick(textView)
                    }
                }

                false
            }

            textView.setOnClickListener {
                message?.let {
                    callback.onContentClick(it)
                }
            }

            textView.setOnLongClickListener {
                message?.let {
                    callback.onContentLongPress(it)
                }
                true
            }

            failureView.setOnClickListener {
                message?.let {
                    callback.onFailureClick(it)
                }
            }

        }
    }

    override fun update() {
        val textMessage = message as TextMessage
        with (itemView) {

            configuration.loadImage(avatarView, textMessage.user.avatar)
            updateImageSize(avatarView, configuration.userAvatarWidth, configuration.userAvatarHeight, configuration.userAvatarBorderWidth, configuration.userAvatarBorderColor, configuration.userAvatarBorderRadius)

            val spannable = formatLinks(textMessage.text, configuration.linkTextColor)
            configuration.formatText(textView, spannable)
            textView.text = spannable

            nameView.text = textMessage.user.name

            showTimeView(timeView, textMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}