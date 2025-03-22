package com.example.app_deadline_manager.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.app_deadline_manager.model.UserSettingsModel
import java.time.LocalTime

@RequiresApi(value = Build.VERSION_CODES.O)
class UserSettingsRepository {

    private var userSettings = UserSettingsModel(
        email = "user@example.com",
        sendEmail = true,
        sendPush = true,
        workdayBegin = LocalTime.of(9, 0),
        workdayEnd = LocalTime.of(18, 0)
    )

    fun getSettings(): UserSettingsModel {
        return userSettings
    }

    fun updateSettings(newSettings: UserSettingsModel) {
        userSettings = newSettings
    }
}