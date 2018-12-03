package com.github.herokotlin.messagelist.util

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import java.io.IOException

object AudioPlayer: SensorEventListener {

    private var id = ""
    private var url = ""

    private val player = MediaPlayer()

    private val listeners = mutableListOf<AudioPlayerCallback>()

    private lateinit var audioManager: AudioManager
    private lateinit var sensorManager: SensorManager

    // 传感器实例
    private lateinit var sensor: Sensor

    fun init(audioManager: AudioManager, sensorManager: SensorManager) {

        this.audioManager = audioManager
        this.sensorManager = sensorManager
        this.sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        player.setOnPreparedListener {
            player.start()
            listeners.forEach {
                it.onPlay(id)
            }
        }
        player.setOnErrorListener { _, _, _ ->
            stop()
            true
        }
        player.setOnCompletionListener {
            stop()
        }

        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun destroy() {
        stop()
        sensorManager.unregisterListener(this)
    }

    fun play(id: String, url: String) {

        stop()

        try {
            player.setDataSource(url)
            player.prepareAsync()
            listeners.forEach {
                it.onLoad(id)
            }
            this.id = id
            this.url = url
        }
        catch (e: IllegalArgumentException) {

        }
        catch (e: IOException) {

        }

    }

    fun stop() {

        if (id.isBlank()) {
            return
        }

        if (player.isPlaying) {
            player.stop()
        }

        listeners.forEach {
            it.onStop(id)
        }

        player.reset()

        id = ""
        url = ""

        useSpeaker()

    }

    fun isPlaying(id: String): Boolean {

        return this.id.isNotBlank() && this.id == id

    }

    fun addListener(listener: AudioPlayerCallback) {

        listeners.add(listener)

    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            // 本来 == 就可以
            // 但是某些安卓机的值比最大值还要大...
            if (event.values[0] >= sensor.maximumRange) {
                useSpeaker()
            }
            else {
                useEar()
            }
        }
    }

    fun useSpeaker() {

        audioManager.isSpeakerphoneOn = true
        audioManager.mode = AudioManager.MODE_NORMAL

        // 设置音量，解决有些机型切换后没声音或者声音突然变大的问题
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FX_KEY_CLICK)

    }

    fun useEar() {

        audioManager.isSpeakerphoneOn = false

        // 5.0 以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
        }
        else {
            audioManager.mode = AudioManager.MODE_IN_CALL
        }

        // 设置音量，解决有些机型切换后没声音或者声音突然变大的问题
        audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), AudioManager.FX_KEY_CLICK)

    }

}