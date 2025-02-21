package com.atilmohamine.fitnesstracker.ui


import android.app.Application
import com.atilmohamine.fitnesstracker.data.HealthConnectManager

class BaseApplication : Application() {
    val healthConnectManager by lazy {
        HealthConnectManager(this)
    }
}