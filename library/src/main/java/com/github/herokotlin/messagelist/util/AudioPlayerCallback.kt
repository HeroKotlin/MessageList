package com.github.herokotlin.messagelist.util

internal interface AudioPlayerCallback {

    fun onLoad(id: String)

    fun onPlay(id: String)

    fun onStop(id: String)

}