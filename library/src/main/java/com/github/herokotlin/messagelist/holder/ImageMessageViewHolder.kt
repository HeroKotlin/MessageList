package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.ImageMessage
import kotlinx.android.synthetic.main.message_image_left.view.*

internal class ImageMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    override fun create() {

        with (itemView) {

            val isUserNameVisible = configuration.leftUserNameVisible && !isRightMessage || configuration.rightUserNameVisible && isRightMessage

            if (isUserNameVisible) {
                nameView.maxWidth = getContentMaxWidth().toInt()
                nameView.setOnClickListener(onUserNameClick)
            }
            else {
                nameView.visibility = View.GONE
            }

            avatarView.setOnClickListener(onUserAvatarClick)

            imageView.setOnClickListener(onContentClick)
            imageView.setOnLongClickListener(onContentLongPress)

            failureView.setOnClickListener(onFailureClick)

            updateImageSize(avatarView,
                configuration.userAvatarWidth,
                configuration.userAvatarHeight,
                configuration.userAvatarBorderWidth,
                configuration.userAvatarBorderColor,
                configuration.userAvatarBorderRadius,
                configuration.userAvatarBgColor
            )

        }
    }

    override fun update() {
        val imageMessage = message as ImageMessage
        with (itemView) {

            configuration.loadImage(
                avatarView,
                message.user.avatar,
                configuration.userAvatarWidth,
                configuration.userAvatarHeight
            )

            nameView.text = imageMessage.user.name

            configuration.loadImage(
                imageView,
                imageMessage.url,
                imageMessage.width,
                imageMessage.height
            )

            updateImageSize(imageView,
                imageMessage.width,
                imageMessage.height,
                configuration.imageMessageBorderWidth,
                configuration.imageMessageBorderColor,
                configuration.imageMessageBorderRadius,
                configuration.imageMessageBgColor
            )

            showTimeView(timeView, imageMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}