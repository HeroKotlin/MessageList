package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.PostMessage
import kotlinx.android.synthetic.main.message_post_left.view.*

class PostMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    override fun create() {
        with (itemView) {

            val isUserNameVisible = configuration.leftUserNameVisible && !isRightMessage || configuration.rightUserNameVisible && isRightMessage

            if (isUserNameVisible) {
                nameView.maxWidth = getContentMaxWidth().toInt()
                nameView.setOnClickListener {
                    message?.let {
                        callback.onUserNameClick(it)
                    }
                }
            }
            else {
                nameView.visibility = View.GONE
            }

            bubbleView.layoutParams.width = dp2px(configuration.postMessageBubbleWidth)

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

    override fun update() {

        val postMessage = message as PostMessage

        with (itemView) {

            configuration.loadImage(avatarView, postMessage.user.avatar)
            configuration.loadImage(thumbnailView, postMessage.thumbnail)

            updateImageSize(avatarView, configuration.userAvatarWidth, configuration.userAvatarHeight, configuration.userAvatarBorderWidth, configuration.userAvatarBorderColor, configuration.userAvatarBorderRadius)
            updateImageSize(thumbnailView, configuration.postMessageThumbnailWidth, configuration.postMessageThumbnailHeight, 0f, 0, configuration.postMessageThumbnailBorderRadius)

            nameView.text = postMessage.user.name

            titleView.text = postMessage.title
            descView.text = postMessage.desc
            brandView.text = postMessage.brand

            showTimeView(timeView, postMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}