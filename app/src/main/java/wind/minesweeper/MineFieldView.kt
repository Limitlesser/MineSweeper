package wind.minesweeper

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.support.v4.view.GestureDetectorCompat
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

/**
 * Created by wind on 2017/6/1.
 */

class MineFieldView(context: Context, val mineField: MineField) : View(context) {

    val paint = Paint()

    val cell = Point()

    val gestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?) = true

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            mineField.show((e.x / cell.x).toInt(), (e.y / cell.y).toInt())
            invalidate()
            return true
        }
    })

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        cell.set(measuredWidth / mineField.width,
                measuredHeight / mineField.height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0..mineField.width - 1)
            for (j in 0..mineField.height - 1) {
                val block = mineField.blocks[i][j]
                val x = (i * cell.x).toFloat()
                val y = (j * cell.y).toFloat()
                if (block.isOpen) {
                    if (block.isBomb) {
                        paint.color = Color.WHITE
                        drawBlock(canvas, x, y)
                        paint.color = Color.RED
                        canvas.drawCircle(x + cell.x / 2, y + cell.y / 2
                                , Math.min(cell.x, cell.y) / 2.toFloat() - 5, paint)
                    } else {
                        paint.color = Color.WHITE
                        drawBlock(canvas, x, y)
                        paint.color = Color.DKGRAY
                        paint.textSize = 20f
                        if (block.aroundBombNum > 0)
                            canvas.drawText(block.aroundBombNum.toString(), x + cell.x / 2 - 3,
                                    y + cell.y / 2 + 5, paint)
                    }
                } else {
                    paint.color = Color.GRAY
                    drawBlock(canvas, x, y)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun drawBlock(canvas: Canvas, x: Float, y: Float) =
            canvas.drawRect(x + 3, y + 3, (x + cell.x) - 3, (y + cell.y) - 3, paint)


}
