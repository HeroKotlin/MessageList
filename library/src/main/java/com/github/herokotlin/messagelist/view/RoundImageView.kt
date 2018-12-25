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
    private val clipRect = RectF()

    private var imageWidth = 0f
    private var imageHeight = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    private val xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)

    private var drawableBitmap: Bitmap? = null

    private var drawableCanvas: Canvas? = null

    private var drawableWidth = 0

    private var drawableHeight = 0

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
        updateBitmap()
    }

    private fun updateBitmap() {

        drawableBitmap = null

        if (drawable != null) {

            val intrinsicWidth = drawable.intrinsicWidth.toFloat()
            val intrinsicHeight = drawable.intrinsicHeight.toFloat()

            if (viewWidth > 0 && viewHeight > 0 && imageWidth > 0 && imageHeight > 0 && intrinsicWidth > 0 && intrinsicHeight > 0) {

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

                drawableWidth = width.toInt()
                drawableHeight = height.toInt()

                drawableBitmap = Bitmap.createBitmap(drawableWidth, drawableHeight, Bitmap.Config.ARGB_8888)
                drawableCanvas = Canvas(drawableBitmap)

            }

        }

        invalidate()

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        setMeasuredDimension(viewWidth.toInt(), viewHeight.toInt())

    }

    override fun onDraw(canvas: Canvas) {

        val cacheBitmap = drawableBitmap
        val cacheCanvas = drawableCanvas

        if (cacheBitmap == null || cacheCanvas == null) {
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

        if (viewBorderWidth > 0) {
            canvas.drawRoundRect(clipRect, viewBorderRadius, viewBorderRadius, paint)
        }
        else {
            canvas.drawRect(clipRect, paint)
        }

        paint.xfermode = xfermode

        val left = viewBorderWidth + (imageWidth - drawableWidth) / 2
        val top = viewBorderWidth + (imageHeight - drawableHeight) / 2

        // gif 会不停的调 onDraw，因此只有在这里不停的 drawable.draw(cacheCanvas) 才会有动画
        drawable.setBounds(0, 0, drawableWidth, drawableHeight)
        drawable.draw(cacheCanvas)

        canvas.drawBitmap(cacheBitmap, left, top, paint)

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

        viewRect.right = width
        viewRect.bottom = height

        clipRect.left = viewBorderWidth
        clipRect.top = viewBorderWidth
        clipRect.right = width - viewBorderWidth
        clipRect.bottom = height - viewBorderWidth

        // 后续会调 setImageResource 之类的方法
        // 它们会触发 invalidate
        // 因此这里无需调 invalidate

    }

}
