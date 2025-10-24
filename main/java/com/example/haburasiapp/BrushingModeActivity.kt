package com.example.haburasiapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class BrushingModeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brushing_mode)

        val btnWithSensor = findViewById<Button>(R.id.btnWithSensor)
        val btnWithoutSensor = findViewById<Button>(R.id.btnWithoutSensor)

        btnWithSensor.setOnClickListener {
            val intent = Intent(this, BrushingWithSensorActivity::class.java)
            startActivity(intent)
        }

        btnWithoutSensor.setOnClickListener {
            val intent = Intent(this, BrushingGuideActivity::class.java)
            startActivity(intent)
        }
    }
}
