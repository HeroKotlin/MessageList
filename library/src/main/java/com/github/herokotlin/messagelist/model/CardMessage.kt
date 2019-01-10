package com.github.herokotlin.messagelist.model

import com.github.herokotlin.messagelist.enum.MessageStatus

data class CardMessage(
    override var id: String,
    override var user: User,
    override var status: MessageStatus,
    override var time: String,
    override var canRecall: Boolean,
    override var canDelete: Boolean,
    var thumbnail: String,
    var title: String,
    var desc: String,
    var label: String,
    var link: String
): Message