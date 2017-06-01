package wind.minesweeper

import java.util.*

/**
 * Created by wind on 2017/6/1.
 */

data class Block(val bomb: Boolean, var open: Boolean = false, var bombNum: Int = 0)

class MineField(val width: Int, val height: Int, bombNum: Int) {

    val blocks: Array<Array<Block>>

    init {
        val bombs = List(width * height) {
            Block(it < bombNum)
        }.apply { Collections.shuffle(this) }
        blocks = Array(width) {
            i ->
            Array(height) {
                j ->
                bombs[i * height + j]
            }
        }
    }

    fun show(x: Int, y: Int) {
        val block = blocks[x][y]
        if (!block.open) {
            block.open = true
            if (block.bomb) {
                blocks.forEach { it.forEach { it.open = true } }
            } else {
                block.apply {
                    bombNum = aroundBombs(x, y)
                    if (bombNum == 0) {
                        for (i in (x - 1)..(x + 1))
                            for (j in (y - 1)..(y + 1)) {
                                if (i in 0..(width - 1) && j in 0..(height - 1)) {
                                    show(i, j)
                                }
                            }
                    }
                }
            }
        }
    }

    private fun aroundBombs(x: Int, y: Int): Int {
        var bombs = 0
        for (i in (x - 1)..(x + 1))
            for (j in (y - 1)..(y + 1)) {
                if (i in 0..(width - 1) && j in 0..(height - 1)) {
                    if (blocks[i][j].bomb)
                        bombs++
                }
            }
        return bombs
    }

}