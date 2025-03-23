package com.example.app_deadline_manager

import ScheduleUseCase
import ScheduleViewModel
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.app_deadline_manager.compose.RegistrationScreen
import androidx.navigation.compose.rememberNavController
import com.example.app_deadline_manager.compose.settings.SettingsScreen
import com.example.app_deadline_manager.compose.schedule.TasksScheduleScreen
import com.example.app_deadline_manager.compose.create_periodic.CreatePeriodicTaskScreen
import com.example.app_deadline_manager.compose.create_periodic.CreatePeriodicTaskViewModel
import com.example.app_deadline_manager.compose.settings.SettingsViewModel
import com.example.app_deadline_manager.mapper.TaskForScheduleMapper
import com.example.app_deadline_manager.repository.PeriodicTaskRepository
import com.example.app_deadline_manager.repository.SettingsRepository
import com.example.app_deadline_manager.repository.TaskForScheduleRepository
import com.example.app_deadline_manager.repository.TaskRepository

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContent {

            DeadlineManagerAppTheme {

                val navController = rememberNavController()

                var createTaskViewModel = null

                var createPeriodicTaskViewModel = CreatePeriodicTaskViewModel()

                var taskRepository = TaskRepository()

                var periodicTaskRepository = PeriodicTaskRepository()

                var taskForScheduleRepository = TaskForScheduleRepository()
                val context = LocalContext.current
                val settingsRepository = remember { SettingsRepository(context) }
                val settingsViewModel = remember { SettingsViewModel(settingsRepository) }

                var scheduleUseCase = ScheduleUseCase(taskForScheduleRepository, periodicTaskRepository, taskRepository)
                val taskMapper = remember { TaskForScheduleMapper(taskRepository, periodicTaskRepository) }

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
                                TasksScheduleScreen(
                                    Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize(),
                                    navController,
                                    ScheduleViewModel(scheduleUseCase),
                                    taskMapper
                                )
                            }
                        }

                        composable ("Создание задачи") {

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
                                    settingsViewModel,
                                    Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

