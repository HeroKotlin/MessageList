package com.github.herokotlin.messagelist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.github.herokotlin.messagelist.holder.*
import com.github.herokotlin.messagelist.model.*

class MessageListAdapter(private val configuration: MessageListConfiguration, private val callback: MessageListCallback): RecyclerView.Adapter<MessageViewHolder>() {

    companion object {

        private const val MESSAGE_TYPE_TEXT_LEFT = 1

        private const val MESSAGE_TYPE_TEXT_RIGHT = 2

        private const val MESSAGE_TYPE_IMAGE_LEFT = 3

        private const val MESSAGE_TYPE_IMAGE_RIGHT = 4

        private const val MESSAGE_TYPE_AUDIO_LEFT = 5

        private const val MESSAGE_TYPE_AUDIO_RIGHT = 6

        private const val MESSAGE_TYPE_VIDEO_LEFT = 7

        private const val MESSAGE_TYPE_VIDEO_RIGHT = 8

        private const val MESSAGE_TYPE_CARD_LEFT = 9

        private const val MESSAGE_TYPE_CARD_RIGHT = 10

        private const val MESSAGE_TYPE_POST_LEFT = 11

        private const val MESSAGE_TYPE_POST_RIGHT = 12

        private const val MESSAGE_TYPE_EVENT = 13

    }

    private var messageList = mutableListOf<Message>()

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(configuration, callback, messageList[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        when (viewType) {
            MESSAGE_TYPE_TEXT_LEFT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_text_left, parent, false)
                return TextMessageViewHolder(view, false)
            }
            MESSAGE_TYPE_TEXT_RIGHT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_text_right, parent, false)
                return TextMessageViewHolder(view, true)
            }
            MESSAGE_TYPE_IMAGE_LEFT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_image_left, parent, false)
                return ImageMessageViewHolder(view, false)
            }
            MESSAGE_TYPE_IMAGE_RIGHT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_image_right, parent, false)
                return ImageMessageViewHolder(view, true)
            }
            MESSAGE_TYPE_AUDIO_LEFT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_audio_left, parent, false)
                return AudioMessageViewHolder(view, false)
            }
            MESSAGE_TYPE_AUDIO_RIGHT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_audio_right, parent, false)
                return AudioMessageViewHolder(view, true)
            }
            MESSAGE_TYPE_VIDEO_LEFT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_video_left, parent, false)
                return VideoMessageViewHolder(view, false)
            }
            MESSAGE_TYPE_VIDEO_RIGHT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_video_right, parent, false)
                return VideoMessageViewHolder(view, true)
            }
            MESSAGE_TYPE_CARD_LEFT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_card_left, parent, false)
                return CardMessageViewHolder(view, false)
            }
            MESSAGE_TYPE_CARD_RIGHT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_card_right, parent, false)
                return CardMessageViewHolder(view, true)
            }
            MESSAGE_TYPE_POST_LEFT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_post_left, parent, false)
                return PostMessageViewHolder(view, false)
            }
            MESSAGE_TYPE_POST_RIGHT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_post_right, parent, false)
                return PostMessageViewHolder(view, true)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.message_event, parent, false)
                return EventMessageViewHolder(view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        val isRight = configuration.isRightMessage(message)
        when (message) {
            is TextMessage -> {
                return if (isRight) MESSAGE_TYPE_TEXT_RIGHT else MESSAGE_TYPE_TEXT_LEFT
            }
            is ImageMessage -> {
                return if (isRight) MESSAGE_TYPE_IMAGE_RIGHT else MESSAGE_TYPE_IMAGE_LEFT
            }
            is AudioMessage -> {
                return if (isRight) MESSAGE_TYPE_AUDIO_RIGHT else MESSAGE_TYPE_AUDIO_LEFT
            }
            is VideoMessage -> {
                return if (isRight) MESSAGE_TYPE_VIDEO_RIGHT else MESSAGE_TYPE_VIDEO_LEFT
            }
            is CardMessage -> {
                return if (isRight) MESSAGE_TYPE_CARD_RIGHT else MESSAGE_TYPE_CARD_LEFT
            }
            is PostMessage -> {
                return if (isRight) MESSAGE_TYPE_POST_RIGHT else MESSAGE_TYPE_POST_LEFT
            }
            else -> {
                return MESSAGE_TYPE_EVENT
            }
        }
    }

    fun append(messages: List<Message>) {
        messageList.addAll(itemCount, messages)
        notifyDataSetChanged()
    }

    fun prepend(messages: List<Message>) {
        messageList.addAll(0, messages)
        notifyDataSetChanged()
    }

    fun remove(messageId: String) {
        var messageIndex = -1
        messageList.forEachIndexed { index, message ->
            if (messageId == message.id) {
                messageIndex = index
            }
        }
        if (messageIndex >= 0) {
            messageList.removeAt(messageIndex)
            notifyItemRemoved(messageIndex)
        }
    }

    fun removeAll() {
        val count = messageList.count()
        if (count > 0) {
            messageList.removeAll { true }
            notifyDataSetChanged()
        }
    }

    fun update(messageId: String, message: Message) {
        var messageIndex = -1
        messageList.forEachIndexed { index, item ->
            if (messageId == item.id) {
                messageIndex = index
            }
        }
        if (messageIndex >= 0) {
            messageList[messageIndex] = message
            notifyItemChanged(messageIndex)
        }
    }

}

