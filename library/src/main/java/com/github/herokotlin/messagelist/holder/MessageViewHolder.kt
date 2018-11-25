package com.github.herokotlin.messagelist.holder

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import com.github.herokotlin.messagelist.MessageListCallback
import com.github.herokotlin.messagelist.MessageListConfiguration
import com.github.herokotlin.messagelist.enum.MessageStatus
import com.github.herokotlin.messagelist.model.Message
import com.github.herokotlin.messagelist.view.RoundImageView

abstract class MessageViewHolder(view: View): RecyclerView.ViewHolder(view) {

    var isReady = false

    var message: Message? = null


    open fun bind(configuration: MessageListConfiguration, callback: MessageListCallback, message: Message) {

        this.message = message

        if (!isReady) {
            isReady = true
            create(configuration, callback)
        }

        update(configuration)

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

    protected fun updateImageSize(configuration: MessageListConfiguration, imageView: RoundImageView, width: Int, height: Int, borderWidth: Float, borderColor: Int, borderRadius: Float) {

        var imageWidth = dp2px(width.toFloat())
        var imageHeight = dp2px(height.toFloat())
        val imageRatio = imageWidth / imageHeight

        val maxWidth = getContentMaxWidth(configuration)

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

    protected fun getContentMaxWidth(configuration: MessageListConfiguration): Float {

        val screenWidth = itemView.resources.displayMetrics.widthPixels

        return screenWidth - 2 * (configuration.messagePaddingHorizontal + dp2px(configuration.userAvatarWidth.toFloat())) - configuration.leftUserNameMarginLeft - configuration.rightUserNameMarginRight

    }

    abstract fun create(configuration: MessageListConfiguration, callback: MessageListCallback)

    abstract fun update(configuration: MessageListConfiguration)

}