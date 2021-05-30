package com.example.heartratioapp.measure_activity.recorder

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView

class RecorderSurface(context: Context, attr: AttributeSet) : SurfaceView(context, attr),
    SurfaceHolder.Callback {

    private var opacity = 255
    private var desc = true
    var recordThread: RecordThread

    init {
        setZOrderOnTop(true)
        holder.addCallback(this)
        holder.setFormat(PixelFormat.TRANSPARENT)
        recordThread = RecordThread(this, holder)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        val canvas = holder.lockCanvas()
        draw(canvas)
        holder.unlockCanvasAndPost(canvas)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {

    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)

        if (canvas == null) return

        val red = Paint().apply { color = Color.argb(opacity, 250, 20, 30) }
        val grey = Paint().apply { color = Color.argb(opacity, 150, 150, 150) }
        val white = Paint().apply { color = Color.WHITE }
        val w = width.toFloat()
        val h = height.toFloat()

        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        canvas.drawOval(0f, 0f, w, h, grey)
        canvas.drawOval(w/5f, h/5f, w-(w/5f), h-(h/5f), white)
        canvas.drawOval(w/5f, h/5f, w-(w/5f), h-(h/5f), red)
    }

    fun update() {
        if (desc)
            opacity -= 5
        else
            opacity += 5
        if (opacity >= 255 || opacity <= 0)
            desc = !desc
    }
}