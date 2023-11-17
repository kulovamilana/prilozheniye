package com.example.musicapp

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlayerActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    private var isMusicPlaying = false
    private lateinit var textView2: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        val imageView = findViewById<ImageView>(R.id.imageView3)
        val imageView4: ImageView = findViewById(R.id.imageView4)
        textView2 = findViewById(R.id.textView2)

        val rotateAnimation = RotateAnimation(
            0f, 360f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )

        rotateAnimation.duration = 20000
        rotateAnimation.repeatCount = Animation.INFINITE
        rotateAnimation.interpolator = android.view.animation.LinearInterpolator()

        imageView.startAnimation(rotateAnimation)

        val audioUri: Uri? = intent.getParcelableExtra("audioUri")
        if (audioUri != null) {
            initializeMediaPlayer()
            playAudio(audioUri)

            // Set the file name without extension to textView2
            val fileNameWithoutExtension = getFileNameWithoutExtension(audioUri)
            textView2.text = fileNameWithoutExtension
        }

        imageView4.setOnClickListener {
            if (isMusicPlaying) {
                stopAudio()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            finish()
        }
    }

    private fun initializeMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
            mediaPlayer?.setOnCompletionListener {
                isMusicPlaying = false
            }
        }
    }

    private fun playAudio(uri: Uri) {
        mediaPlayer?.reset()
        mediaPlayer?.setDataSource(this, uri)
        mediaPlayer?.prepare()
        mediaPlayer?.start()
        isMusicPlaying = true
    }

    private fun stopAudio() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isMusicPlaying = false
    }

    override fun onStop() {
        super.onStop()
        if (isMusicPlaying) {
            stopAudio()
        }
    }

    private fun getFileNameWithoutExtension(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val displayNameIndex = cursor?.getColumnIndexOrThrow("_display_name")
        cursor?.moveToFirst()
        val fileNameWithExtension = cursor?.getString(displayNameIndex ?: 0)
        cursor?.close()

        // Remove extension
        val dotIndex = fileNameWithExtension?.lastIndexOf(".")
        return if (dotIndex != null && dotIndex > 0) {
            fileNameWithExtension.substring(0, dotIndex)
        } else {
            fileNameWithExtension ?: ""
        }
    }
}