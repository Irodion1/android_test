package ru.skillbranch.gameofthrones.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.skillbranch.gameofthrones.R


class SplashFragment : Fragment(), SplashContract.View {

    lateinit var mPresenter: SplashContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mPresenter = SplashPresenter(this)
        mPresenter.onStart()
    }

    override fun getFragment(): Fragment {
        return this
    }
}