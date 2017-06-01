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
            val point = toMinePoint(e.x, e.y)
            mineField.show(point.x, point.y)
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
        for (i in 0..(mineField.width - 1))
            for (j in 0..(mineField.height - 1)) {
                val block = mineField.blocks[i][j]
                val point = toViewPoint(i, j)
                if (block.open) {
                    if (block.bomb) {
                        paint.color = Color.WHITE
                        drawBlock(canvas, point)
                        paint.color = Color.RED
                        canvas.drawCircle(point.x.toFloat() + cell.x / 2, point.y.toFloat() + cell.y / 2
                                , Math.min(cell.x, cell.y) / 2.toFloat() - 5, paint)
                    } else {
                        paint.color = Color.WHITE
                        drawBlock(canvas, point)
                        paint.color = Color.DKGRAY
                        paint.textSize = 20f
                        if (block.bombNum > 0)
                            canvas.drawText(block.bombNum.toString(), point.x.toFloat() + cell.x / 2 - 3,
                                    point.y.toFloat() + cell.y / 2 + 5, paint)
                    }
                } else {
                    paint.color = Color.DKGRAY
                    drawBlock(canvas, point)
                }
            }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private fun drawBlock(canvas: Canvas, point: Point) =
            canvas.drawRect(point.x.toFloat() + 3, point.y.toFloat() + 3,
                    (point.x + cell.x).toFloat() - 3, (point.y + cell.y).toFloat() - 3, paint)


    private fun toViewPoint(x: Int, y: Int) = Point(x * cell.x, y * cell.y)

    private fun toMinePoint(x: Float, y: Float) = Point((x / cell.x).toInt(), (y / cell.y).toInt())
}
