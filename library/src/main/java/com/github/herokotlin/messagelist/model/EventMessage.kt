package com.github.herokotlin.messagelist.model

import com.github.herokotlin.messagelist.enum.MessageStatus

data class EventMessage(
    override var id: String,
    override var user: User,
    override var status: MessageStatus,
    override var time: String,
    var event: String
): Message