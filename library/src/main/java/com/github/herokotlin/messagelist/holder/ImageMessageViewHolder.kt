package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.ImageMessage
import com.github.herokotlin.messagelist.model.MenuItem
import kotlinx.android.synthetic.main.message_image_left.view.*

internal class ImageMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    override val menuItems: List<MenuItem> by lazy {
        createMenuItems(
            MenuItem(configuration.menuItemShare) {
                callback.onShareClick(message)
            }
        )
    }

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