package com.example.lab8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sec)
        val root = findViewById<android.view.View>(R.id.main)
        val left = root.paddingLeft
        val top = root.paddingTop
        val right = root.paddingRight
        val bottom = root.paddingBottom
        ViewCompat.setOnApplyWindowInsetsListener(root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(left + systemBars.left, top + systemBars.top,
                right + systemBars.right, bottom + systemBars.bottom)
            insets
        }
        // 宣告元件變數並使用 findViewByID 方法取得元件
        val edName = findViewById<EditText>(R.id.edName)
        val edPhone = findViewById<EditText>(R.id.edPhone)
        val btnSend = findViewById<Button>(R.id.btnSend)
        // 設定按鈕監聽器，取得輸入的姓名與電話
        btnSend.setOnClickListener {
            //判斷是否輸入資料
            when {
                edName.text.isBlank() -> showToast("請輸入姓名")

                edPhone.text.isBlank() -> showToast("請輸入電話")

                else -> {
                    val b = Bundle()
                    b.putString("name", edName.text.toString().trim())
                    b.putString("phone", edPhone.text.toString().trim())
                    //使用setResult()回傳聯絡人資料
                    setResult(Activity.RESULT_OK, Intent().putExtras(b))
                    finish()
                }
            }
        }
    }

    // 建立 showToast 方法顯示 Toast 訊息
    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
