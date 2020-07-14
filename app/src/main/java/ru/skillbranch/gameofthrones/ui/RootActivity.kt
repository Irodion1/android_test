package ru.skillbranch.gameofthrones.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_root.*
import ru.skillbranch.gameofthrones.R
import ru.skillbranch.gameofthrones.ui.splash.SplashFragmentDirections

class RootActivity : AppCompatActivity() {

    private lateinit var vm: RootViewModel
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        Log.d("M_RootActivity", "RootActivity created")
        initViewModel()
        savedInstanceState ?: prepareData()
        navController = findNavController(
            R.id.my_nav_host_fragment
        )
    }

    private fun initViewModel() {
        vm = RootViewModel(application)
    }

    private fun prepareData() {
        vm.syncDataIfNedeed().observe(this, Observer<LoadResult<Boolean>> {
            when (it) {
                is LoadResult.Loading -> {
                    navController.navigate(R.id.splashFragment)
                }
                is LoadResult.Sucess -> {
                    val action = SplashFragmentDirections.actionSplashFragmentToHousesFragment()
                    navController.navigate(action)
                }
                is LoadResult.Error -> {
                    Snackbar.make(
                        rootActivity,
                        it.errorMessage.toString(),
                        Snackbar.LENGTH_INDEFINITE
                    ).show()
                }
            }
        })
    }

}