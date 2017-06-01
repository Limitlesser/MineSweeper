package wind.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.alert

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mineField = MineField(width = 20, height = 10, bombNum = 20)
        val mineFieldView = MineFieldView(this, mineField);
        mineField.onGameOver = { succ ->
            alert(if (succ) "You are Successed!" else "You are Failed!") {
                positiveButton("Go on") {
                    mineField.reset()
                    mineFieldView.invalidate()
                }
            }.show()
        }
        setContentView(mineFieldView)
    }
}
