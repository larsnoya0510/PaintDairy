package com.example.paintdairy

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class CustomAplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}