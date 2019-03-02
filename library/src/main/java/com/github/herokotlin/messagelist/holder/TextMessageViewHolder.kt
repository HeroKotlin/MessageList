package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.TextMessage
import com.github.herokotlin.messagelist.view.LinkMovementMethod
import kotlinx.android.synthetic.main.message_text_left.view.*

internal class TextMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    override val onLinkLongPress: (String) -> Unit = {
        onContentLongPress(itemView.bubbleView)
    }

    override fun create() {
        with (itemView) {

            val contentMaxWidth = getContentMaxWidth().toInt()
            val isUserNameVisible = configuration.leftUserNameVisible && !isRightMessage || configuration.rightUserNameVisible && isRightMessage

            if (isUserNameVisible) {
                nameView.maxWidth = contentMaxWidth
                nameView.setOnClickListener(onUserNameClick)
            }
            else {
                nameView.visibility = View.GONE
            }

            textView.maxWidth = contentMaxWidth
            textView.movementMethod = LinkMovementMethod
            textView.isClickable = false
            textView.isFocusable = false
            textView.isLongClickable = false

            avatarView.setOnClickListener(onUserAvatarClick)

            bubbleView.setOnClickListener(onContentClick)
            bubbleView.setOnLongClickListener(onContentLongPress)

            failureView.setOnClickListener(onFailureClick)

        }
    }

    override fun update() {
        val textMessage = message as TextMessage
        with (itemView) {

            configuration.loadImage(
                avatarView,
                message.user.avatar,
                configuration.userAvatarWidth,
                configuration.userAvatarHeight
            )

            updateImageSize(avatarView,
                configuration.userAvatarWidth,
                configuration.userAvatarHeight,
                configuration.userAvatarBorderWidth,
                configuration.userAvatarBorderColor,
                configuration.userAvatarBorderRadius,
                configuration.userAvatarBgColor
            )

            val spannable = formatLinks(
                textMessage.text,
                configuration.linkTextColor,
                onLinkClick,
                onLinkLongPress
            )
            configuration.formatText(textView, spannable)

            textView.text = spannable
            nameView.text = textMessage.user.name

            showTimeView(timeView, textMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}