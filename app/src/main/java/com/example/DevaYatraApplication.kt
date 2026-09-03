package com.example

import android.app.Application
import com.example.data.AppDatabase
import com.example.data.DevaYatraRepository

class DevaYatraApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { DevaYatraRepository(database.bookingDao(), database.driverDao(), database.escalationDao()) }
}
