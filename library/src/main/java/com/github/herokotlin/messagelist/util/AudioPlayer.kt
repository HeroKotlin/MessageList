package com.github.herokotlin.messagelist.util

import android.media.AudioManager
import android.media.MediaPlayer
import android.util.Log
import java.io.IOException

object AudioPlayer {

    private var url = ""

    private val player = MediaPlayer()

    private val listeners = mutableListOf<AudioPlayerCallback>()

    init {
        player.setOnPreparedListener {
            player.start()
            listeners.forEach {
                it.onPlay(url)
            }
        }
        player.setOnErrorListener { _, _, _ ->
            stop()
            true
        }
        player.setOnCompletionListener {
            stop()
        }
    }

    fun play(url: String) {

        stop()

        try {
            player.setDataSource(url)
            player.prepareAsync()
            listeners.forEach {
                it.onLoad(url)
            }
            this.url = url
        }
        catch (e: IllegalArgumentException) {

        }
        catch (e: IOException) {

        }

    }

    fun stop() {

        if (url.isBlank()) {
            return
        }

        if (player.isPlaying) {
            player.stop()
        }

        listeners.forEach {
            it.onStop(url)
        }

        player.reset()

        url = ""

    }

    fun isPlaying(url: String): Boolean {

        return this.url.isNotBlank() && this.url == url

    }

    fun addListener(listener: AudioPlayerCallback) {

        listeners.add(listener)

    }

}