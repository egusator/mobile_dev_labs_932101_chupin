package com.example.app_deadline_manager.repository
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalTime

@RequiresApi(Build.VERSION_CODES.O)
class SettingsRepository(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "settings.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "settings"
        private const val COLUMN_KEY = "setting_key"
        private const val COLUMN_VALUE = "setting_value"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE $TABLE_NAME (" +
                    "$COLUMN_KEY TEXT PRIMARY KEY, " +
                    "$COLUMN_VALUE TEXT NOT NULL)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun saveSetting(key: String, value: String) {
        writableDatabase.execSQL(
            "INSERT OR REPLACE INTO $TABLE_NAME ($COLUMN_KEY, $COLUMN_VALUE) VALUES (?, ?)",
            arrayOf(key, value)
        )
    }

    fun getSetting(key: String, defaultValue: String): String {
        val cursor = readableDatabase.rawQuery(
            "SELECT $COLUMN_VALUE FROM $TABLE_NAME WHERE $COLUMN_KEY = ?",
            arrayOf(key)
        )
        val value = if (cursor.moveToFirst()) cursor.getString(0) else defaultValue
        cursor.close()
        return value
    }

    fun getBooleanSetting(key: String, defaultValue: Boolean): Boolean {
        return getSetting(key, defaultValue.toString()).toBoolean()
    }

    fun saveBooleanSetting(key: String, value: Boolean) {
        saveSetting(key, value.toString())
    }

    fun getTimeSetting(key: String, defaultTime: LocalTime): LocalTime {
        return try {
            LocalTime.parse(getSetting(key, defaultTime.toString()))
        } catch (e: Exception) {
            defaultTime
        }
    }

    fun saveTimeSetting(key: String, time: LocalTime) {
        saveSetting(key, time.toString())
    }
}