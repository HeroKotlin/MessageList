package com.github.herokotlin.messagelist.model

import com.github.herokotlin.messagelist.enum.MessageStatus

interface Message {
    var id: String
    var user: User
    var status: MessageStatus
    var time: String
    var canCopy: Boolean
    var canShare: Boolean
    var canRecall: Boolean
    var canDelete: Boolean
}