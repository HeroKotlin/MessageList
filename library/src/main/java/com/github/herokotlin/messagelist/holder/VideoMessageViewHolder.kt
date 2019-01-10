package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.MenuItem
import com.github.herokotlin.messagelist.model.VideoMessage
import kotlinx.android.synthetic.main.message_video_left.view.*

internal class VideoMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

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

            playView.setOnClickListener(onContentClick)
            playView.setOnLongClickListener(onContentLongPress)

            failureView.setOnClickListener(onFailureClick)

        }
    }

    override fun update() {

        val videoMessage = message as VideoMessage

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

            nameView.text = videoMessage.user.name

            durationView.text = formatDuration(videoMessage.duration)

            showTimeView(timeView, videoMessage.time)
            showStatusView(spinnerView, failureView)

            configuration.loadImage(
                thumbnailView,
                videoMessage.thumbnail,
                videoMessage.width,
                videoMessage.height
            )

            updateImageSize(thumbnailView,
                videoMessage.width,
                videoMessage.height,
                configuration.videoMessageBorderWidth,
                configuration.videoMessageBorderColor,
                configuration.videoMessageBorderRadius,
                configuration.videoMessageBgColor
            )

        }
    }

    override fun createMenuItems(): List<MenuItem> {
        val items = mutableListOf<MenuItem>()
        if (message.canShare) {
            items.add(
                MenuItem(configuration.menuItemShare) {
                    callback.onShareClick(message)
                }
            )
        }
        if (message.canRecall) {
            items.add(
                MenuItem(configuration.menuItemRecall) {
                    callback.onRecallClick(message)
                }
            )
        }
        if (message.canDelete) {
            items.add(
                MenuItem(configuration.menuItemDelete) {
                    callback.onDeleteClick(message)
                }
            )
        }
        return items
    }

    private fun formatDuration(duration: Int): String {

        val MINUTE = 60
        val HOUR = MINUTE * 60

        var seconds = duration
        val hours = seconds / HOUR

        seconds -= hours * HOUR

        val minutes = seconds / MINUTE

        seconds -= minutes * MINUTE

        var result = lpad(minutes) + ":" + lpad(seconds)

        if (hours > 0) {
            result = lpad(hours) + ":" + result
        }

        return result

    }

    private fun lpad(value: Int): String {
        if (value > 9) {
            return value.toString()
        }
        return "0$value"
    }

}