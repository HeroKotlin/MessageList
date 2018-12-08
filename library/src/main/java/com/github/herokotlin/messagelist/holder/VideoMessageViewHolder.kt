package com.github.herokotlin.messagelist.holder

import android.view.View
import com.github.herokotlin.messagelist.model.VideoMessage
import kotlinx.android.synthetic.main.message_video_left.view.*

class VideoMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    override fun create() {
        with (itemView) {

            val isUserNameVisible = configuration.leftUserNameVisible && !isRightMessage || configuration.rightUserNameVisible && isRightMessage

            if (isUserNameVisible) {
                nameView.maxWidth = getContentMaxWidth().toInt()
                nameView.setOnClickListener {
                    callback.onUserNameClick(message)
                }
            }
            else {
                nameView.visibility = View.GONE
            }

            avatarView.setOnClickListener {
                callback.onUserAvatarClick(message)
            }

            nameView.setOnClickListener {
                callback.onUserNameClick(message)
            }

            playView.setOnClickListener {
                callback.onContentClick(message)
            }

            playView.setOnLongClickListener {
                callback.onContentLongPress(message)
                true
            }

            failureView.setOnClickListener {
                callback.onFailureClick(message)
            }

        }
    }

    override fun update() {

        val videoMessage = message as VideoMessage

        with (itemView) {

            configuration.loadImage(avatarView, videoMessage.user.avatar)
            updateImageSize(avatarView, configuration.userAvatarWidth, configuration.userAvatarHeight, configuration.userAvatarBorderWidth, configuration.userAvatarBorderColor, configuration.userAvatarBorderRadius)

            nameView.text = videoMessage.user.name

            durationView.text = formatDuration(videoMessage.duration)

            showTimeView(timeView, videoMessage.time)
            showStatusView(spinnerView, failureView)

            configuration.loadImage(thumbnailView, videoMessage.thumbnail)
            updateImageSize(thumbnailView, videoMessage.width, videoMessage.height, configuration.videoMessageBorderWidth, configuration.videoMessageBorderColor, configuration.videoMessageBorderRadius)

        }
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