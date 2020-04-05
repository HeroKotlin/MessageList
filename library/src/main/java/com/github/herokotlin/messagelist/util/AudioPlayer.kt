package com.github.herokotlin.messagelist.util

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.PowerManager
import java.io.IOException

internal class AudioPlayer: SensorEventListener {

    var onPlay: (() -> Unit)? = null

    private var id = ""
    private var url = ""
    private var maxDuration = 0

    private val player = MediaPlayer()

    private val listeners = mutableListOf<AudioPlayerCallback>()

    private lateinit var audioManager: AudioManager
    private lateinit var sensorManager: SensorManager
    private lateinit var powerManager: PowerManager

    // 传感器实例
    private var sensor: Sensor? = null

    // 控制屏幕开关
    private var wakeLock: PowerManager.WakeLock? = null

    fun init(context: Context, maxDuration: Int) {

        this.maxDuration = maxDuration

        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            wakeLock = powerManager.newWakeLock(PowerManager.PROXIMITY_SCREEN_OFF_WAKE_LOCK, "HeroKotlin:MessageList")
        }

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

    }

    fun destroy() {
        stop()
    }

    fun play(id: String, url: String) {

        onPlay?.invoke()

        stop()

        try {

            player.setDataSource(url)
            player.prepareAsync()
            listeners.forEach {
                it.onLoad(id)
            }

            this.id = id
            this.url = url

            useSpeaker()

            sensor?.let {
                sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
            }

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

        sensor?.let {
            sensorManager.unregisterListener(this, it)
        }

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
            if (event.values[0] >= event.sensor.maximumRange) {
                if (useSpeaker()) {
                    wakeLock?.release()
                }
            }
            else if (useEar()) {
                wakeLock?.acquire(maxDuration * 1000L)
            }
        }
    }

    private fun useSpeaker(): Boolean {

        if (!audioManager.isSpeakerphoneOn) {
            audioManager.isSpeakerphoneOn = true

            audioManager.mode = AudioManager.MODE_NORMAL

            // 设置音量，解决有些机型切换后没声音或者声音突然变大的问题
            audioManager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                audioManager.getStreamVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FX_KEY_CLICK
            )
            return true
        }

        return false

    }

    private fun useEar(): Boolean {

        if (audioManager.isSpeakerphoneOn) {

            audioManager.isSpeakerphoneOn = false

            // 5.0 以上
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
                audioManager.setStreamVolume(
                    AudioManager.MODE_IN_COMMUNICATION,
                    audioManager.getStreamMaxVolume(AudioManager.MODE_IN_COMMUNICATION),
                    0
                )
            }
            else {
                audioManager.mode = AudioManager.MODE_IN_CALL
                audioManager.setStreamVolume(
                    AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.FX_KEY_CLICK
                )
            }

            return true
        }

        return false

    }

}