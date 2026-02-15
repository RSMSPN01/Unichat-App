package com.example.application.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.application.ui.viewmodel.AppViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    profileUserId: String,
    currentUserId: String,
    viewModel: AppViewModel = viewModel()
) {

    val user = viewModel.getUserById(profileUserId)
    val isOwnProfile = profileUserId == currentUserId
    val isFollowing = viewModel.isFollowing(profileUserId)

    val userPosts = viewModel.getPostsByUser(profileUserId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Username
        Text(
            text = user?.username ?: "Unknown",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons
        if (isOwnProfile) {

            Button(onClick = { /* Edit profile later */ }) {
                Text("Edit Profile")
            }

        } else {

            Row {

                Button(
                    onClick = {
                        if (isFollowing)
                            viewModel.unfollowUser(profileUserId)
                        else
                            viewModel.followUser(profileUserId)
                    }
                ) {
                    Text(if (isFollowing) "Unfollow" else "Follow")
                }

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedButton(
                    onClick = {
                        if (profileUserId != currentUserId) {
                            navController.navigate("chat/$profileUserId")
                        }
                    }
                ) {
                    Text("Message")
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // POSTS GRID
        if (userPosts.isEmpty()) {

            Text("No posts yet")

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
                            .background(MaterialTheme.colorScheme.primaryContainer)
                    )
                }
            }
        }
    }
}
