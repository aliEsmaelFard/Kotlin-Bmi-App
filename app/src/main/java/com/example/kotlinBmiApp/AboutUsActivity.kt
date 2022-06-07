package com.example.kotlinBmiApp

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about__us__activty)
        this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        val imageButton = findViewById<ImageButton>(R.id.back_image)
        imageButton.setOnClickListener { onBackPressed() }

        val cardView = findViewById<MaterialCardView>(R.id.about_us_card)
        cardView.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            val strTo =
                arrayOf("aliesmaelfard@gmail.com")
            intent.putExtra(Intent.EXTRA_EMAIL, strTo)
            intent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
            intent.putExtra(Intent.EXTRA_TEXT, "Body")
            intent.type = "message/rfc822"
            intent.setPackage("com.google.android.gm")
            startActivity(intent)
        }

    }
}