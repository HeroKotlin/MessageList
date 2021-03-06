package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.PostMessage
import kotlinx.android.synthetic.main.message_post_left.view.*

internal class PostMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

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

            bubbleView.layoutParams.width = dp2px(configuration.postMessageBubbleWidth)

            avatarView.setOnClickListener(onUserAvatarClick)

            bubbleView.setOnClickListener(onContentClick)
            bubbleView.setOnLongClickListener(onContentLongPress)

            failureView.setOnClickListener(onFailureClick)

            updateImageSize(avatarView,
                configuration.userAvatarWidth,
                configuration.userAvatarHeight,
                configuration.userAvatarBorderWidth,
                configuration.userAvatarBorderColor,
                configuration.userAvatarBorderRadius,
                configuration.userAvatarBgColor
            )

            updateImageSize(thumbnailView,
                configuration.postMessageThumbnailWidth,
                configuration.postMessageThumbnailHeight,
                0f, 0,
                configuration.postMessageThumbnailBorderRadius,
                configuration.postMessageThumbnailBgColor
            )

        }

    }

    override fun update() {

        val postMessage = message as PostMessage

        with (itemView) {

            configuration.loadImage(
                avatarView,
                message.user.avatar,
                configuration.userAvatarWidth,
                configuration.userAvatarHeight
            )

            configuration.loadImage(
                thumbnailView,
                postMessage.thumbnail,
                configuration.postMessageThumbnailWidth,
                configuration.postMessageThumbnailHeight
            )

            nameView.text = postMessage.user.name

            titleView.text = postMessage.title
            descView.text = postMessage.desc
            labelView.text = postMessage.label

            showTimeView(timeView, postMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}