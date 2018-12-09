package com.github.herokotlin.messagelist.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView
import java.lang.ref.WeakReference

class RoundImageView : ImageView {

    private var viewWidth = 1f
    private var viewHeight = 1f

    private var viewBorderWidth = 0f
    private var viewBorderColor = Color.BLACK
    private var viewBorderRadius = 0f

    private var intrinsicWidth = 0
    private var intrinsicHeight = 0

    private var imageWidth = 0f
    private var imageHeight = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val backgroundRect = RectF(0f, 0f, 0f, 0f)

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    // 待绘制的图
    private var drawBitmap: WeakReference<Bitmap>? = null
    // 遮罩图
    private var maskBitmap: WeakReference<Bitmap>? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        updateImage()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        updateImage()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        updateImage()
    }

    private fun updateImage() {

        if (drawable != null) {
            intrinsicWidth = drawable.intrinsicWidth
            intrinsicHeight = drawable.intrinsicHeight
        }

    }

    private fun createMaskBitmap(): Bitmap {

        val bitmap = Bitmap.createBitmap(imageWidth.toInt(), imageHeight.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.BLACK

        canvas.drawRoundRect(RectF(0f, 0f, imageWidth, imageHeight), viewBorderRadius, viewBorderRadius, paint)

        return bitmap
    }

    private fun createDrawBitmap(): Bitmap {

        var width = imageWidth.toInt()
        var height = imageHeight.toInt()

        val widthScale = imageWidth / intrinsicWidth
        val heightScale = imageHeight / intrinsicHeight

        // > 1 表示图片比较小，没必要缩放了
        if (widthScale < 1 || heightScale < 1) {
            val scale = Math.min(widthScale, heightScale)
            width = (intrinsicWidth * scale).toInt()
            height = (intrinsicHeight * scale).toInt()
        }
        else {
            // 小于 50 就算了
            if (width - intrinsicWidth > 50) {
                width = intrinsicWidth
            }
            if (height - intrinsicHeight > 50) {
                height = intrinsicHeight
            }
        }

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)

        return bitmap

    }

    override fun invalidate() {

        maskBitmap = null
        drawBitmap = null

        super.invalidate()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        setMeasuredDimension(viewWidth.toInt(), viewHeight.toInt())

    }

    override fun onDraw(canvas: Canvas) {

        if (drawable == null) {
            return
        }

        paint.style = Paint.Style.FILL

        if (viewBorderColor != 0) {
            paint.color = viewBorderColor
        }

        if (viewBorderWidth > 0) {
            canvas.drawRoundRect(backgroundRect, viewBorderRadius, viewBorderRadius, paint)
        }

        // 避免前面用了半透明颜色
        paint.color = Color.BLACK

        var bitmap = drawBitmap?.get()
        if (bitmap == null || bitmap.isRecycled) {
            bitmap = createDrawBitmap()
            drawBitmap = WeakReference(bitmap)
        }

        var mask = maskBitmap?.get()
        if (mask == null || mask.isRecycled) {
            mask = createMaskBitmap()
            maskBitmap = WeakReference(mask)
        }

        val saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)

        var left = viewBorderWidth
        var top = viewBorderWidth

        if (bitmap.width < imageWidth) {
            left += (imageWidth - bitmap.width) / 2
        }
        if (bitmap.height < imageHeight) {
            top += (imageHeight - bitmap.height) / 2
        }

        canvas.drawBitmap(bitmap, left, top, paint)

        paint.xfermode = xfermode

        canvas.drawBitmap(mask, viewBorderWidth, viewBorderWidth, paint)

        paint.xfermode = null

        canvas.restoreToCount(saved)

    }

    fun setImageInfo(width: Float, height: Float, borderWidth: Float, borderColor: Int, borderRadius: Float) {

        viewWidth = width
        viewHeight = height
        viewBorderWidth = borderWidth
        viewBorderColor = borderColor
        viewBorderRadius = borderRadius

        backgroundRect.right = width
        backgroundRect.bottom = height

        if (viewBorderWidth > 0) {
            imageWidth = width - 2 * viewBorderWidth
            imageHeight = height - 2 * viewBorderWidth
        }
        else {
            imageWidth = width
            imageHeight = height
        }

        // 后续会调 setImageResource 之类的方法
        // 它们会触发 invalidate
        // 因此这里无需调 invalidate

    }

}
