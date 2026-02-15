package com.example.application.ui.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
    val users = viewModel.getSortedChatUsers()

    Column(modifier = Modifier.fillMaxSize()) {

        Text(
            text = "Chats",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        if (users.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No chats yet")
            }

        } else {

            LazyColumn {
                items(users) { user ->

                    val lastMessage = viewModel.getLastMessage(user)

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("chat/$user")
                            }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(modifier = Modifier.weight(1f)) {

                            Text(
                                text = user,
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
