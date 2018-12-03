package com.github.herokotlin.messagelist.util

interface AudioPlayerCallback {

    fun onLoad(id: String)

    fun onPlay(id: String)

    fun onStop(id: String)

}