package com.example.haburasiapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.tts.TextToSpeech
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import android.content.Intent


class BrushingGuideActivity : AppCompatActivity() {

    private lateinit var guideText: TextView
    private lateinit var timerText: TextView
    private lateinit var startButton: Button
    private lateinit var toothImage: ImageView
    private var mediaPlayer: MediaPlayer? = null
    private lateinit var startPlayer: MediaPlayer
    private lateinit var tts: TextToSpeech

    // 表示用（漢字）
    private val steps = listOf(
        "① 下の右奥歯をみがこう！",
        "② 下の左奥歯をみがこう！",
        "③ 下の前歯をみがこう！",
        "④ 上の左奥歯をみがこう！",
        "⑤ 上の右奥歯をみがこう！",
        "⑥ 上の前歯をみがこう！",
        "✨ おつかれさま！ピカピカになったよ！"
    )

    // 読み上げ用（ひらがな）
    private val stepsTTS = listOf(
        "したのみぎおくばをみがこう",
        "したのひだりおくばをみがこう",
        "したのまえばをみがこう",
        "うえのひだりおくばをみがこう",
        "うえのみぎおくばをみがこう",
        "うえのまえばをみがこう",
        "おつかれさまでした、ぴかぴかになったよ"
    )

    private val imageResources = listOf(
        R.drawable.migisitaoku,
        R.drawable.sitahidarioku,
        R.drawable.sitamae,
        R.drawable.uehidarioku,
        R.drawable.ueomigioku,
        R.drawable.uemae
    )

    private var currentStep = 0
    private var countdownSeconds = 10
    private val handler = Handler(Looper.getMainLooper())
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_brushing_guide)

        guideText = findViewById(R.id.guideText)
        timerText = findViewById(R.id.timerText)
        startButton = findViewById(R.id.startButton)
        toothImage = findViewById(R.id.toothImage)

        toothImage.setImageResource(R.drawable.tooth_base)
        timerText.text = "残り 10 秒"

        // 効果音読み込み
        startPlayer = MediaPlayer.create(this, R.raw.start_sound_trimmed)

        // TTS 初期化
        tts = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts.language = Locale.JAPANESE
            }
        }

        startButton.setOnClickListener {
            if (!isRunning) {
                isRunning = true
                currentStep = 0
                startButton.isEnabled = false
                playStartSound()
                startCountdownBeforeStart(3)
            }
        }
    }

    private fun startCountdownBeforeStart(seconds: Int) {
        var count = seconds
        timerText.text = "スタートまで $count..."
        val countdownRunnable = object : Runnable {
            override fun run() {
                count--
                if (count > 0) {
                    timerText.text = "スタートまで $count..."
                    handler.postDelayed(this, 1000)
                } else {
                    timerText.text = ""
                    nextStep()
                }
            }
        }
        handler.postDelayed(countdownRunnable, 1000)
    }

    private fun nextStep() {
        if (currentStep < steps.size) {
            guideText.text = steps[currentStep]
            tts.speak(stepsTTS[currentStep], TextToSpeech.QUEUE_FLUSH, null, null)

            if (currentStep < imageResources.size) {
                playStepSound()
                toothImage.setImageResource(imageResources[currentStep])
            } else {
                toothImage.setImageResource(R.drawable.tooth_base)
            }

            countdownSeconds = 10
            updateTimerText()

            val countdownRunnable = object : Runnable {
                override fun run() {
                    countdownSeconds--
                    updateTimerText()
                    if (countdownSeconds > 0) {
                        handler.postDelayed(this, 1000)
                    } else {
                        currentStep++
                        nextStep()
                    }
                }
            }

            handler.postDelayed(countdownRunnable, 1000)

        } else {
            // 歯みがき完了後 → ごほうび画面へ遷移！
            val intent = Intent(this, RewardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun updateTimerText() {
        timerText.text = "残り $countdownSeconds 秒"
    }

    private fun playStepSound() {
        mediaPlayer?.release()
        mediaPlayer = MediaPlayer.create(this, R.raw.step_sound)
        mediaPlayer?.start()
    }

    private fun playStartSound() {
        startPlayer.seekTo(0)
        startPlayer.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
        startPlayer.release()
        tts.shutdown()
    }
}
