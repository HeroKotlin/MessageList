package com.github.herokotlin.messagelist

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.widget.ImageView
import android.widget.TextView
import com.github.herokotlin.messagelist.model.Message
import com.github.herokotlin.messagelist.util.AudioPlayer

abstract class MessageListConfiguration(val context: Context) {

    internal var audioPlayer = AudioPlayer()

    /**
     * 消息的水平内间距
     */
    val messagePaddingHorizontal = context.resources.getDimension(R.dimen.message_list_message_padding_horizontal)

    /**
     * 用户头像的宽度
     */
    var userAvatarWidth = 40

    /**
     * 用户头像的高度
     */
    var userAvatarHeight = 40

    /**
     * 用户头像的边框大小
     */
    val userAvatarBorderWidth = context.resources.getDimension(R.dimen.message_list_user_avatar_border_width)

    /**
     * 用户头像的边框颜色
     */
    var userAvatarBorderColor = Color.parseColor("#CCCCCC")

    /**
     * 用户头像的圆角
     */
    var userAvatarBorderRadius = 4f

    /**
     * 用户头像的背景颜色
     */
    var userAvatarBgColor = Color.parseColor("#08000000")

    /**
     * 左侧用户名称是否显示
     */
    var leftUserNameVisible = true

    /**
     * 左侧用户名称与头像的距离
     */
    val leftUserNameMarginLeft = context.resources.getDimension(R.dimen.message_list_left_user_name_margin_left)

    /**
     * 右侧用户名称是否显示
     */
    var rightUserNameVisible = true

    /**
     * 右侧用户名称与头像的距离
     */
    val rightUserNameMarginRight = context.resources.getDimension(R.dimen.message_list_right_user_name_margin_right)

    /**
     * 语音消息气泡的最小宽度
     */
    var audioMessageBubbleMinWidth = 80f

    /**
     * 语音消息的最大时长
     */
    var audioMessageMaxDuration = 60

    /**
     * 语音消息占据内容宽度的最大比例
     */
    var audioMessageMaxRatio = 4f / 5f

    /**
     * 图片边框大小
     */
    val imageMessageBorderWidth = context.resources.getDimension(R.dimen.message_list_image_message_border_width)

    /**
     * 图片边框颜色
     */
    var imageMessageBorderColor = Color.parseColor("#88CCCCCC")

    /**
     * 图片圆角
     */
    var imageMessageBorderRadius = 4f

    /**
     * 图片背景色
     */
    var imageMessageBgColor = Color.parseColor("#08000000")

    /**
     * 视频缩略图边框大小
     */
    val videoMessageBorderWidth = context.resources.getDimension(R.dimen.message_list_video_message_border_width)

    /**
     * 视频缩略图边框颜色
     */
    var videoMessageBorderColor = Color.parseColor("#88CCCCCC")

    /**
     * 视频缩略图圆角
     */
    var videoMessageBorderRadius = 4f

    /**
     * 视频缩略图背景色
     */
    var videoMessageBgColor = Color.parseColor("#08000000")

    /**
     * 名片消息的气泡宽度
     */
    var cardMessageBubbleWidth = 230

    /**
     * 名片消息的缩略图宽度
     */
    var cardMessageThumbnailWidth = 40

    /**
     * 名片消息的缩略图高度
     */
    var cardMessageThumbnailHeight = 40

    /**
     * 名片消息的缩略图圆角
     */
    var cardMessageThumbnailBorderRadius = 0f

    /**
     * 名片消息的缩略图背景色
     */
    var cardMessageThumbnailBgColor = 0

    /**
     * 文章消息的气泡宽度
     */
    var postMessageBubbleWidth = 230

    /**
     * 文章消息的缩略图宽度
     */
    var postMessageThumbnailWidth = 38

    /**
     * 文章消息的缩略图高度
     */
    var postMessageThumbnailHeight = 38

    /**
     * 文章消息的缩略图圆角
     */
    var postMessageThumbnailBorderRadius = 0f

    /**
     * 文章消息的缩略图背景色
     */
    var postMessageThumbnailBgColor = 0

    /**
     * 链接颜色
     */
    var linkTextColor = Color.parseColor("#1690E5")

    /**
     * 菜单文本
     */
    var menuItemCopy = "复制"

    var menuItemShare = "转发"

    /**
     * 格式化文本内容
     */
    abstract fun formatText(textView: TextView, text: SpannableString)

    /**
     * 是否是显示在右边的消息
     */
    abstract fun isRightMessage(message: Message): Boolean

    /**
     * 加载图片
     */
    abstract fun loadImage(imageView: ImageView, url: String, width: Int, height: Int)

}