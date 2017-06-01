package wind.minesweeper

import java.util.*

/**
 * Created by wind on 2017/6/1.
 */

data class Block(val isBomb: Boolean, var isOpen: Boolean = false, var aroundBombNum: Int = 0)

class MineField(val width: Int, val height: Int, val bombNum: Int) {

    lateinit var blocks: Array<Array<Block>>

    var onGameOver: ((Boolean) -> Unit)? = null

    init {
        reset()
    }

    fun reset() {
        val bombs = List(width * height) {
            Block(it < bombNum)
        }.apply { Collections.shuffle(this) }
        blocks = Array(width) { i ->
            Array(height) {
                j ->
                bombs[i * height + j]
            }
        }
    }

    fun show(x: Int, y: Int) {
        val block = blocks[x][y]
        if (!block.isOpen) {
            block.isOpen = true
            if (block.isBomb) {
                blocks.forEach { it.forEach { it.isOpen = true } }
                onGameOver?.invoke(false)
                return
            } else {
                block.aroundBombNum = aroundBombs(x, y).filter { it.isBomb }.size
                if (block.aroundBombNum == 0) {
                    aroundBombs(x, y) { i, j -> show(i, j) }
                }
            }
            if (blocks.flatMap { it.asIterable() }.filter { !it.isBomb && !it.isOpen }.isEmpty())
                onGameOver?.invoke(true)
        }
    }


    private fun aroundBombs(x: Int, y: Int, block: ((Int, Int) -> Unit)? = null): List<Block> {
        val bombs = mutableListOf<Block>()
        for (i in x - 1..x + 1)
            for (j in y - 1..y + 1) {
                if (i in 0..width - 1 && j in 0..height - 1) {
                    bombs.add(blocks[i][j])
                    block?.invoke(i, j)
                }
            }
        return bombs
    }

}