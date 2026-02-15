package com.example.application.ui.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.application.data.FakeRepository
import com.example.application.data.Repository
import com.example.application.model.Message
import com.example.application.model.Post
import com.example.application.model.User
import com.example.application.model.Comment


class AppViewModel(
    private val repository: Repository = FakeRepository()
) : ViewModel() {

    // ---------------- CURRENT USER ----------------

    val currentUserId: String = "user_0"

    // ---------------- FOLLOW ----------------

    private val followingUsers = mutableStateListOf<String>()

    fun getFollowingUsers() = followingUsers

    fun followUser(targetUserId: String) {
        if (targetUserId == currentUserId) return
        if (!followingUsers.contains(targetUserId)) {
            followingUsers.add(targetUserId)
        }
    }

    fun unfollowUser(targetUserId: String) {
        followingUsers.remove(targetUserId)
    }

    fun isFollowing(targetUserId: String) =
        followingUsers.contains(targetUserId)

    // ---------------- USERS ----------------

    fun getUsers(): List<User> = repository.getUsers()

    fun getUserById(userId: String): User? {
        return repository.getUsers().find { it.id == userId }
    }

    // ---------------- POSTS ----------------

    fun getPosts() = repository.getPosts()

    fun getPostsByUser(userId: String) =
        repository.getPosts().filter { it.ownerId == userId }

    fun addPost(content: String) {
        val post = Post(
            id = System.currentTimeMillis().toInt(),
            ownerId = currentUserId,
            content = content
        )
        repository.addPost(post)
    }

    fun toggleLike(postId: Int) {
        val post = repository.getPosts().find { it.id == postId } ?: return

        val updatedLikes =
            if (post.likedBy.contains(currentUserId)) {
                post.likedBy - currentUserId
            } else {
                post.likedBy + currentUserId
            }

        repository.updatePost(
            post.copy(likedBy = updatedLikes)
        )
    }
    fun addComment(postId: Int, text: String) {

        val post = repository.getPosts().find { it.id == postId } ?: return

        val newComment = Comment(
            id = System.currentTimeMillis().toInt(),
            userId = currentUserId,
            text = text
        )

        val updatedComments = post.comments + newComment

        repository.updatePost(
            post.copy(comments = updatedComments)
        )
    }

    // ---------------- SEARCH ----------------

    private val _searchHistory = mutableStateListOf<String>()
    val searchHistory: List<String> get() = _searchHistory

    fun addSearchQuery(userId: String) {
        if (!_searchHistory.contains(userId)) {
            _searchHistory.add(0, userId)
        }
    }

    fun removeSearchQuery(userId: String) {
        _searchHistory.remove(userId)
    }

    fun clearSearchHistory() {
        _searchHistory.clear()
    }

    // ---------------- CHAT ----------------

    fun getMessages(userId: String) =
        repository.getMessages(userId)

    fun sendMessage(userId: String, text: String) {
        if (userId == currentUserId) return

        val message = Message(
            text = text,
            isMe = true,
            timestamp = System.currentTimeMillis()
        )

        repository.sendMessage(userId, message)
    }

    fun getLastMessage(userId: String) =
        repository.getLastMessage(userId)

    fun getSortedChatUsers(): List<String> {
        return getFollowingUsers().sortedByDescending { userId ->
            repository.getLastMessage(userId)?.timestamp ?: 0L
        }
    }
}
