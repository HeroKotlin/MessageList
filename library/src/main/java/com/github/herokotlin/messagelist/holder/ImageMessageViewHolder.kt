package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.MessageListCallback
import com.github.herokotlin.messagelist.MessageListConfiguration
import com.github.herokotlin.messagelist.model.ImageMessage
import kotlinx.android.synthetic.main.message_image_left.view.*

class ImageMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

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

            imageView.setOnClickListener {
                message?.let {
                    callback.onContentClick(it)
                }
            }

            imageView.setOnLongClickListener {
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
        val imageMessage = message as ImageMessage
        with (itemView) {

            configuration.loadImage(avatarView, imageMessage.user.avatar)
            updateImageSize(configuration, avatarView, configuration.userAvatarWidth, configuration.userAvatarHeight, configuration.userAvatarBorderWidth, configuration.userAvatarBorderColor, configuration.userAvatarBorderRadius)

            nameView.text = imageMessage.user.name

            configuration.loadImage(imageView, imageMessage.url)
            updateImageSize(configuration, imageView, imageMessage.width, imageMessage.height, configuration.imageMessageBorderWidth, configuration.imageMessageBorderColor, configuration.imageMessageBorderRadius)

            showTimeView(timeView, imageMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}