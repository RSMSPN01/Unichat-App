package com.example.application.data

import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.application.MyApplication
import com.example.application.model.Message
import com.example.application.model.Post
import com.example.application.model.User

object FakeRepository : Repository {

    private val prefs = MyApplication.instance
        .getSharedPreferences("uniconnect_local", Context.MODE_PRIVATE)

    // ---------------- USERS ----------------

    private val users = mutableStateListOf(
        User("user_0", "Praveen", "https://i.pravatar.cc/150?img=1"),
        User("user_1", "Rohan", "https://i.pravatar.cc/150?img=2"),
        User("user_2", "Aman", "https://i.pravatar.cc/150?img=3"),
        User("user_3", "Neha", "https://i.pravatar.cc/150?img=4"),
        User("user_4", "Arjun", "https://i.pravatar.cc/150?img=5")
    )

    override fun getUsers(): SnapshotStateList<User> = users

    init {
        loadFollowing()
    }

    // ---------------- FOLLOW SYSTEM ----------------

    fun followUser(currentUserId: String, targetUserId: String) {

        val currentIndex = users.indexOfFirst { it.id == currentUserId }
        val targetIndex = users.indexOfFirst { it.id == targetUserId }

        if (currentIndex == -1 || targetIndex == -1) return

        val currentUser = users[currentIndex]
        val targetUser = users[targetIndex]

        if (!currentUser.following.contains(targetUserId)) {

            val updatedCurrentUser = currentUser.copy(
                following = (currentUser.following + targetUserId).toMutableSet()
            )

            val updatedTargetUser = targetUser.copy(
                followers = (targetUser.followers + currentUserId).toMutableSet()
            )

            users[currentIndex] = updatedCurrentUser
            users[targetIndex] = updatedTargetUser

            saveFollowing()
        }
    }

    fun unfollowUser(currentUserId: String, targetUserId: String) {

        val currentIndex = users.indexOfFirst { it.id == currentUserId }
        val targetIndex = users.indexOfFirst { it.id == targetUserId }

        if (currentIndex == -1 || targetIndex == -1) return

        val currentUser = users[currentIndex]
        val targetUser = users[targetIndex]

        val updatedCurrentUser = currentUser.copy(
            following = (currentUser.following - targetUserId).toMutableSet()
        )

        val updatedTargetUser = targetUser.copy(
            followers = (targetUser.followers - currentUserId).toMutableSet()
        )

        users[currentIndex] = updatedCurrentUser
        users[targetIndex] = updatedTargetUser

        saveFollowing()
    }

    private fun saveFollowing() {
        val serialized = users.joinToString("|||") { user ->
            "${user.id}###${user.following.joinToString(",")}"
        }

        prefs.edit()
            .putString("following_data", serialized)
            .apply()
    }

    private fun loadFollowing() {

        val saved = prefs.getString("following_data", null) ?: return

        saved.split("|||").forEach { item ->

            val parts = item.split("###")
            if (parts.size != 2) return@forEach

            val userId = parts[0]
            val followingList = if (parts[1].isBlank())
                emptySet()
            else
                parts[1].split(",").toSet()

            val index = users.indexOfFirst { it.id == userId }
            if (index != -1) {
                users[index] = users[index].copy(
                    following = followingList.toMutableSet()
                )
            }
        }

        // rebuild followers from following
        users.forEach { user ->
            user.following.forEach { followedId ->
                val targetIndex = users.indexOfFirst { it.id == followedId }
                if (targetIndex != -1) {
                    val target = users[targetIndex]
                    users[targetIndex] = target.copy(
                        followers = (target.followers + user.id).toMutableSet()
                    )
                }
            }
        }
    }

    // ---------------- POSTS ----------------

    private val posts = mutableStateListOf<Post>().apply {

        add(
            Post(
                id = 1,
                ownerId = "user_0",
                content = "Morning workout done 💪 Discipline > Motivation",
                imageUrl = "https://picsum.photos/500/500?random=1"
            )
        )

        add(
            Post(
                id = 2,
                ownerId = "user_1",
                content = "Late night coding session 🔥",
                imageUrl = "https://picsum.photos/500/500?random=2"
            )
        )

        add(
            Post(
                id = 3,
                ownerId = "user_2",
                content = "Consistency builds confidence.",
                imageUrl = "https://picsum.photos/500/500?random=3"
            )
        )

        add(
            Post(
                id = 4,
                ownerId = "user_3",
                content = "Sunset therapy 🌇",
                imageUrl = "https://picsum.photos/500/500?random=4"
            )
        )

        add(
            Post(
                id = 5,
                ownerId = "user_4",
                content = "Building something exciting 👀",
                imageUrl = "https://picsum.photos/500/500?random=5"
            )
        )
    }

    override fun getPosts(): SnapshotStateList<Post> = posts

    override fun addPost(post: Post) {
        posts.add(0, post)
    }

    override fun updatePost(post: Post) {
        val index = posts.indexOfFirst { it.id == post.id }
        if (index != -1) posts[index] = post
    }

    // ---------------- CHAT ----------------

    private val chatMessages =
        mutableMapOf<String, SnapshotStateList<Message>>()

    override fun getMessages(userId: String): SnapshotStateList<Message> {
        return chatMessages.getOrPut(userId) {
            loadMessages(userId)
        }
    }

    override fun sendMessage(userId: String, message: Message) {

        val messages = chatMessages.getOrPut(userId) {
            loadMessages(userId)
        }

        messages.add(message)
        saveMessages(userId)
    }

    override fun getLastMessage(userId: String): Message? {
        return chatMessages[userId]?.lastOrNull()
    }

    private fun saveMessages(userId: String) {

        val messages = chatMessages[userId] ?: return

        val serialized = messages.joinToString("|||") {
            "${it.text}###${it.isMe}###${it.timestamp}"
        }

        prefs.edit()
            .putString("chat_$userId", serialized)
            .apply()
    }

    private fun loadMessages(userId: String): SnapshotStateList<Message> {

        val saved = prefs.getString("chat_$userId", null)
            ?: return mutableStateListOf()

        val list = mutableStateListOf<Message>()

        saved.split("|||").forEach { item ->
            val parts = item.split("###")
            if (parts.size == 3) {
                list.add(
                    Message(
                        text = parts[0],
                        isMe = parts[1].toBoolean(),
                        timestamp = parts[2].toLong()
                    )
                )
            }
        }

        return list
    }
}
