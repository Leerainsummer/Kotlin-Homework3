package com.example.lab8

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    // 宣告 MyAdapter 物件，使用 lateinit 關鍵字來延遲初始化
    private lateinit var myAdapter: MyAdapter

    // 宣告 contacts 陣列，表示聯絡人資料
    private val contacts = ArrayList<Contact>()

    // 宣告 ActivityResultLauncher。
    // 內部負責處理 SecActivity 回傳結果
    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            // 取得回傳的 Intent，並從 Intent 中取得聯絡人資訊
            val intent = result.data
            val name = intent?.getStringExtra("name") ?: ""
            val phone = intent?.getStringExtra("phone") ?: ""
            // 新增聯絡人資料
            if (name.isBlank() || phone.isBlank()) return@registerForActivityResult
            contacts.add(Contact(name, phone))
            saveContacts()
            // 更新列表
            myAdapter.notifyItemInserted(contacts.lastIndex)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
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
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnAdd = findViewById<Button>(R.id.btnAdd)
        // 創建 LinearLayoutManager 物件，設定垂直排列
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = linearLayoutManager
        // 創建 MyAdapter 並連結 recyclerView
        val saved = org.json.JSONArray(
            getPreferences(MODE_PRIVATE).getString("contacts", "[]"))
        for (index in 0 until saved.length()) {
            val entry = saved.getJSONObject(index)
            contacts.add(Contact(entry.getString("name"), entry.getString("phone")))
        }
        myAdapter = MyAdapter(contacts) { saveContacts() }
        recyclerView.adapter = myAdapter
        // 設定按鈕監聽器，使用 startForResult 前往 SecActivity
        btnAdd.setOnClickListener {
            val i = Intent(this, SecActivity::class.java)
            startForResult.launch(i)
        }
    }
    private fun saveContacts() {
        val saved = org.json.JSONArray()
        contacts.forEach { contact ->
            saved.put(org.json.JSONObject().apply {
                put("name", contact.name)
                put("phone", contact.phone)
            })
        }
        getPreferences(MODE_PRIVATE).edit()
            .putString("contacts", saved.toString()).apply()
    }
}
