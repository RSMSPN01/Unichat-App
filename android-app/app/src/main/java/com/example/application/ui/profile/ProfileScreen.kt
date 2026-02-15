package com.example.application.ui.profile
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.application.ui.viewmodel.AppViewModel

@Composable
fun ProfileScreen(
    username: String,
    viewModel: AppViewModel
) {

    val currentUser = viewModel.currentUser
    val isOwnProfile = username == currentUser
    val isFollowing = viewModel.isFollowing(username)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = username,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isOwnProfile) {

            Button(onClick = { /* Edit profile later */ }) {
                Text("Edit Profile")
            }

        } else {

            Row {

                Button(
                    onClick = {
                        if (isFollowing)
                            viewModel.unfollowUser(username)
                        else
                            viewModel.followUser(username)
                    }
                ) {
                    Text(
                        if (isFollowing) "Unfollow"
                        else "Follow"
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                if (isFollowing) {
                    OutlinedButton(
                        onClick = {
                            // Later navigate to chat
                        }
                    ) {
                        Text("Message")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Placeholder for posts grid
        Text("Posts will appear here")
    }
}
