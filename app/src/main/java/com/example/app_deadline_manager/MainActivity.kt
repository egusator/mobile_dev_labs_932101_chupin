package com.example.app_deadline_manager

import android.os.Build
import android.os.Bundle
import android.view.Window
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app_deadline_manager.compose.RegistrationScreen
import androidx.navigation.compose.rememberNavController
import com.example.app_deadline_manager.compose.SettingsScreen
import com.example.app_deadline_manager.compose.TasksListScreen
import com.example.app_deadline_manager.compose.create_periodic.CreatePeriodicTaskScreen
import com.example.app_deadline_manager.compose.create_periodic.CreatePeriodicTaskViewModel
import com.example.app_deadline_manager.compose.create_task.CreateTaskScreen
import com.example.app_deadline_manager.compose.create_task.TaskViewModel
import java.time.LocalDate

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContent {

            DeadlineManagerAppTheme {

                val navController = rememberNavController()

                var createTaskViewModel = TaskViewModel ()

                var createPeriodicTaskViewModel = CreatePeriodicTaskViewModel()

                Scaffold(modifier = Modifier.fillMaxSize()) {
                    paddingValues ->
                    NavHost(
                        startDestination = "Регистрация",
                        navController = navController,
                        modifier = Modifier.padding(paddingValues)
                    ) {
                        composable(
                            ("Регистрация")
                        ) {
                            RegistrationScreen(
                                Modifier.padding(paddingValues),
                                navController = navController
                                )
                        }
                        composable(
                            ("Список задач")
                        ) {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                topBar = {
                                    TasksAppTopBar(Modifier.fillMaxWidth(), "Список задач", navController)
                                }
                            ) { paddingValues ->
                                TasksListScreen(
                                    Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize(),
                                    initialContent = getTestTasks(),
                                    navController
                                )
                            }
                        }

                        composable ("Создание задачи") {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                topBar = {
                                    TasksAppTopBar(Modifier.fillMaxWidth(), "Создание задачи", navController)
                                }
                            ) { paddingValues ->
                                CreateTaskScreen(
                                    Modifier.padding(paddingValues).fillMaxSize(),
                                    createTaskViewModel = createTaskViewModel
                                )
                            }
                        }

                        composable ("Создание повторяющейся задачи") {
                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                topBar = {
                                    TasksAppTopBar(Modifier.fillMaxWidth(), "Создание повторяющейся задачи", navController)
                                }
                            ) { paddingValues ->
                                CreatePeriodicTaskScreen(
                                    Modifier.padding(paddingValues).fillMaxSize(),
                                    createPeriodicTaskViewModel = createPeriodicTaskViewModel
                                )
                            }
                        }


                        composable ("Настройки") {

                            Scaffold(
                                modifier = Modifier.fillMaxSize(),
                                topBar = {
                                    TasksAppTopBar(Modifier.fillMaxWidth(), "Настройки", navController)
                                }
                            ) { paddingValues ->
                                SettingsScreen(
                                    Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize(),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun getTestTasks(): List<TaskViewModel> {
    return listOf(
        TaskViewModel(
            taskName = "name1",
            description = "description1",
            deadline = LocalDate.now().plusDays(2),
            priority = "Высокий",
            hours = 0,
            minutes = 30
        ),
        TaskViewModel(
            taskName = "name2",
            description = "description2",
            deadline = LocalDate.now().plusDays(1),
            priority = "Средний",
            hours = 2,
            minutes = 0
        ),
        TaskViewModel(
            taskName = "name3",
            description = "description3",
            deadline = LocalDate.now(),
            priority = "Низкий",
            hours = 0,
            minutes = 10
        ),
        TaskViewModel(
            taskName = "name4",
            description = "description4",
            deadline = LocalDate.now().plusDays(3),
            priority = "Средний",
            hours = 0,
            minutes = 45
        ),
        TaskViewModel(
            taskName = "name5",
            description = "description5",
            deadline = LocalDate.now().plusDays(5),
            priority = "Высокий",
            hours = 3,
            minutes = 0
        )
    )
}
