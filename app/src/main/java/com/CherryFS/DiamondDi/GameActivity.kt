package com.CherryFS.DiamondDi

import android.graphics.Bitmap

import android.os.Bundle
import android.util.Size
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.CherryFS.DiamondDi.files.BoardOptionsViewModel
import com.CherryFS.DiamondDi.files.BoardTitledSize
import com.CherryFS.DiamondDi.files.GameBoard
import androidx.lifecycle.Observer

data class BoardActivityParams(val bitmap: Bitmap, val size: BoardTitledSize)

class GameActivity : AppCompatActivity() {
    companion object {
        lateinit var initialConfig: BoardActivityParams
    }

    private val viewModel: BoardOptionsViewModel by lazy {
        ViewModelProviders.of(this).get(BoardOptionsViewModel::class.java)
    }

    private fun mountBoard() {
        var board = findViewById<GameBoard>(R.id.boardView)

        viewModel.boardSize.observe(this, Observer {
            it?.let {
                board.resize(
                    Size(it.width, it.height),
                    viewModel.boardImage.value
                )
            }
        })

        findViewById<Button>(R.id.shuffle).setOnClickListener {
            board.shuffle()
        }

        findViewById<Button>(R.id.reset).setOnClickListener {
            board.shuffle(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.apply {
            boardSize.value = initialConfig.size
            boardImage.value = initialConfig.bitmap
        }

        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.game_activity)
        setSupportActionBar(findViewById(R.id.board_options_toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mountBoard()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//
//        return super.onOptionsItemSelected(item)
//    }
}
