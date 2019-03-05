package com.github.herokotlin.messagelist

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.github.herokotlin.messagelist.model.Message
import kotlinx.android.synthetic.main.message_list.view.*

class MessageList : LinearLayout {

    lateinit var callback: MessageListCallback

    lateinit var configuration: MessageListConfiguration

    var hasMoreMessage = true

        set(value) {
            if (field == value) {
                return
            }
            field = value
            refreshLayout.isEnabled = value
        }

    private lateinit var adapter: MessageListAdapter

    private val messageListPaddingVertical: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.message_list_padding_vertical)
    }

    private val messageItemMarginTop: Int by lazy {
        resources.getDimensionPixelSize(R.dimen.message_list_message_margin_top)
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        configuration.audioPlayer.destroy()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.message_list, this)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.addItemDecoration(
            MessageDecoration()
        )

        recyclerView.setOnTouchListener { _, event ->
            if (event.actionMasked == MotionEvent.ACTION_UP) {
                callback.onListClick()
            }
            false
        }
        refreshLayout.setOnRefreshListener {
            callback.onLoadMore()
        }

        hasMoreMessage = false

    }

    fun init(configuration: MessageListConfiguration, callback: MessageListCallback) {

        configuration.audioPlayer.init(context, configuration.audioMessageMaxDuration)

        configuration.audioPlayer.onPlay = {
            callback.onUseAudio()
        }

        this.configuration = configuration
        this.callback = callback

        adapter = MessageListAdapter(configuration, callback)
        recyclerView.adapter = adapter

    }

    fun stopAudio() {
        configuration.audioPlayer.stop()
    }

    fun loadMoreComplete(hasMoreMessage: Boolean) {
        this.hasMoreMessage = hasMoreMessage
        refreshLayout.isRefreshing = false
    }

    fun scrollToBottom(animated: Boolean) {
        val position = adapter.itemCount
        if (position > 0) {
            if (animated) {
                recyclerView.smoothScrollToPosition(position - 1)
            }
            else {
                recyclerView.scrollToPosition(position - 1)
            }
        }
    }

    fun append(message: Message) {
        adapter.append(listOf(message))
    }

    fun append(messages: List<Message>) {
        if (messages.isNotEmpty()) {
            adapter.append(messages)
        }
    }

    fun prepend(message: Message) {
        adapter.prepend(listOf(message))
    }

    fun prepend(messages: List<Message>) {
        if (messages.isNotEmpty()) {
            adapter.prepend(messages)
        }
    }

    fun remove(messageId: String) {
        adapter.remove(messageId)
    }

    fun removeAll() {
        adapter.removeAll()
    }

    fun update(messageId: String, message: Message) {
        adapter.update(messageId, message)
    }

    /**
     * 确保音频可用，通常是外部要用音频了
     */
    fun ensureAudioAvailable() {
        stopAudio()
    }

    inner class MessageDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

            val index = parent.getChildAdapterPosition(view)

            if (index == 0) {
                outRect.top = messageListPaddingVertical
            }
            else {
                outRect.top = messageItemMarginTop
            }

            if (index == adapter.itemCount - 1) {
                outRect.bottom = messageListPaddingVertical
            }
            else {
                outRect.bottom = 0
            }

        }

    }

}
