package com.example.haburasiapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.FrameLayout
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.View
import android.content.SharedPreferences
import android.widget.Toast
import java.time.LocalDate

class StampListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = FrameLayout(this)
        layout.layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)

        val prefs = getSharedPreferences("StampPrefs", MODE_PRIVATE)

        val calendarImage = ImageView(this).apply {
            setImageResource(R.drawable.calendar_december)
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
        layout.addView(calendarImage)

        val stampSize = 130
        val startX = -140
        val startY = 810
        val cellWidth = 205
        val cellHeight = 205

        val firstDayColumn = 0

        val today = LocalDate.now()
        val currentDay = if (today.year == 2025 && today.monthValue == 12) today.dayOfMonth else -1

        var lastStampedDay = prefs.getInt("lastStampedDay", -1)
        var currentStreak = prefs.getInt("currentStreak", 0)

        for (day in 1..31) {
            val index = day + firstDayColumn - 1
            val column = index % 7
            val row = index / 7

            val stamp = ImageView(this).apply {
                setImageResource(R.drawable.stamp_placeholder)
                layoutParams = FrameLayout.LayoutParams(stampSize, stampSize).apply {
                    leftMargin = startX + column * cellWidth
                    topMargin = startY + row * cellHeight
                }
                visibility = if (prefs.getBoolean("day_$day", false)) View.VISIBLE else View.INVISIBLE
            }

            val touchArea = View(this).apply {
                layoutParams = FrameLayout.LayoutParams(cellWidth, cellHeight).apply {
                    leftMargin = startX + column * cellWidth
                    topMargin = startY + row * cellHeight
                }
                setOnClickListener {
                    if (day == currentDay) {
                        if (!prefs.getBoolean("day_$day", false)) {
                            stamp.apply {
                                visibility = View.VISIBLE
                                scaleX = 0f
                                scaleY = 0f
                                animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(300)
                                    .start()
                            }
                            prefs.edit().putBoolean("day_$day", true).apply()

                            if (lastStampedDay == day - 1) {
                                currentStreak++
                            } else {
                                currentStreak = 1
                            }
                            lastStampedDay = day
                            prefs.edit()
                                .putInt("lastStampedDay", lastStampedDay)
                                .putInt("currentStreak", currentStreak)
                                .apply()

                            Toast.makeText(this@StampListActivity, "$currentStreak 日連続達成！", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@StampListActivity, "今日のスタンプは既に押されています。", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            layout.addView(touchArea)
            layout.addView(stamp)
        }

        val streakText = TextView(this).apply {
            text = "現在の連続記録: ${prefs.getInt("currentStreak", 0)}日"
            textSize = 16f
            layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                topMargin = 60
                leftMargin = 300
            }
        }
        layout.addView(streakText)

        val backButton = Button(this).apply {
            text = "ホームに戻る"
            layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                topMargin = 160
                leftMargin = 60
            }
            setOnClickListener {
                val intent = Intent(this@StampListActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        layout.addView(backButton)

        val myPageButton = Button(this).apply {
            text = "マイページに戻る"
            layoutParams = FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                topMargin = 320
                leftMargin = 60
            }
            setOnClickListener {
                val intent = Intent(this@StampListActivity, MyPageActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        layout.addView(myPageButton)

        setContentView(layout)
    }
}
