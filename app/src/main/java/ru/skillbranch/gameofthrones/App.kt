package ru.skillbranch.gameofthrones

import android.app.Application

object App : Application() {
    lateinit var instance: App

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}