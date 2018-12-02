package com.github.herokotlin.messagelist.model

import com.github.herokotlin.messagelist.enum.MessageStatus

data class PostMessage(
    override var id: String,
    override var user: User,
    override var status: MessageStatus,
    override var time: String,
    var thumbnail: String,
    var title: String,
    var desc: String,
    var label: String,
    var link: String
): Message