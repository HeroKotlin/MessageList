package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.TextMessage
import com.github.herokotlin.messagelist.view.linkMovementMethod
import kotlinx.android.synthetic.main.message_text_left.view.*

class TextMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    private var clickOnLink = false

    private val onLinkClick = { link: String ->
        clickOnLink = true
        callback.onLinkClick(link)
    }

    override fun create() {
        with (itemView) {

            val contentMaxWidth = getContentMaxWidth().toInt()
            val isUserNameVisible = configuration.leftUserNameVisible && !isRightMessage || configuration.rightUserNameVisible && isRightMessage

            if (isUserNameVisible) {
                nameView.maxWidth = contentMaxWidth
                nameView.setOnClickListener {
                    callback.onUserNameClick(message)
                }
            }
            else {
                nameView.visibility = View.GONE
            }

            textView.maxWidth = contentMaxWidth
            textView.movementMethod = linkMovementMethod

            avatarView.setOnClickListener {
                callback.onUserAvatarClick(message)
            }

            nameView.setOnClickListener {
                callback.onUserNameClick(message)
            }

            textView.setOnClickListener {
                if (clickOnLink) {
                    clickOnLink = false
                }
                else {
                    callback.onContentClick(message)
                }
            }

            textView.setOnLongClickListener {
                callback.onContentLongPress(message)
                true
            }

            failureView.setOnClickListener {
                callback.onFailureClick(message)
            }

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