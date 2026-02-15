package com.example.application.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.example.application.ui.viewmodel.AppViewModel
import com.example.application.ui.home.PostCard
import com.example.application.model.Post

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    profileUserId: String,
    currentUserId: String,
    viewModel: AppViewModel
) {

    val users = viewModel.getUsers()
    val user = users.find { it.id == profileUserId }
    val currentUser = users.find { it.id == currentUserId }

    val isOwnProfile = profileUserId == currentUserId
    val isFollowing = currentUser?.following?.contains(profileUserId) == true

    val userPosts = viewModel.getPostsByUser(profileUserId)

    var selectedPost by remember { mutableStateOf<Post?>(null) }

    Column(modifier = Modifier.fillMaxSize()) {

        // 🔥 HEADER
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            AsyncImage(
                model = user?.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = user?.username ?: "Unknown",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${userPosts.size}")
                    Text("Posts")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${user?.followers?.size ?: 0}")
                    Text("Followers")
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${user?.following?.size ?: 0}")
                    Text("Following")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isOwnProfile) {

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Edit Profile")
                }

            } else {

                Row(modifier = Modifier.fillMaxWidth()) {

                    Button(
                        onClick = {
                            if (isFollowing)
                                viewModel.unfollowUser(profileUserId)
                            else
                                viewModel.followUser(profileUserId)
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (isFollowing) "Unfollow" else "Follow")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    OutlinedButton(
                        onClick = {
                            navController.navigate("chat/$profileUserId")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Message")
                    }
                }
            }
        }

        Divider()

        // 🔥 POSTS GRID
        if (userPosts.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No posts yet")
            }

        } else {

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(4.dp)
            ) {

                items(userPosts) { post ->

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .aspectRatio(1f)
                            .clickable { selectedPost = post }
                    ) {

                        if (post.imageUrl != null) {

                            AsyncImage(
                                model = post.imageUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(6.dp)),
                                contentScale = ContentScale.Crop
                            )

                        } else {

                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = post.content ?: "",
                                    maxLines = 2
                                )
                            }
                        }

                        // 🔥 Overlay
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Row {
                                Text("❤️ ${post.likeCount}")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("💬 ${post.comments.size}")
                            }
                        }
                    }
                }
            }
        }
    }

    // 🔥 Bottom Sheet
    if (selectedPost != null) {

        ModalBottomSheet(
            onDismissRequest = { selectedPost = null }
        ) {

            PostCard(
                post = selectedPost!!,
                viewModel = viewModel,
                navController = navController,
                isExpanded = true,
                onToggleComments = {}
            )
        }
    }
}
