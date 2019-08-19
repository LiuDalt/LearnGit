package com.example.administrator.myapplication

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View


class RoomChatBubble: View {

    private var srcBitmap: Bitmap? = null
    private var imgH: Int = 0
    private var imgW: Int = 0
    private var halfImgH : Int = 0
    private var halfImgW : Int = 0
    private val paint: Paint = Paint()

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        if(srcBitmap != null && width > imgW && height > imgH) {
            paint.shader = imgLeftTopShader
            canvas!!.drawRect(0f, 0f, (width - halfImgW).toFloat(), (height - halfImgH).toFloat(), paint)

            paint.shader = imgRightTopShader
            canvas!!.translate((width - halfImgW).toFloat(), 0f)
            canvas!!.drawRect(0f, 0f, width.toFloat(),(height - halfImgH).toFloat(), paint)
            canvas!!.translate(-(width - halfImgW).toFloat(), 0f)

            paint.shader = imgLeftBottomShader
            canvas!!.translate(0f, (height - halfImgH).toFloat())
            canvas!!.drawRect(0f, 0f, (width - halfImgW).toFloat(), height.toFloat(), paint)
            canvas!!.translate(0f, -(height - halfImgH).toFloat())

            paint.shader = imgRightBottomShader
            canvas!!.translate((width - halfImgW).toFloat(), (height - halfImgH).toFloat())
            canvas!!.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            canvas!!.translate(-(width - halfImgW).toFloat(), -(height - halfImgH).toFloat())
        }
    }

    private var imgLeftTopShader: BitmapShader? = null

    private var imgRightTopShader: BitmapShader? = null

    private var imgLeftBottomShader: BitmapShader? = null

    private var imgRightBottomShader: BitmapShader? = null

    var imgLeftTop: Bitmap? = null

    var imgRightTop: Bitmap? = null

    var imgLeftBottom: Bitmap? = null

    var imgRightBottom: Bitmap? = null

    fun setImagePath(path: String) {
        srcBitmap = BitmapFactory.decodeFile(path)

        imgH = srcBitmap!!.height
        imgW = srcBitmap!!.width
        halfImgH = imgH / 2
        halfImgW = imgW / 2

        imgLeftTop = Bitmap.createBitmap(srcBitmap, 0, 0, halfImgW, halfImgH)
        imgLeftTopShader = BitmapShader(imgLeftTop, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        imgRightTop = Bitmap.createBitmap(srcBitmap, halfImgW, 0,halfImgW, halfImgH)
        imgRightTopShader = BitmapShader(imgRightTop, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        imgLeftBottom = Bitmap.createBitmap(srcBitmap, 0, halfImgH, halfImgW, halfImgH)
        imgLeftBottomShader = BitmapShader(imgLeftBottom, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        imgRightBottom = Bitmap.createBitmap(srcBitmap, halfImgW, halfImgH, halfImgW, halfImgH)
        imgRightBottomShader = BitmapShader(imgRightBottom, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        invalidate()
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = measureSize(widthMeasureSpec, 200)
        val height = measureSize(heightMeasureSpec, 50)
        setMeasuredDimension(width, height)
    }

    private fun measureSize(measureValue: Int, defaultSize: Int): Int {
        val mode = View.MeasureSpec.getMode(measureValue)
        val specValue = View.MeasureSpec.getSize(measureValue)
        when (mode) {
            //指定一个默认值
            View.MeasureSpec.UNSPECIFIED -> return defaultSize
            //取测量值
            View.MeasureSpec.EXACTLY -> return specValue
            //取测量值和默认值中的最小值
            View.MeasureSpec.AT_MOST -> return Math.min(defaultSize, specValue)
            else -> return defaultSize
        }
    }
}
