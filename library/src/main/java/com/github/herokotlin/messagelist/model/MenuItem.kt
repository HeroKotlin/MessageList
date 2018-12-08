package com.github.herokotlin.messagelist.model

data class MenuItem (
    var text: String,
    var onClick: () -> Unit
)