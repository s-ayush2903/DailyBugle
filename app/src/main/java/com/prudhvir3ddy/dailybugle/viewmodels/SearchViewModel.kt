package com.prudhvir3ddy.dailybugle.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.prudhvir3ddy.dailybugle.BuildConfig
import com.prudhvir3ddy.dailybugle.network.NewsApi
import com.prudhvir3ddy.dailybugle.network.data.News
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchViewModel() : ViewModel() {

    private val _foundNews = MutableLiveData<News>()

    val foundNews: LiveData<News>
        get() = _foundNews

    private val _status = MutableLiveData<Boolean>()

    val status: LiveData<Boolean>
        get() = _status

    fun searchNews(query: String) {
        viewModelScope.launch {

            val getNewsDeferred =
                NewsApi().newsService.getEveryThing(query, BuildConfig.apiNews)
            try {
                val resultList = getNewsDeferred.await()
                _foundNews.value = resultList
            } catch (e: Exception) {
                Log.i("searchResult", e.message)
                if (e.message.equals("HTTP 504 Unsatisfiable Request (only-if-cached)"))
                    _status.value = true
            }
        }
    }

    fun resetStatus() {
        _status.value = false
    }

}