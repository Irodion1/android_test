package ru.skillbranch.gameofthrones.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.skillbranch.gameofthrones.repositories.RootRepository
import ru.skillbranch.gameofthrones.utils.Utils.networkAvailable

class RootViewModel(val app: Application) : AndroidViewModel(app) {
    private val repository = RootRepository

    fun syncDataIfNedeed(): LiveData<LoadResult<Boolean>> {
        val result: MutableLiveData<LoadResult<Boolean>> =
            MutableLiveData(LoadResult.Loading(false))
        viewModelScope.launch(Dispatchers.IO) {
            if (repository.isNeedUpdate()) {
                if (!networkAvailable(app)) {
                    result.postValue(LoadResult.Error("Интернет недоступен - приложение не может быть запущенно. Подключитесь к интернету и перезапустите приложение"))
                    return@launch
                }
                repository.sync()
                result.postValue(LoadResult.Sucess(true))
            } else {
                delay(5000)
                result.postValue(LoadResult.Sucess(true))
            }
        }

        return result
    }
}

sealed class LoadResult<T>(
    val data: T?,
    val errorMessage: String? = null
) {
    class Sucess<T>(data: T) : LoadResult<T>(data)
    class Loading<T>(data: T? = null) : LoadResult<T>(data)
    class Error<T>(message: String, data: T? = null) : LoadResult<T>(data, message)
}