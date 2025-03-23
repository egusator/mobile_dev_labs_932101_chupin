package com.example.app_deadline_manager.compose.settings
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_deadline_manager.repository.SettingsRepository
import kotlinx.coroutines.launch
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {

    var email = mutableStateOf(repository.getSetting("email", ""))
        private set

    var isPushEnabled = mutableStateOf(repository.getBooleanSetting("push_enabled", false))
        private set

    var isEmailNotificationsEnabled = mutableStateOf(repository.getBooleanSetting("email_notifications_enabled", false))
        private set

    var workStartTime = mutableStateOf(repository.getTimeSetting("work_start_time", LocalTime.of(9, 0)))
        private set

    var workEndTime = mutableStateOf(repository.getTimeSetting("work_end_time", LocalTime.of(18, 0)))
        private set

    fun updateEmail(newEmail: String) {
        email.value = newEmail
        saveToDatabase("email", newEmail)
    }

    fun togglePushNotifications() {
        isPushEnabled.value = !isPushEnabled.value
        saveBooleanToDatabase("push_enabled", isPushEnabled.value)
    }

    fun toggleEmailNotifications() {
        isEmailNotificationsEnabled.value = !isEmailNotificationsEnabled.value
        saveBooleanToDatabase("email_notifications_enabled", isEmailNotificationsEnabled.value)
    }

    fun updateWorkStartTime(newTime: LocalTime) {
        workStartTime.value = newTime
        saveTimeToDatabase("work_start_time", newTime)
    }

    fun updateWorkEndTime(newTime: LocalTime) {
        workEndTime.value = newTime
        saveTimeToDatabase("work_end_time", newTime)
    }

    private fun saveToDatabase(key: String, value: String) {
        viewModelScope.launch {
            repository.saveSetting(key, value)
        }
    }

    private fun saveBooleanToDatabase(key: String, value: Boolean) {
        viewModelScope.launch {
            repository.saveBooleanSetting(key, value)
        }
    }

    private fun saveTimeToDatabase(key: String, time: LocalTime) {
        viewModelScope.launch {
            repository.saveTimeSetting(key, time)
        }
    }
}
