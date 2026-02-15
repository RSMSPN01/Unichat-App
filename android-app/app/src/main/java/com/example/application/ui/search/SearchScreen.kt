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
import com.example.application.ui.navigation.BottomScreen
import com.example.application.ui.viewmodel.AppViewModel

@Composable
fun SearchScreen(
    navController: NavHostController,
    viewModel: AppViewModel
) {

    var query by remember { mutableStateOf("") }
    var searchHistory by remember { mutableStateOf(listOf<String>()) }
    var showDialog by remember { mutableStateOf(false) }

    val users = List(50) { "User $it" }

    val filteredUsers = if (query.isNotBlank()) {
        users.filter {
            it.contains(
                query,
                ignoreCase = true
            )
        }
    } else {
        emptyList()
    }

    Column {

        // 🔍 SEARCH BAR
        OutlinedTextField(
            value = query,
            onValueChange = { query = it },
            placeholder = { Text("Search users") },
            singleLine = true,
            shape = RoundedCornerShape(30.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            },
            trailingIcon = {
                if (query.isNotBlank()) {
                    IconButton(onClick = { query = "" }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear"
                        )
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        // 🔥 IF TYPING → SHOW RESULTS
        if (query.isNotBlank()) {

            LazyColumn {
                items(filteredUsers) { user ->

                    Text(
                        text = user,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .clickable {

                                // Add to history if not exists
                                if (!searchHistory.contains(user)) {
                                    searchHistory = listOf(user) + searchHistory
                                }

                                navController.navigate(
                                    BottomScreen.Profile.createRoute(user)
                                )
                            }
                    )
                }
            }

        } else {

            // 🔥 SHOW HISTORY
            if (searchHistory.isNotEmpty()) {

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
                    items(searchHistory) { item ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {

                            Text(
                                text = item,
                                modifier = Modifier.clickable {
                                    navController.navigate(
                                        BottomScreen.Profile.createRoute(item)
                                    )
                                }
                            )

                            IconButton(
                                onClick = {
                                    searchHistory =
                                        searchHistory.filter { it != item }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Remove"
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // 🔥 CONFIRM DIALOG
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    searchHistory = emptyList()
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

