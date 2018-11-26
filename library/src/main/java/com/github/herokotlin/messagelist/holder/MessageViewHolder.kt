package com.github.herokotlin.messagelist.holder

import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import com.github.herokotlin.messagelist.MessageListCallback
import com.github.herokotlin.messagelist.MessageListConfiguration
import com.github.herokotlin.messagelist.enum.MessageStatus
import com.github.herokotlin.messagelist.model.LinkToken
import com.github.herokotlin.messagelist.model.Message
import com.github.herokotlin.messagelist.view.LinkSpan
import com.github.herokotlin.messagelist.view.RoundImageView
import java.util.regex.Pattern

abstract class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {

    companion object {
        val LINK_PATTERN = Pattern.compile("\\[\\w+:[^]]+\\]")
    }

    private var isReady = false

    var message: Message? = null

    lateinit var configuration: MessageListConfiguration
    lateinit var callback: MessageListCallback

    open fun bind(configuration: MessageListConfiguration, callback: MessageListCallback, message: Message) {

        this.message = message

        if (!isReady) {
            isReady = true

            this.configuration = configuration
            this.callback = callback

            create()
        }

        update()

    }

    protected fun showTimeView(timeView: TextView, time: String) {

        if (time.isBlank()) {
            timeView.visibility = View.GONE
        }
        else {
            timeView.text = time
            timeView.visibility = View.VISIBLE
        }

    }

    protected fun showStatusView(spinnerView: View, failureView: View) {
        when (message?.status) {
            MessageStatus.SEND_ING -> {
                spinnerView.visibility = View.VISIBLE
                failureView.visibility = View.GONE
            }
            MessageStatus.SEND_FAILURE -> {
                failureView.visibility = View.VISIBLE
                spinnerView.visibility = View.GONE
            }
            else -> {
                spinnerView.visibility = View.GONE
                failureView.visibility = View.GONE
            }
        }
    }

    protected fun dp2px(value: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, itemView.resources.displayMetrics)
    }

    protected fun dp2px(value: Int): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), itemView.resources.displayMetrics).toInt()
    }

    protected fun setLinkClickListener(textView: TextView) {

        textView.setOnTouchListener { view, event ->

            val action = event.action

            val text = textView.text

            if (text is SpannableString && action == MotionEvent.ACTION_UP) {

                var x = event.x.toInt()
                var y = event.y.toInt()

                x -= textView.totalPaddingLeft
                y -= textView.totalPaddingTop

                x += textView.scrollX
                y += textView.scrollY

                val layout = textView.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())

                var link = text.getSpans(off, off, ClickableSpan::class.java)
                if (link.isNotEmpty()) {
                    link[0].onClick(textView)
                    true
                }
            }

            false
        }

    }

    protected fun formatLinks(text: String, linkColor: Int): SpannableString {

        val links = mutableListOf<LinkToken>()
        var index = 0

        var newString = ""

        val matcher = LINK_PATTERN.matcher(text)
        if (matcher != null) {
            while (matcher.find()) {

                val group = matcher.group().toString()
                val start = matcher.start()
                val end = matcher.end()

                newString += text.substring(index, start)

                // 去掉左右 [ ] 后的链接
                val link = group.substring(1, group.length - 1)

                // 文本
                val subText = link.substring(link.indexOf(":") + 1)

                links.add(
                    LinkToken(subText, link, newString.length)
                )

                newString += subText

                index = end

            }
        }

        if (index < text.length) {
            newString += text.substring(index)
        }

        val spannable = SpannableString(newString)

        for (item in links) {
            val start = item.position
            val end = item.position + item.text.length
            spannable.setSpan(LinkSpan(item.link, callback), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(ForegroundColorSpan(linkColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return spannable

    }

    protected fun updateImageSize(imageView: RoundImageView, width: Int, height: Int, borderWidth: Float, borderColor: Int, borderRadius: Float) {

        var imageWidth = dp2px(width.toFloat())
        var imageHeight = dp2px(height.toFloat())
        val imageRatio = imageWidth / imageHeight

        val maxWidth = getContentMaxWidth()

        if (imageWidth > maxWidth) {
            imageWidth = maxWidth
            imageHeight = imageWidth / imageRatio
        }

        val avatarHeight = dp2px(configuration.userAvatarHeight.toFloat())
        if (imageHeight < avatarHeight) {
            imageHeight = avatarHeight
            imageWidth = imageHeight * imageRatio
        }

        imageView.setImageInfo(imageWidth, imageHeight, borderWidth, borderColor, dp2px(borderRadius))

    }

    protected fun getContentMaxWidth(): Float {

        val screenWidth = itemView.resources.displayMetrics.widthPixels

        return screenWidth - 2 * (configuration.messagePaddingHorizontal + dp2px(configuration.userAvatarWidth.toFloat())) - configuration.leftUserNameMarginLeft - configuration.rightUserNameMarginRight

    }

    abstract fun create()

    abstract fun update()

}