package wind.minesweeper

import java.util.*

/**
 * Created by wind on 2017/6/1.
 */

data class Block(val isBomb: Boolean, var isOpen: Boolean = false, var aroundBombNum: Int = 0)

class MineField(val width: Int, val height: Int, val bombNum: Int, var onGameOver: ((Boolean) -> Unit)? = null) {

    lateinit var blocks: Array<Array<Block>>

    init {
        reset()
    }

    fun reset() {
        val blockArray = List(width * height) { Block(it < bombNum) }.apply { Collections.shuffle(this) }
        blocks = Array(width) { i -> Array(height) { j -> blockArray[i * height + j] } }
    }

    fun open(x: Int, y: Int) {
        val block = blocks[x][y]
        if (block.isOpen) return
        block.isOpen = true
        if (block.isBomb) {
            blocks.forEach { it.forEach { it.isOpen = true } }
            onGameOver?.invoke(false)
        } else {
            block.aroundBombNum = aroundBombs(x, y).filter { it.isBomb }.size
            if (block.aroundBombNum == 0) aroundBombs(x, y) { i, j -> open(i, j) }
            if (blocks.flatMap { it.asIterable() }.filter { !it.isBomb && !it.isOpen }.isEmpty())
                onGameOver?.invoke(true)
        }
    }

    private fun aroundBombs(x: Int, y: Int, block: ((Int, Int) -> Unit)? = null): List<Block> {
        val bombs = mutableListOf<Block>()
        for (i in maxOf(x - 1, 0)..minOf(x + 1, width - 1))
            for (j in maxOf(y - 1, 0)..minOf(y + 1, height - 1)) {
                bombs.add(blocks[i][j])
                block?.invoke(i, j)
            }
        return bombs
    }
}