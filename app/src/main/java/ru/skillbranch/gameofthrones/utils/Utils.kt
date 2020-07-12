package ru.skillbranch.gameofthrones.utils

import android.content.Context
import android.net.ConnectivityManager

object Utils {
    fun networkAvailable(context: Context): Boolean {
        val conManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return conManager.activeNetworkInfo.isConnected
    }
}