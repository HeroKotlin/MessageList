package com.github.herokotlin.messagelist.holder

import android.graphics.drawable.AnimationDrawable
import android.view.View
import com.github.herokotlin.messagelist.enum.MessageStatus
import com.github.herokotlin.messagelist.model.AudioMessage
import com.github.herokotlin.messagelist.util.AudioPlayer
import com.github.herokotlin.messagelist.util.AudioPlayerCallback
import kotlinx.android.synthetic.main.message_audio_left.view.*

class AudioMessageViewHolder(view: View, val isRightMessage: Boolean): MessageViewHolder(view) {

    var url = ""

    override fun create() {
        with (itemView) {

            val isUserNameVisible = configuration.leftUserNameVisible && !isRightMessage || configuration.rightUserNameVisible && isRightMessage

            if (isUserNameVisible) {
                nameView.setOnClickListener {
                    message?.let {
                        callback.onUserNameClick(it)
                    }
                }
            }
            else {
                nameView.visibility = View.GONE
            }

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
                    if (AudioPlayer.isPlaying(url)) {
                        AudioPlayer.stop()
                    }
                    else {
                        AudioPlayer.play(url)
                    }
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

        AudioPlayer.addListener(object : AudioPlayerCallback {
            override fun onLoad(url: String) {
                showLoading()
            }

            override fun onPlay(url: String) {
                if (url == this@AudioMessageViewHolder.url) {
                    hideLoading()
                    playAnimation()
                }
            }

            override fun onStop(url: String) {
                if (url == this@AudioMessageViewHolder.url) {
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

            url = audioMessage.url

            if (AudioPlayer.isPlaying(url)) {
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