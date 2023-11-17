package com.example.musicapp

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val PICK_AUDIO_FILE_REQUEST = 1
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)


        val btnOpenFilePicker: Button = findViewById(R.id.btnOpenFilePicker)

        btnOpenFilePicker.setOnClickListener {
            openFilePicker()
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "audio/*"
        startActivityForResult(intent, PICK_AUDIO_FILE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_AUDIO_FILE_REQUEST && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                if (isValidAudioFile(uri)) {
                    handleSelectedAudioFile(uri)
                } else {
                    Toast.makeText(
                        this,
                        "Приложение не поддержвиает данный формат файла",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun isValidAudioFile(uri: Uri): Boolean {
        val contentResolver = contentResolver
        val mimeType = contentResolver.getType(uri)
        return mimeType?.startsWith("audio/") == true
    }

    private fun handleSelectedAudioFile(uri: Uri) {
        val playerIntent = Intent(this, PlayerActivity::class.java)
        playerIntent.putExtra("audioUri", uri)
        startActivity(playerIntent)

        finish()
    }

    fun onOpenFilePickerButtonClick(view: View) {
        openFilePicker()

    }

    override fun onDestroy() {
        mediaPlayer?.release()
        super.onDestroy()
    }
}
