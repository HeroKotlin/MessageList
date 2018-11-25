package com.github.herokotlin.messagelist.util

interface AudioPlayerCallback {

    fun onLoad(url: String)

    fun onPlay(url: String)

    fun onStop(url: String)

}