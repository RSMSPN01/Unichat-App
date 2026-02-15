package com.example.application.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.application.ui.viewmodel.AppViewModel

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: AppViewModel
) {

    var query by remember { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }

    val users = viewModel.getUsers()

    val filteredUsers = if (query.isNotBlank()) {
        users.filter {
            it.username.contains(query, ignoreCase = true)
                    && it.id != viewModel.currentUserId
        }
    } else emptyList()


    Column {

        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search users") },
            singleLine = true,
            shape = RoundedCornerShape(30.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        if (query.isNotBlank()) {

            LazyColumn {
                items(filteredUsers) { user ->

                    Text(
                        text = user.username,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {

                                viewModel.addSearchQuery(user.id)

                                navController.navigate("profile/${user.id}")
                            }
                    )
                }
            }

        } else {

            if (viewModel.searchHistory.isNotEmpty()) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Recent Searches")

                    Text(
                        text = "Clear All",
                        modifier = Modifier.clickable {
                            showDialog = true
                        }
                    )
                }

                LazyColumn {
                    items(viewModel.searchHistory) { userId ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = userId,
                                modifier = Modifier.clickable {
                                    navController.navigate("profile/$userId")
                                }
                            )

                            IconButton(
                                onClick = {
                                    viewModel.removeSearchQuery(userId)
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearSearchHistory()
                    showDialog = false
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            },
            title = { Text("Clear history?") },
            text = { Text("Are you sure you want to delete all search history?") }
        )
    }
}
