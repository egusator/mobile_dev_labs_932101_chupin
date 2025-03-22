package com.example.app_deadline_manager.model

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
data class UserSettingsModel(
    var email: String,
    var sendEmail: Boolean,
    var sendPush: Boolean,
    var workdayBegin: LocalTime,
    var workdayEnd: LocalTime
)