package com.github.herokotlin.messagelist.holder

import android.graphics.drawable.AnimationDrawable
import android.view.View
import com.github.herokotlin.messagelist.enum.MessageStatus
import com.github.herokotlin.messagelist.model.AudioMessage
import com.github.herokotlin.messagelist.util.AudioPlayer
import com.github.herokotlin.messagelist.util.AudioPlayerCallback
import kotlinx.android.synthetic.main.message_audio_left.view.*

class AudioMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

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

            setOnClickListener {
                callback.onListClick()
            }

            avatarView.setOnClickListener {
                callback.onUserAvatarClick(message)
            }

            bubbleView.setOnClickListener {
                if (AudioPlayer.isPlaying(message.id)) {
                    AudioPlayer.stop()
                }
                else {
                    val audioMessage = message as AudioMessage
                    AudioPlayer.play(audioMessage.id, audioMessage.url)
                }
            }

            bubbleView.setOnLongClickListener {
                callback.onContentLongPress(message)
                true
            }

            failureView.setOnClickListener {
                callback.onFailureClick(message)
            }

        }

        AudioPlayer.addListener(object : AudioPlayerCallback {
            override fun onLoad(id: String) {
                showLoading()
            }

            override fun onPlay(id: String) {
                if (id == message.id) {
                    hideLoading()
                    playAnimation()
                }
            }

            override fun onStop(id: String) {
                if (id == message.id) {
                    hideLoading()
                    stopAnimation()
                }
            }
        })

    }

    override fun update() {

        val audioMessage = message as AudioMessage

        with (itemView) {

            configuration.loadImage(avatarView, audioMessage.user.avatar)

            updateImageSize(avatarView, configuration.userAvatarWidth, configuration.userAvatarHeight, configuration.userAvatarBorderWidth, configuration.userAvatarBorderColor, configuration.userAvatarBorderRadius)

            nameView.text = audioMessage.user.name

            updateContentSize(audioMessage.duration)

            if (audioMessage.status == MessageStatus.SEND_SUCCESS) {
                durationView.text = audioMessage.duration.toString()

                durationView.visibility = View.VISIBLE
                unitView.visibility = View.VISIBLE
            }
            else {
                durationView.visibility = View.GONE
                unitView.visibility = View.GONE
            }

            showTimeView(timeView, audioMessage.time)
            showStatusView(spinnerView, failureView)

            if (AudioPlayer.isPlaying(message.id)) {
                playAnimation()
            }
            else {
                stopAnimation()
            }
        }
    }

    private fun updateContentSize(duration: Int) {

        val durationRatio = duration.toFloat() / configuration.audioMessageMaxDuration

        val maxWidth = configuration.audioMessageMaxRatio * getContentMaxWidth()
        val minWidth = dp2px(configuration.audioMessageBubbleMinWidth)

        var bubbleWidth = maxWidth * durationRatio

        if (bubbleWidth > maxWidth) {
            bubbleWidth = maxWidth
        }
        else if (bubbleWidth < minWidth) {
            bubbleWidth = minWidth
        }

        itemView.bubbleView.layoutParams.width = bubbleWidth.toInt()

    }


    private fun playAnimation() {
        val animation = itemView.animationView.drawable as AnimationDrawable
        animation.start()
    }

    private fun stopAnimation() {
        val animation = itemView.animationView.drawable as AnimationDrawable
        animation.stop()
        animation.selectDrawable(0)
    }

    private fun showLoading() {
        with (itemView) {
            loadingView.visibility = View.VISIBLE
            animationView.visibility = View.GONE
        }
    }

    private fun hideLoading() {
        with (itemView) {
            loadingView.visibility = View.GONE
            animationView.visibility = View.VISIBLE
        }
    }

}