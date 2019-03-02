package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.enum.FileType
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

            val resId: Int
            when (fileMessage.type) {
                FileType.WORD -> {
                    resId = R.drawable.message_list_file_word
                }
                FileType.EXCEL -> {
                    resId = R.drawable.message_list_file_excel
                }
                FileType.PPT -> {
                    resId = R.drawable.message_list_file_ppt
                }
                else -> {
                    resId = R.drawable.message_list_file_pdf
                }
            }

            typeView.setImageResource(resId)

            updateImageSize(avatarView,
                configuration.userAvatarWidth,
                configuration.userAvatarHeight,
                configuration.userAvatarBorderWidth,
                configuration.userAvatarBorderColor,
                configuration.userAvatarBorderRadius,
                configuration.userAvatarBgColor
            )

            updateImageSize(typeView,
                configuration.fileMessageTypeWidth,
                configuration.fileMessageTypeHeight,
                0f, 0,
                configuration.fileMessageTypeBorderRadius,
                configuration.fileMessageTypeBgColor
            )

            nameView.text = fileMessage.user.name

            titleView.text = fileMessage.title
            descView.text = fileMessage.desc

            showTimeView(timeView, fileMessage.time)
            showStatusView(spinnerView, failureView)

        }
    }

}