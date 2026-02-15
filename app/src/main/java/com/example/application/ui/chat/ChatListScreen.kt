package com.example.application.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.application.ui.viewmodel.AppViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ChatListScreen(
    navController: NavController,
    viewModel: AppViewModel
) {

    val chatUserIds = viewModel.getSortedChatUsers()
    val allUsers = viewModel.getUsers()

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Chats",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        if (chatUserIds.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No chats yet")
            }

        } else {

            LazyColumn {
                items(chatUserIds) { userId ->

                    val lastMessage = viewModel.getLastMessage(userId)
                    val user = allUsers.find { it.id == userId }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("chat/$userId")
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(modifier = Modifier.weight(1f)) {

                            // 🔥 FIXED: Show username instead of userId
                            Text(
                                text = user?.username ?: userId,
                                style = MaterialTheme.typography.titleMedium
                            )

                            Text(
                                text = lastMessage?.text ?: "Start conversation",
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1
                            )
                        }

                        val formattedTime = lastMessage?.timestamp?.let {
                            SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                                .format(Date(it))
                        } ?: ""

                        Text(
                            text = formattedTime,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
