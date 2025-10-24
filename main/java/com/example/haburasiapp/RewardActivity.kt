package com.example.haburasiapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.Period

class RewardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        val rewardText = findViewById<TextView>(R.id.rewardText)
        val stampImage = findViewById<ImageView>(R.id.stampImage)
        val viewStampsButton = findViewById<Button>(R.id.viewStampsButton)

        rewardText.text = "スタンプGET！"
        stampImage.setImageResource(R.drawable.stamp_placeholder)

        saveBrushingRecord()
        updateStreakRecord()
        addStampCount()
        saveCalendarStamp() // ← ここで日付を保存！

        viewStampsButton.setOnClickListener {
            val intent = Intent(this, StampListActivity::class.java)
            startActivity(intent)
        }
    }

    // タイムスタンプ記録（使ってるならそのままでOK）
    private fun saveBrushingRecord() {
        val prefs = getSharedPreferences("brushing_data", MODE_PRIVATE)
        val editor = prefs.edit()
        val now = System.currentTimeMillis()
        editor.putLong("last_brushed", now)
        editor.apply()
    }

    // 連続記録更新
    private fun updateStreakRecord() {
        val prefs = getSharedPreferences("brushing_data", MODE_PRIVATE)
        val editor = prefs.edit()

        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val todayStr = today.format(formatter)

        val lastDateStr = prefs.getString("last_brushed_date", null)

        val newStreak = if (lastDateStr != null) {
            val lastDate = LocalDate.parse(lastDateStr, formatter)
            val diff = Period.between(lastDate, today).days
            if (diff == 1) {
                prefs.getInt("streak_count", 1) + 1
            } else if (diff == 0) {
                prefs.getInt("streak_count", 1)
            } else {
                1
            }
        } else {
            1
        }

        editor.putString("last_brushed_date", todayStr)
        editor.putInt("streak_count", newStreak)
        editor.apply()
    }

    // スタンプ取得数カウント
    private fun addStampCount() {
        val prefs = getSharedPreferences("brushing_data", MODE_PRIVATE)
        val current = prefs.getInt("stamp_count", 0)
        prefs.edit().putInt("stamp_count", current + 1).apply()
    }

    // カレンダー用スタンプ日保存（yyyyMMdd = true）
    private fun saveCalendarStamp() {
        val prefs = getSharedPreferences("brushing_data", MODE_PRIVATE)
        val today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        prefs.edit().putBoolean(today, true).apply()
    }
}
