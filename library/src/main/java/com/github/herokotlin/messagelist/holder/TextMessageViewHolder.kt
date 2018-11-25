package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.MessageListCallback
import com.github.herokotlin.messagelist.MessageListConfiguration
import com.github.herokotlin.messagelist.model.TextMessage
import kotlinx.android.synthetic.main.message_text_left.view.*

class TextMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    override fun create(configuration: MessageListConfiguration, callback: MessageListCallback) {
        with (itemView) {

            val isUserNameVisible = configuration.leftUserNameVisible && !isRightMessage || configuration.rightUserNameVisible && isRightMessage

            if (isUserNameVisible) {
                nameView.setOnClickListener {
                    message?.let {
                        callback.onUserNameClick(it)
                    }
                }
            }
            else {
                nameView.visibility = View.GONE
            }

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

    override fun update(configuration: MessageListConfiguration) {
        val textMessage = message as TextMessage
        with (itemView) {

            configuration.loadImage(avatarView, textMessage.user.avatar)
            updateImageSize(configuration, avatarView, configuration.userAvatarWidth, configuration.userAvatarHeight, configuration.userAvatarBorderWidth, configuration.userAvatarBorderColor, configuration.userAvatarBorderRadius)

            textView.maxWidth = getContentMaxWidth(configuration).toInt()
            configuration.formatText(textView, textMessage.text)

            nameView.text = textMessage.user.name

            showTimeView(timeView, textMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}