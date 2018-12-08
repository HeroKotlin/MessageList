package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.MenuItem
import com.github.herokotlin.messagelist.model.TextMessage
import com.github.herokotlin.messagelist.view.linkMovementMethod
import kotlinx.android.synthetic.main.message_text_left.view.*

class TextMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    private var clickOnLink = false

    private val onLinkClick = { link: String ->
        clickOnLink = true
        callback.onLinkClick(link)
    }

    override var onContentClick = { _: View? ->
        if (clickOnLink) {
            clickOnLink = false
        }
        else {
            callback.onContentClick(message)
        }
    }

    override fun create() {

        menuItems.add(
            MenuItem(configuration.menuItemCopy) {
                callback.onCopyClick(message)
            }
        )

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
            textView.movementMethod = linkMovementMethod

            avatarView.setOnClickListener(onUserAvatarClick)

            textView.setOnClickListener(onContentClick)

            textView.setOnLongClickListener(onContentLongPress)

            failureView.setOnClickListener(onFailureClick)

        }
    }

    override fun update() {
        val textMessage = message as TextMessage
        with (itemView) {

            configuration.loadImage(avatarView, textMessage.user.avatar)
            updateImageSize(avatarView, configuration.userAvatarWidth, configuration.userAvatarHeight, configuration.userAvatarBorderWidth, configuration.userAvatarBorderColor, configuration.userAvatarBorderRadius)

            val spannable = formatLinks(textMessage.text, configuration.linkTextColor, onLinkClick)
            configuration.formatText(textView, spannable)
            textView.text = spannable

            nameView.text = textMessage.user.name

            showTimeView(timeView, textMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}