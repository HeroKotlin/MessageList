package com.github.herokotlin.messagelist.model

internal data class MenuItem (
    var text: String,
    var onClick: () -> Unit
)