package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.CardMessage
import com.github.herokotlin.messagelist.model.MenuItem
import kotlinx.android.synthetic.main.message_card_left.view.*

internal class CardMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

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

            bubbleView.layoutParams.width = dp2px(configuration.cardMessageBubbleWidth)

            avatarView.setOnClickListener(onUserAvatarClick)

            bubbleView.setOnClickListener(onContentClick)
            bubbleView.setOnLongClickListener(onContentLongPress)

            failureView.setOnClickListener(onFailureClick)

        }

    }

    override fun update() {

        val cardMessage = message as CardMessage

        with (itemView) {

            configuration.loadImage(
                avatarView,
                message.user.avatar,
                configuration.userAvatarWidth,
                configuration.userAvatarHeight
            )

            configuration.loadImage(
                thumbnailView,
                cardMessage.thumbnail,
                configuration.cardMessageThumbnailWidth,
                configuration.cardMessageThumbnailHeight
            )

            updateImageSize(avatarView,
                configuration.userAvatarWidth,
                configuration.userAvatarHeight,
                configuration.userAvatarBorderWidth,
                configuration.userAvatarBorderColor,
                configuration.userAvatarBorderRadius,
                configuration.userAvatarBgColor
            )

            updateImageSize(thumbnailView,
                configuration.cardMessageThumbnailWidth,
                configuration.cardMessageThumbnailHeight,
                0f, 0,
                configuration.cardMessageThumbnailBorderRadius,
                configuration.cardMessageThumbnailBgColor
            )

            nameView.text = cardMessage.user.name

            titleView.text = cardMessage.title
            descView.text = cardMessage.desc
            labelView.text = cardMessage.label

            showTimeView(timeView, cardMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}