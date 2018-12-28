package com.github.herokotlin.messagelist

import com.github.herokotlin.messagelist.model.Message

interface MessageListCallback {

    fun onListClick() {

    }

    fun onUserAvatarClick(message: Message) {

    }

    fun onUserNameClick(message: Message) {

    }

    fun onContentClick(message: Message) {

    }

    fun onCopyClick(message: Message) {

    }

    fun onShareClick(message: Message) {

    }

    fun onFailureClick(message: Message) {

    }

    fun onLinkClick(link: String) {

    }

    fun onLoadMore() {

    }

    fun onUseAudio() {

    }

}