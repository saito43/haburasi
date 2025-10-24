package com.example.haburasiapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val editName = findViewById<EditText>(R.id.editName)
        val editAddress = findViewById<EditText>(R.id.editAddress)
        val editMemo = findViewById<EditText>(R.id.editMemo)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // 既存の情報を表示
        val prefs = getSharedPreferences("mypage_data", MODE_PRIVATE)
        editName.setText(prefs.getString("name", ""))
        editAddress.setText(prefs.getString("address", ""))
        editMemo.setText(prefs.getString("memo", ""))

        // 保存ボタン押下時の処理
        saveButton.setOnClickListener {
            val name = editName.text.toString()
            val address = editAddress.text.toString()
            val memo = editMemo.text.toString()

            val editor = prefs.edit()
            editor.putString("name", name)
            editor.putString("address", address)
            editor.putString("memo", memo)
            editor.apply()

            // MyPageActivity に戻る
            val intent = Intent(this, MyPageActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }
    }
}
