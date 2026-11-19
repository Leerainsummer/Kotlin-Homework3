package com.example.lab9_2

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.math.pow

class MainActivity : AppCompatActivity() {
    private var calculation: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val root = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(bars.left, bars.top, bars.right, bars.bottom)
            insets
        }
        val heightInput = findViewById<EditText>(R.id.edHeight)
        val weightInput = findViewById<EditText>(R.id.edWeight)
        val ageInput = findViewById<EditText>(R.id.edAge)
        val button = findViewById<Button>(R.id.btnCalculate)
        val weightResult = findViewById<TextView>(R.id.tvWeightResult)
        val fatResult = findViewById<TextView>(R.id.tvFatResult)
        val bmiResult = findViewById<TextView>(R.id.tvBmiResult)
        val progressText = findViewById<TextView>(R.id.tvProgress)
        val progress = findViewById<ProgressBar>(R.id.progressBar)
        val panel = findViewById<LinearLayout>(R.id.llProgress)
        val male = findViewById<RadioButton>(R.id.btnBoy)

        button.setOnClickListener {
            if (calculation?.isActive == true) return@setOnClickListener
            val height = positiveNumber(heightInput, "請輸入大於 0 的身高（公分）")
                ?: return@setOnClickListener
            val weight = positiveNumber(weightInput, "請輸入大於 0 的體重（公斤）")
                ?: return@setOnClickListener
            val age = positiveNumber(ageInput, "請輸入大於 0 的年齡")
                ?: return@setOnClickListener
            val isMale = male.isChecked
            button.isEnabled = false
            weightResult.text = "標準體重\n無"
            fatResult.text = "體脂肪\n無"
            bmiResult.text = "BMI\n無"
            progress.progress = 0
            progressText.text = "0%"
            panel.visibility = View.VISIBLE
            calculation = lifecycleScope.launch {
                try {
                    for (value in 1..100) {
                        delay(50)
                        progress.progress = value
                        progressText.text = "$value%"
                    }
                    val bmi = weight / (height / 100).pow(2)
                    val standard = if (isMale) (height - 80) * 0.7
                        else (height - 70) * 0.6
                    val bodyFat = 1.39 * bmi + 0.16 * age -
                        if (isMale) 19.34 else 9.0
                    weightResult.text = "標準體重\n" + format(standard) + " kg"
                    fatResult.text = "體脂肪\n" + format(bodyFat) + "%"
                    bmiResult.text = "BMI\n" + format(bmi)
                } finally {
                    panel.visibility = View.GONE
                    button.isEnabled = true
                }
            }
        }
    }

    private fun positiveNumber(input: EditText, message: String): Double? {
        val value = input.text.toString().trim().toDoubleOrNull()
        if (value == null || !value.isFinite() || value <= 0) {
            input.error = message
            input.requestFocus()
            return null
        }
        return value
    }

    private fun format(value: Double) = String.format(Locale.TAIWAN, "%.2f", value)
}
