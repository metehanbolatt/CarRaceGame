package com.metehanbolat.carracegame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.metehanbolat.carracegame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), GameTask {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mGameView: GameView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        mGameView = GameView(this ,this)
        startButtonClick()

    }

    override fun closeGame(mScore: Int) {
        binding.apply {
            score.text = resources.getString(R.string.score, mScore)
            root.removeView(mGameView)
            startButton.visibility = View.VISIBLE
            score.visibility = View.VISIBLE
        }
    }

    private fun startButtonClick() {
        binding.apply {
            startButton.setOnClickListener {
                mGameView.setBackgroundResource(R.drawable.road)
                root.addView(mGameView)
                startButton.visibility = View.GONE
                score.visibility = View.GONE
            }
        }
    }
}