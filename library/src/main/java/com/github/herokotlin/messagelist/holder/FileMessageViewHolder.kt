package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.enum.FileIcon
import com.github.herokotlin.messagelist.model.FileMessage
import kotlinx.android.synthetic.main.message_file_left.view.*
import com.github.herokotlin.messagelist.R

internal class FileMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

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

            bubbleView.layoutParams.width = dp2px(configuration.fileMessageBubbleWidth)

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

            updateImageSize(iconView,
                configuration.fileMessageIconWidth,
                configuration.fileMessageIconHeight,
                0f, 0,
                configuration.fileMessageIconBorderRadius,
                configuration.fileMessageIconBgColor
            )

        }

    }

    override fun update() {

        val fileMessage = message as FileMessage

        with (itemView) {

            configuration.loadImage(
                avatarView,
                message.user.avatar,
                configuration.userAvatarWidth,
                configuration.userAvatarHeight
            )

            iconView.setImageResource(
                when (fileMessage.icon) {
                    FileIcon.WORD -> {
                        R.drawable.message_list_file_word
                    }
                    FileIcon.EXCEL -> {
                        R.drawable.message_list_file_excel
                    }
                    FileIcon.PPT -> {
                        R.drawable.message_list_file_ppt
                    }
                    FileIcon.PDF -> {
                        R.drawable.message_list_file_pdf
                    }
                    else -> {
                        R.drawable.message_list_file_txt
                    }
                }
            )

            nameView.text = fileMessage.user.name

            titleView.text = fileMessage.title
            descView.text = fileMessage.desc

            showTimeView(timeView, fileMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}