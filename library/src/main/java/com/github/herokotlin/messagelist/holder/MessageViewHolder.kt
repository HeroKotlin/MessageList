package com.github.herokotlin.messagelist.holder

import android.support.v7.widget.RecyclerView
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.github.herokotlin.messagelist.R
import com.github.herokotlin.messagelist.MessageListCallback
import com.github.herokotlin.messagelist.MessageListConfiguration
import com.github.herokotlin.messagelist.enum.MessageStatus
import com.github.herokotlin.messagelist.model.LinkToken
import com.github.herokotlin.messagelist.model.MenuItem
import com.github.herokotlin.messagelist.model.Message
import com.github.herokotlin.messagelist.view.LinkSpan
import com.github.herokotlin.messagelist.view.MenuWindow
import com.github.herokotlin.messagelist.view.RoundImageView
import kotlinx.android.synthetic.main.message_list_menu.view.*
import kotlinx.android.synthetic.main.message_list_menu_item.view.*
import java.util.regex.Pattern

internal abstract class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {

    companion object {
        private val LINK_PATTERN = Pattern.compile("\\[[^\\[:]+:[^\\]]+\\]")
    }

    private var isReady = false

    lateinit var message: Message

    lateinit var configuration: MessageListConfiguration
    lateinit var callback: MessageListCallback

    open val onUserAvatarClick = { _: View? ->
        callback.onUserAvatarClick(message)
    }

    open val onUserNameClick = { _: View? ->
        callback.onUserNameClick(message)
    }

    open val onContentClick = { _: View? ->
        callback.onContentClick(message)
    }

    open val onLinkClick: (String) -> Unit = {
        callback.onLinkClick(it)
    }

    open val onLinkLongPress: (String) -> Unit = {
        callback.onLinkClick(it)
    }

    open val onContentLongPress = { view: View? ->
        val menuItems = createMenuItems()
        if (view != null && menuItems.count() > 0) {
            val menu = MenuWindow()

            val contentView = LayoutInflater.from(itemView.context).inflate(R.layout.message_list_menu, null) as LinearLayout

            menuItems.forEachIndexed { index, menuItem ->

                val childView = LayoutInflater.from(itemView.context).inflate(R.layout.message_list_menu_item, null)

                with (childView.buttonView) {
                    text = menuItem.text
                    setOnClickListener {
                        menu.hide()
                        menuItem.onClick()
                    }
                }

                if (index == 0) {
                    childView.dividerView.visibility = View.GONE
                }

                contentView.listView.addView(childView)

            }

            menu.show(view, contentView)

        }
        true
    }

    open val onFailureClick = { _: View? ->
        callback.onFailureClick(message)
    }

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
        when (message.status) {
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

    protected fun formatLinks(text: String, linkColor: Int, onLinkClick: (String) -> Unit, onLinkLongPress: (String) -> Unit): SpannableString {

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

                // 去掉左右 [ ]
                val rawText = group.substring(1, group.length - 1)

                val separatorIndex = rawText.indexOf(":")

                val linkText = rawText.substring(0, separatorIndex)
                val labelText = rawText.substring(separatorIndex + 1)

                links.add(
                    LinkToken(labelText, linkText, newString.length)
                )

                newString += labelText

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
            spannable.setSpan(LinkSpan(item.link, onLinkClick, onLinkLongPress), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannable.setSpan(ForegroundColorSpan(linkColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return spannable

    }

    protected fun updateImageSize(imageView: RoundImageView, width: Int, height: Int, borderWidth: Float, borderColor: Int, borderRadius: Float, bgColor: Int) {

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

        imageView.setImageInfo(imageWidth, imageHeight, borderWidth, borderColor, dp2px(borderRadius), bgColor)

    }

    protected fun getContentMaxWidth(): Float {

        val screenWidth = itemView.resources.displayMetrics.widthPixels

        return screenWidth - 2 * (configuration.messagePaddingHorizontal + dp2px(configuration.userAvatarWidth.toFloat())) - configuration.leftUserNameMarginLeft - configuration.rightUserNameMarginRight

    }

    open fun createMenuItems(): List<MenuItem> {

        val items = mutableListOf<MenuItem>()
        if (message.canCopy) {
            items.add(
                MenuItem(configuration.menuItemCopy) {
                    callback.onCopyClick(message)
                }
            )
        }
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

    abstract fun create()

    abstract fun update()

}