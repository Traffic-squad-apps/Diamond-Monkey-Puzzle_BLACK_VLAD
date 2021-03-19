package com.CherryFS.DiamondDi.files

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.CherryFS.DiamondDi.R
import java.io.Serializable

typealias TitledResourcePair = Pair<Int, String>

data class BoardTitledSize(val width: Int, val height: Int): Serializable {
    override fun toString(): String {
        return "$width x $height"
    }
}

class BoardOptionsViewModel : ViewModel() {
    companion object {
        val PREDEFINED_BOARD_SIZE = arrayOf(
            BoardTitledSize(3, 3),
            BoardTitledSize(4, 4),
            BoardTitledSize(5, 5),
            BoardTitledSize(6, 6),
            BoardTitledSize(7, 7),
            BoardTitledSize(8, 8)
        )

        val PREDEFINED_IMAGES: Array<TitledResourcePair> = arrayOf(
            TitledResourcePair(
                R.drawable.monkeys1,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys2,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys3,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys4,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys5,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys6,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys7,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys8,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys9,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys10,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys11,
                ""
            ),
            TitledResourcePair(
                R.drawable.monkeys12,
                ""
            )
        )
    }

    val boardSize = MutableLiveData<BoardTitledSize>()
    val boardImage = MutableLiveData<Bitmap>()
}
