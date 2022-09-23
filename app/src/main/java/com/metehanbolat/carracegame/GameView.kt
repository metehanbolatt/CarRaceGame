package com.metehanbolat.carracegame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import kotlin.math.abs

@SuppressLint("ViewConstructor")
class GameView(context: Context, var gameTask: GameTask): View(context) {

    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myCarPosition = 0
    private val otherCars = ArrayList<HashMap<String, Any>>()

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherCars.add(map)
        }
        time += 10 + speed
        val carWidth = viewWidth / 5
        val carHeight = carWidth / 10
        myPaint!!.style = Paint.Style.FILL
        val d = ResourcesCompat.getDrawable(resources, R.drawable.red_car, null)

        d?.let {
            it.setBounds(
                myCarPosition * viewWidth / 3 + viewWidth / 15 + 25,
                viewHeight - 2 - carHeight,
                myCarPosition * viewWidth / 3 + viewWidth / 15 + carWidth - 25,
                viewHeight - 2
            )
            it.draw(canvas!!)
        }
        myPaint!!.color = Color.GREEN
        var highScore = 0
        for (i in otherCars.indices) {
            try {
                val carX = otherCars[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                val carY = time - otherCars[i]["startTime"] as Int
                val d2 = ResourcesCompat.getDrawable(resources, R.drawable.yellow_car, null)
                d2?.let {
                    it.setBounds(
                        carX + 25,
                        carY - carHeight,
                        carX + carWidth,
                        carY
                    )
                    it.draw(canvas!!)
                }
                if (otherCars[i]["lane"] as Int == myCarPosition) {
                    if (carY > viewHeight - 2 - carHeight && carY < viewHeight - 2) {
                        gameTask.closeGame(score)
                    }
                }
                if (carY > viewHeight + carHeight) {
                    otherCars.removeAt(i)
                    score++
                    speed = 1 + abs(score / 8)
                    if (score > highScore)  highScore = score
                }
            }
            catch (e: Exception) {
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas!!.drawText("Score: $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed: $speed", 380f, 80f, myPaint!!)
        invalidate()

    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) if (myCarPosition > 0) myCarPosition--
                if (x1 > viewWidth / 2) if (myCarPosition < 2) myCarPosition++
                invalidate()
            }
            MotionEvent.ACTION_UP -> {

            }
        }

        return true

    }
}