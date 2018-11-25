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

    fun onContentLongPress(message: Message) {

    }

    fun onFailureClick(message: Message) {

    }

    fun onLoadMore() {

    }

}