package com.example.haburasiapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 歯を磨くボタン → BrushingGuideActivity に遷移
        val startButton = findViewById<Button>(R.id.btnBrush)
        startButton.setOnClickListener {
            val intent = Intent(this, BrushingGuideActivity::class.java)
            startActivity(intent)
        }

        // マイページボタン → MyPageActivity へ遷移
        val myPageButton = findViewById<Button>(R.id.myPageButton)
        myPageButton.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }
    }
}
