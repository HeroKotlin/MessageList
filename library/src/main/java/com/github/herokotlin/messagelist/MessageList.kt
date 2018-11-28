package com.github.herokotlin.messagelist

import android.content.Context
import android.graphics.Rect
import android.hardware.SensorManager
import android.media.AudioManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.github.herokotlin.messagelist.model.Message
import com.github.herokotlin.messagelist.util.AudioPlayer
import kotlinx.android.synthetic.main.message_list.view.*

class MessageList : LinearLayout {

    var callback = object: MessageListCallback {}

    var hasMoreMessage = false

        set(value) {
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
        AudioPlayer.destroy()
    }

    private fun init() {
        LayoutInflater.from(context).inflate(R.layout.message_list, this)

        recyclerView.layoutManager = LinearLayoutManager(this.context)
        recyclerView.addItemDecoration(
            MessageDecoration()
        )

        setOnClickListener {
            callback.onListClick()
        }
        refreshLayout.setOnRefreshListener {
            callback.onLoadMore()
        }

        hasMoreMessage = false

        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        AudioPlayer.init(audioManager, sensorManager)
    }

    fun init(configuration: MessageListConfiguration) {
        adapter = MessageListAdapter(configuration, callback)
        recyclerView.adapter = adapter
    }

    fun loadMoreComplete(hasMoreMessage: Boolean) {
        this.hasMoreMessage = hasMoreMessage
        refreshLayout.isRefreshing = false
    }

    fun scrollToBottom(animated: Boolean) {
        val position = recyclerView.adapter.itemCount - 1
        if (animated) {
            recyclerView.smoothScrollToPosition(position)
        }
        else {
            recyclerView.scrollToPosition(position)
        }
    }

    fun append(message: Message) {
        adapter.append(message)
    }

    fun append(messages: List<Message>) {
        adapter.append(messages)
    }

    fun prepend(message: Message) {
        adapter.prepend(message)
    }

    fun prepend(messages: List<Message>) {
        adapter.prepend(messages)
    }

    fun remove(messageId: String) {
        adapter.remove(messageId)
    }

    fun removeAll() {
        adapter.removeAll()
    }

    fun update(message: Message) {
        adapter.update(message)
    }

    inner class MessageDecoration : RecyclerView.ItemDecoration() {

        override fun getItemOffsets(rect: Rect, view: View,
                                    parent: RecyclerView, state: RecyclerView.State?) {

            val index = parent.getChildAdapterPosition(view)

            if (index == 0) {
                rect.top = messageListPaddingVertical
            }
            else {
                rect.top = messageItemMarginTop
            }

            if (index == adapter.itemCount - 1) {
                rect.bottom = messageListPaddingVertical
            }
            else {
                rect.bottom = 0
            }

        }
    }

}
