package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.MessageListCallback
import com.github.herokotlin.messagelist.MessageListConfiguration
import com.github.herokotlin.messagelist.model.CardMessage
import kotlinx.android.synthetic.main.message_card_left.view.*

class CardMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

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

            bubbleView.layoutParams.width = dp2px(configuration.cardMessageBubbleWidth)

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

            bubbleView.setOnClickListener {
                message?.let {
                    callback.onContentClick(it)
                }
            }

            bubbleView.setOnLongClickListener {
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

        val cardMessage = message as CardMessage

        with (itemView) {

            configuration.loadImage(avatarView, cardMessage.user.avatar)
            configuration.loadImage(thumbnailView, cardMessage.thumbnail)

            updateImageSize(configuration, avatarView, configuration.userAvatarWidth, configuration.userAvatarHeight, configuration.userAvatarBorderWidth, configuration.userAvatarBorderColor, configuration.userAvatarBorderRadius)
            updateImageSize(configuration, thumbnailView, configuration.cardMessageThumbnailWidth, configuration.cardMessageThumbnailHeight, 0f, 0, configuration.cardMessageThumbnailBorderRadius)

            nameView.text = cardMessage.user.name

            titleView.text = cardMessage.title
            descView.text = cardMessage.desc
            typeView.text = cardMessage.type

            showTimeView(timeView, cardMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}