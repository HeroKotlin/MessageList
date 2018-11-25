package com.github.herokotlin.messagelist.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.widget.ImageView
import java.lang.ref.WeakReference

class RoundImageView : ImageView {

    private var viewWidth = 1f
    private var viewHeight = 1f

    private var viewBorderWidth = 0f
    private var viewBorderColor = Color.BLACK
    private var viewBorderRadius = 0f

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

    private fun createMaskBitmap(): Bitmap {

        val bitmap = Bitmap.createBitmap(imageWidth.toInt(), imageHeight.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        paint.color = Color.BLACK

        canvas.drawRoundRect(RectF(0f, 0f, imageWidth, imageHeight), viewBorderRadius, viewBorderRadius, paint)

        return bitmap
    }

    private fun createDrawBitmap(): Bitmap {

        val bitmap = Bitmap.createBitmap(imageWidth.toInt(), imageHeight.toInt(), Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)

        drawable.setBounds(0, 0, imageWidth.toInt(), imageHeight.toInt())

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

        val saved = canvas.saveLayer(null, null, Canvas.ALL_SAVE_FLAG)

        var bitmap = drawBitmap?.get()
        if (bitmap == null || bitmap.isRecycled) {
            bitmap = createDrawBitmap()
            drawBitmap = WeakReference(bitmap)
        }

        canvas.drawBitmap(bitmap, viewBorderWidth, viewBorderWidth, paint)


        paint.xfermode = xfermode


        bitmap = maskBitmap?.get()
        if (bitmap == null || bitmap.isRecycled) {
            bitmap = createMaskBitmap()
            maskBitmap = WeakReference(bitmap)
        }
        canvas.drawBitmap(bitmap, viewBorderWidth, viewBorderWidth, paint)


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

        invalidate()

    }

}
