package com.github.herokotlin.messagelist.model

import com.github.herokotlin.messagelist.enum.MessageStatus

data class TextMessage(
    override var id: String,
    override var user: User,
    override var status: MessageStatus,
    override var time: String,
    var text: String
): Message