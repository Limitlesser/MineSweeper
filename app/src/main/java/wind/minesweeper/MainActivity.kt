package wind.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mineField = MineField(width = 20, height = 10, bombNum = 20)
        setContentView(MineFieldView(this, mineField))
    }
}
