package com.example.heartratioapp.measure_activity.recorder

import android.view.SurfaceHolder

class RecordThread(private val surface: RecorderSurface, private val surfaceHolder: SurfaceHolder) : Thread() {

    var running = true
    private val targetFPS = 60

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (1000 / targetFPS).toLong()

        do {
            startTime = System.nanoTime()
            val canvas = surfaceHolder.lockCanvas()
            surface.draw(canvas)
            surface.update()
            surfaceHolder.unlockCanvasAndPost(canvas)
            timeMillis = (System.nanoTime() - startTime)/ 1000000
            waitTime = targetTime - timeMillis

            if (waitTime >= 0)
                sleep(waitTime)
        } while (running)
    }
}