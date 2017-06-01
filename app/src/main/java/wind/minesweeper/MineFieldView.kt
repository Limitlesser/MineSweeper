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

    var xCell = 0
    var yCell = 0

    val gestureDetector: GestureDetectorCompat = GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            val point = toMinePoint(e.x, e.y)
            mineField.show(point.x, point.y)
            invalidate()
            return true
        }
    })

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        xCell = measuredWidth / mineField.width
        yCell = measuredHeight / mineField.height
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (i in 0..(mineField.width - 1))
            for (j in (0..mineField.height - 1)) {
                val block = mineField.blocks[i][j]
                val point = toViewPoint(i, j)
                if (block.open) {
                    if (block.bomb) {
                        paint.color = Color.WHITE
                        canvas.drawRect(point.x.toFloat() + 3, point.y.toFloat() + 3,
                                (point.x + xCell).toFloat() - 3, (point.y + yCell).toFloat() - 3, paint)
                        paint.color = Color.RED
                        canvas.drawCircle(point.x.toFloat() + xCell / 2, point.y.toFloat() + yCell / 2
                                , Math.min(xCell, yCell) / 2.toFloat() - 5, paint)
                    } else {
                        paint.color = Color.WHITE
                        canvas.drawRect(point.x.toFloat() + 3, point.y.toFloat() + 3,
                                (point.x + xCell).toFloat() - 3, (point.y + yCell).toFloat() - 3, paint)
                        paint.color = Color.DKGRAY
                        paint.textSize = 20f
                        if (block.bombNum > 0)
                            canvas.drawText(block.bombNum.toString(), point.x.toFloat() + xCell / 2 - 3,
                                    point.y.toFloat() + yCell / 2 + 5, paint)
                    }
                } else {
                    paint.color = Color.DKGRAY
                    canvas.drawRect(point.x.toFloat() + 3, point.y.toFloat() + 3,
                            (point.x + xCell).toFloat() - 3, (point.y + yCell).toFloat() - 3, paint)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun toViewPoint(x: Int, y: Int) = Point(x * xCell, y * yCell)

    private fun toMinePoint(x: Float, y: Float) = Point((x / xCell).toInt(), (y / yCell).toInt())
}
