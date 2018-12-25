package com.github.herokotlin.messagelist.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.widget.ImageView

internal class RoundImageView : ImageView {

    private var viewWidth = 0f
    private var viewHeight = 0f

    private var viewBorderWidth = 0f
    private var viewBorderColor = Color.BLACK
    private var viewBorderRadius = 0f

    private val viewRect = RectF()

    private var imageWidth = 0f
    private var imageHeight = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)

    // 待绘制的图
    private var drawBitmap: Bitmap? = null

    // 遮罩图
    private var maskBitmap: Bitmap? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        updateBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        updateBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        updateBitmap()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewRect.right = w.toFloat()
        viewRect.bottom = h.toFloat()
        updateBitmap()
    }

    private fun updateBitmap() {

        maskBitmap = null
        drawBitmap = null

        if (drawable != null) {

            val intrinsicWidth = drawable.intrinsicWidth.toFloat()
            val intrinsicHeight = drawable.intrinsicHeight.toFloat()

            if (viewWidth > 0 && viewHeight > 0 && intrinsicWidth > 0 && intrinsicHeight > 0) {
                maskBitmap = createMaskBitmap(imageWidth, imageHeight)
                drawBitmap = createDrawBitmap(intrinsicWidth, intrinsicHeight)
            }

        }

        invalidate()

    }

    private fun createMaskBitmap(width: Float, height: Float): Bitmap {

        val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.BLACK
        canvas.drawRoundRect(RectF(0f, 0f, width, height), viewBorderRadius, viewBorderRadius, paint)

        return bitmap

    }

    private fun createDrawBitmap(intrinsicWidth: Float, intrinsicHeight: Float): Bitmap {

        val scale = Math.min(imageWidth / intrinsicWidth, imageHeight / intrinsicHeight)

        var width = intrinsicWidth * scale
        var height = intrinsicHeight * scale

        // 避免除 0
        if (height > 0) {
            // 如果只是少了一点点像素，直接缩放就行了
            // 反正也看不出来
            val ratio = width / height
            if (imageWidth > width) {
                width = imageWidth
                height = width / ratio
            }
            if (imageHeight > height) {
                height = imageHeight
                width = height * ratio
            }
        }

        val bitmap = Bitmap.createBitmap(width.toInt(), height.toInt(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        drawable.setBounds(0, 0, width.toInt(), height.toInt())
        drawable.draw(canvas)

        return bitmap

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        setMeasuredDimension(viewWidth.toInt(), viewHeight.toInt())

    }

    override fun onDraw(canvas: Canvas) {

        val maskImage = maskBitmap
        val drawImage = drawBitmap

        if (maskImage == null || drawImage == null) {
            return
        }

        paint.style = Paint.Style.FILL

        if (viewBorderColor != 0) {
            paint.color = viewBorderColor
        }

        if (viewBorderWidth > 0) {
            canvas.drawRoundRect(viewRect, viewBorderRadius, viewBorderRadius, paint)
        }

        // 避免前面用了半透明颜色
        paint.color = Color.BLACK

        val saved = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(null, null)
        }
        else {
            canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)
        }

        val left = viewBorderWidth + (imageWidth - drawImage.width) / 2
        val top = viewBorderWidth + (imageHeight - drawImage.height) / 2

        canvas.drawBitmap(drawBitmap, left, top, paint)

        paint.xfermode = xfermode

        canvas.drawBitmap(maskBitmap, viewBorderWidth, viewBorderWidth, paint)

        paint.xfermode = null

        canvas.restoreToCount(saved)

    }

    fun setImageInfo(width: Float, height: Float, borderWidth: Float, borderColor: Int, borderRadius: Float) {

        viewWidth = width
        viewHeight = height
        viewBorderWidth = borderWidth
        viewBorderColor = borderColor
        viewBorderRadius = borderRadius

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
