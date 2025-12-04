package com.example.haburasiapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MyPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        val avatarImage = findViewById<ImageView>(R.id.profileImage)
        val nameText = findViewById<TextView>(R.id.nameText)
        val addressText = findViewById<TextView>(R.id.addressText)
        val memoText = findViewById<TextView>(R.id.memoText)
        val streakText = findViewById<TextView>(R.id.streakText)
        val editButton = findViewById<Button>(R.id.editProfileButton)
        val viewStampsButton = findViewById<Button>(R.id.viewStampsButton)
        val resetStampsButton = findViewById<Button>(R.id.resetStampsButton)
        val toHomeButton = findViewById<Button>(R.id.toHomeButton)

        val prefs: SharedPreferences = getSharedPreferences("mypage_data", MODE_PRIVATE)
        val name = prefs.getString("name", "温情のおじょう")
        val address = prefs.getString("address", "愛知県")
        val memo = prefs.getString("memo", "今日もがんばる！")

        val stampPrefs = getSharedPreferences("StampPrefs", MODE_PRIVATE)
        val streak = stampPrefs.getInt("currentStreak", 0)

        nameText.text = "名前：$name"
        addressText.text = "住所：$address"
        memoText.text = "メモ：$memo"
        streakText.text = "📅 連続記録：${streak}日継続中！"

        avatarImage.setImageResource(R.drawable.avatar_base3)
        avatarImage.setOnClickListener {
            val intent = Intent(this, AvatarEditActivity::class.java)
            startActivity(intent)
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

        viewStampsButton.setOnClickListener {
            val intent = Intent(this, StampListActivity::class.java)
            startActivity(intent)
        }

        resetStampsButton.setOnClickListener {
            stampPrefs.edit().clear().apply()
            streakText.text = "📅 連続記録：0日継続中！"
            Toast.makeText(this, "スタンプ履歴をリセットしました", Toast.LENGTH_SHORT).show()
        }

        toHomeButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
