package com.example.application.ui.chat

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Message(
    val text: String,
    val isMe: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) {
    val time: String
        get() {
            val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
            return sdf.format(Date(timestamp))
        }
}
