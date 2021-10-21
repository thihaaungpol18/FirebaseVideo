package com.thiha.android4k.testfirebasevideostreaming.view.activity

import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView
import com.thiha.android4k.testfirebasevideostreaming.R

class FullscreenVideoActivity : AppCompatActivity() {

    private lateinit var playView: PlayerView
    private lateinit var simpleExoPlayer: SimpleExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestFullscreen()
        setContentView(R.layout.activity_fullscreen_video)
    }

    override fun onResume() {
        super.onResume()
        val url = intent.extras?.get("url")
        playView = findViewById(R.id.player_view)
        val item = MediaItem.fromUri(Uri.parse(url.toString()))
        simpleExoPlayer = SimpleExoPlayer.Builder(this)
            .build()
        simpleExoPlayer.setMediaItem(item)
        playView.player = simpleExoPlayer
        simpleExoPlayer.playWhenReady = true
        simpleExoPlayer.prepare()
    }

    private fun requestFullscreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}