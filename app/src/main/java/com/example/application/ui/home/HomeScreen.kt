package com.example.application.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.application.model.Post
import com.example.application.ui.viewmodel.AppViewModel

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: AppViewModel
) {

    val users = viewModel.getUsers()
    val posts = viewModel.getPosts()

    var showDialog by remember { mutableStateOf(false) }
    var expandedPostId by remember { mutableStateOf<Int?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                expandedPostId = null
            }
    ) {

        LazyColumn {

            item {
                StoriesSection(
                    users = users,
                    navController = navController
                )
            }

            items(posts) { post ->
                PostCard(
                    post = post,
                    viewModel = viewModel,
                    navController = navController,
                    isExpanded = expandedPostId == post.id,
                    onToggleComments = {
                        expandedPostId =
                            if (expandedPostId == post.id) null else post.id
                    }
                )
            }
        }

        FloatingActionButton(
            onClick = { showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+")
        }

        if (showDialog) {
            CreatePostDialog(
                onDismiss = { showDialog = false },
                onPost = {
                    viewModel.addPost(it)
                    showDialog = false
                }
            )
        }
    }
}

@Composable
fun StoriesSection(
    users: List<com.example.application.model.User>,
    navController: NavController
) {
    LazyRow(modifier = Modifier.padding(vertical = 12.dp)) {

        items(users) { user ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 12.dp)
                    .clickable {
                        navController.navigate("profile/${user.id}")
                    }
            ) {

                AsyncImage(
                    model = user.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(text = user.username)
            }
        }
    }
}

@Composable
fun PostCard(
    post: Post,
    viewModel: AppViewModel,
    navController: NavController,
    isExpanded: Boolean,
    onToggleComments: () -> Unit
) {

    val user = viewModel.getUserById(post.ownerId)
    val isLiked = post.likedBy.contains(viewModel.currentUserId)
    var commentText by remember { mutableStateOf("") }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable {
                    navController.navigate("profile/${post.ownerId}")
                }
            ) {

                AsyncImage(
                    model = user?.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(text = user?.username ?: post.ownerId)
            }


            Spacer(modifier = Modifier.height(12.dp))

            // 🔥 Post Text
            post.content?.let {
                Text(text = it)
            }

            // 🔥 Post Image
            post.imageUrl?.let {
                Spacer(modifier = Modifier.height(12.dp))

                AsyncImage(
                    model = it,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 🔥 Like + Comment Row
            Row(verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = if (isLiked) "❤️" else "🤍",
                    modifier = Modifier
                        .clickable {
                            viewModel.toggleLike(post.id)
                        }
                        .padding(4.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))
                Text("${post.likeCount}")

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "💬",
                    modifier = Modifier
                        .clickable {
                            onToggleComments()
                        }
                        .padding(4.dp)
                )

                Spacer(modifier = Modifier.width(6.dp))
                Text("${post.comments.size}")
            }

            // 🔥 Expand Comments
            if (isExpanded) {

                Spacer(modifier = Modifier.height(12.dp))

                post.comments.forEach { comment ->

                    val commentUser = viewModel.getUserById(comment.userId)

                    Column(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {

                        Text(
                            text = commentUser?.username ?: comment.userId,
                            style = MaterialTheme.typography.labelSmall
                        )

                        Text(text = comment.text)
                    }
                }


                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = commentText,
                    onValueChange = { commentText = it },
                    placeholder = { Text("Add a comment...") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        if (commentText.isNotBlank()) {
                            viewModel.addComment(post.id, commentText)
                            commentText = ""
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Post")
                }
            }
        }
    }
}

@Composable
fun CreatePostDialog(
    onDismiss: () -> Unit,
    onPost: (String) -> Unit
) {

    var text by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Text(
                text = "Post",
                modifier = Modifier
                    .clickable {
                        if (text.isNotBlank()) {
                            onPost(text)
                        }
                    }
                    .padding(8.dp)
            )
        },
        dismissButton = {
            Text(
                text = "Cancel",
                modifier = Modifier
                    .clickable { onDismiss() }
                    .padding(8.dp)
            )
        },
        text = {
            Column {
                Text("Create Post")
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Write something...") }
                )
            }
        }
    )
}
