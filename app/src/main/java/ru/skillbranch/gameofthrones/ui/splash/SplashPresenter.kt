package ru.skillbranch.gameofthrones.ui.splash

import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.internal.operators.observable.ObservableJust
import io.reactivex.schedulers.Schedulers
import ru.skillbranch.gameofthrones.repositories.RootRepository
import java.util.concurrent.TimeUnit

class SplashPresenter(var mView: SplashContract.View) : SplashContract.Presenter {
    val SPLASH_DELAY = 5L
    var navController: NavController = mView.getFragment().findNavController()
    private val disposable = CompositeDisposable()


    override fun onStart() {
        if (RootRepository.isNeedAaa()) {
            disposable.add(
                ObservableJust<ApiResult>(ApiResult.FINISHED)
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(Schedulers.io())
                    .delay(SPLASH_DELAY, TimeUnit.SECONDS)
                    .subscribe({ processResults(it) }, { processResults(ApiResult.ERROR) })
            )
        } else {
            disposable.add(ObservableJust<ApiResult>(ApiResult.FINISHED)
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .delay(SPLASH_DELAY, TimeUnit.SECONDS)
                .subscribe { processResults(it) })
        }
    }

    fun processResults(result: ApiResult) {
        when (result) {
            ApiResult.FINISHED -> {
                val action = SplashFragmentDirections.actionSplashFragmentToHousesFragment()
                navController.navigate(action)
            }
            ApiResult.ERROR -> {
                Toast.makeText(mView.getActivity(), "error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    enum class ApiResult {
        FINISHED, ERROR
    }

}