package com.example.application.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.example.application.ui.chat.Message
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AppViewModel : ViewModel() {

    val currentUser: String = "User 0"

    // ------------------------
    // USERS
    // ------------------------

    private val allUsers = List(20) { "User $it" }

    fun getAllUsers(): List<String> = allUsers

    // ------------------------
    // FOLLOW SYSTEM
    // ------------------------

    private val followingUsers = mutableStateListOf<String>()

    fun isFollowing(username: String): Boolean {
        return followingUsers.contains(username)
    }

    fun followUser(username: String) {
        if (!followingUsers.contains(username)) {
            followingUsers.add(username)
        }
    }

    fun unfollowUser(username: String) {
        followingUsers.remove(username)
    }

    fun getFollowingUsers(): List<String> {
        return followingUsers
    }

    // ------------------------
    // CHAT SYSTEM
    // ------------------------
    private val chatMessages =
        mutableMapOf<String, SnapshotStateList<Message>>()

    fun getMessages(username: String): SnapshotStateList<Message> {
        return chatMessages.getOrPut(username) {
            mutableStateListOf()
        }
    }
    fun getLastMessage(username: String): Message? {
        return chatMessages[username]?.lastOrNull()
    }

    fun sendMessage(username: String, text: String) {

        val message = Message(
            text = text,
            isMe = true,
            time = getCurrentTime()
        )

        val messages = chatMessages.getOrPut(username) {
            mutableStateListOf()
        }

        messages.add(message)
    }
    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(Date())
    }
}
