package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.ImageMessage
import kotlinx.android.synthetic.main.message_image_left.view.*

class ImageMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    override fun create() {

        with (itemView) {

            val isUserNameVisible = configuration.leftUserNameVisible && !isRightMessage || configuration.rightUserNameVisible && isRightMessage

            if (isUserNameVisible) {
                nameView.maxWidth = getContentMaxWidth().toInt()
                nameView.setOnClickListener {
                    callback.onUserNameClick(message)
                }
            }
            else {
                nameView.visibility = View.GONE
            }

            setOnClickListener {
                callback.onListClick()
            }

            avatarView.setOnClickListener {
                callback.onUserAvatarClick(message)
            }

            nameView.setOnClickListener {
                callback.onUserNameClick(message)
            }

            imageView.setOnClickListener {
                callback.onContentClick(message)
            }

            imageView.setOnLongClickListener {
                callback.onContentLongPress(message)
                true
            }

            failureView.setOnClickListener {
                callback.onFailureClick(message)
            }

        }
    }

    override fun update() {
        val imageMessage = message as ImageMessage
        with (itemView) {

            configuration.loadImage(avatarView, imageMessage.user.avatar)
            updateImageSize(avatarView, configuration.userAvatarWidth, configuration.userAvatarHeight, configuration.userAvatarBorderWidth, configuration.userAvatarBorderColor, configuration.userAvatarBorderRadius)

            nameView.text = imageMessage.user.name

            configuration.loadImage(imageView, imageMessage.url)
            updateImageSize(imageView, imageMessage.width, imageMessage.height, configuration.imageMessageBorderWidth, configuration.imageMessageBorderColor, configuration.imageMessageBorderRadius)

            showTimeView(timeView, imageMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}