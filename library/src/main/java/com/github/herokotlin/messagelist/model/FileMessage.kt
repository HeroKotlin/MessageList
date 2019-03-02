package com.github.herokotlin.messagelist.model

import com.github.herokotlin.messagelist.enum.MessageStatus
import com.github.herokotlin.messagelist.enum.FileType

data class FileMessage(
    override var id: String,
    override var user: User,
    override var status: MessageStatus,
    override var time: String,
    override var canCopy: Boolean,
    override var canShare: Boolean,
    override var canRecall: Boolean,
    override var canDelete: Boolean,
    var type: FileType,
    var title: String,
    var desc: String,
    var link: String
): Message