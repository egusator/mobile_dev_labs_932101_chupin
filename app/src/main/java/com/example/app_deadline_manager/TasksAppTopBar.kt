package com.example.app_deadline_manager

import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksAppTopBar(
    modifier: Modifier = Modifier,
    currentName: String,
    navController: NavController
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        title = { Text(currentName) },
        Modifier.background(Color.Gray),
        actions = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(Icons.Filled.Menu, contentDescription = "Меню")
            }

            DropdownMenu(
                expanded = menuExpanded,
                onDismissRequest = { menuExpanded = false }
            ) {
                val menuItems = listOf(
                    "Создание задачи",
                    "Список задач",
                    "Настройки",
                    "Создание повторяющейся задачи"
                )

                menuItems.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = item,
                                color = if (item == currentName) Color.Gray else Color.Unspecified
                            )
                        },
                        onClick = {
                            navController.navigate(item)
                        },
                        enabled = item != currentName
                    )
                }
            }
        }
    )
}