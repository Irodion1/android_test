package ru.skillbranch.gameofthrones.ui.splash

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment

interface SplashContract {
    interface View {
        fun getActivity(): Activity?
        fun getContext(): Context?
        fun getFragment(): Fragment
    }

    interface Presenter {
        fun onStart()
    }
}