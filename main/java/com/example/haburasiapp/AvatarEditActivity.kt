package com.example.haburasiapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class AvatarEditActivity : AppCompatActivity() {

    private lateinit var avatarBase: ImageView
    private lateinit var avatarHair: ImageView
    private lateinit var avatarClothes: ImageView
    private lateinit var avatarAccessory: ImageView

    private val genderList = listOf(
        R.drawable.avatar_boy,
        R.drawable.avatar_girl
    )

    private val hairList = listOf(
        R.drawable.avatar_hair1,
        R.drawable.avatar_hair2
    )

    private val clothesList = listOf(
        R.drawable.clothes_body_1,
        R.drawable.clothes_body_2
    )

    // 小物：画像ID + (Xオフセット, Yオフセット, 拡大率)
    private val accessoryList = listOf(
        Triple(R.drawable.accessory_ribbon, Pair(376f, 600f), 1.0f),
        Triple(R.drawable.accessory_tiara, Pair(376f, 20f), 2.4f),
        Triple(R.drawable.accessory_necklace, Pair(376f, 600f), 1.0f)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_avatar_edit)

        avatarBase = findViewById(R.id.avatarBase)
        avatarHair = findViewById(R.id.avatarHair)
        avatarClothes = findViewById(R.id.avatarClothes)
        avatarAccessory = findViewById(R.id.avatarAccessory)

        findViewById<Button>(R.id.genderButton).setOnClickListener {
            showOptions("性別を選択", genderList) { resId ->
                avatarBase.setImageResource(resId)
            }
        }

        findViewById<Button>(R.id.hairButton).setOnClickListener {
            showOptions("髪型を選択", hairList) { resId ->
                avatarHair.setImageResource(resId)
                avatarHair.visibility = View.VISIBLE
            }
        }

        findViewById<Button>(R.id.clothesButton).setOnClickListener {
            showOptions("服を選択", clothesList) { resId ->
                avatarClothes.setImageResource(resId)
            }
        }

        findViewById<Button>(R.id.accessoryButton).setOnClickListener {
            showAccessoryOptions("小物を選択", accessoryList)
        }
    }

    private fun showOptions(title: String, list: List<Int>, onSelected: (Int) -> Unit) {
        val items = list.mapIndexed { index, _ -> "パターン${index + 1}" }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(items) { _, which ->
                onSelected(list[which])
            }
            .show()
    }

    private fun showAccessoryOptions(title: String, list: List<Triple<Int, Pair<Float, Float>, Float>>) {
        val items = list.mapIndexed { index, _ -> "パターン${index + 1}" }.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle(title)
            .setItems(items) { _, which ->
                val (resId, offset, scale) = list[which]
                avatarAccessory.setImageResource(resId)
                avatarAccessory.visibility = View.VISIBLE
                avatarAccessory.translationX = offset.first
                avatarAccessory.translationY = offset.second
                avatarAccessory.scaleX = scale
                avatarAccessory.scaleY = scale
            }
            .show()
    }
}
