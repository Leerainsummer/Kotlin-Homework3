package com.example.lab9_1

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private lateinit var start: Button
    private lateinit var rabbit: SeekBar
    private lateinit var turtle: SeekBar
    private var race: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val root = findViewById<android.view.View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        start = findViewById(R.id.btnStart)
        rabbit = findViewById(R.id.sbRabbit)
        turtle = findViewById(R.id.sbTurtle)
        rabbit.setOnTouchListener { _, _ -> true }
        turtle.setOnTouchListener { _, _ -> true }
        start.setOnClickListener { startRace() }
    }

    private fun startRace() {
        if (race?.isActive == true) return
        start.isEnabled = false
        rabbit.progress = 0
        turtle.progress = 0
        race = lifecycleScope.launch {
            val winner = CompletableDeferred<String>()
            try {
                val result = coroutineScope {
                    val rabbitJob = launch {
                        while (isActive && !winner.isCompleted) {
                            delay(100)
                            if (Random.nextInt(3) < 2) delay(300)
                            if (winner.isCompleted) break
                            rabbit.progress = (rabbit.progress + 3).coerceAtMost(100)
                            if (rabbit.progress == 100) winner.complete("兔子勝利")
                        }
                    }
                    val turtleJob = launch {
                        while (isActive && !winner.isCompleted) {
                            delay(100)
                            if (winner.isCompleted) break
                            turtle.progress = (turtle.progress + 1).coerceAtMost(100)
                            if (turtle.progress == 100) winner.complete("烏龜勝利")
                        }
                    }
                    val result = winner.await()
                    rabbitJob.cancelAndJoin()
                    turtleJob.cancelAndJoin()
                    result
                }
                Toast.makeText(this@MainActivity, result, Toast.LENGTH_SHORT).show()
            } finally {
                start.isEnabled = true
            }
        }
    }
}
