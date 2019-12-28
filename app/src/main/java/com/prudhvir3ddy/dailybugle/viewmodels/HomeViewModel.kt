package com.prudhvir3ddy.dailybugle.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prudhvir3ddy.dailybugle.BuildConfig
import com.prudhvir3ddy.dailybugle.network.NewsApi
import com.prudhvir3ddy.dailybugle.network.data.News
import com.prudhvir3ddy.dailybugle.network.data.Sources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class HomeViewModel(application: Application) : AndroidViewModel(application){

    private val _sources = MutableLiveData<Sources>()

    val sources: LiveData<Sources>
        get() = _sources

    private val _topNews = MutableLiveData<News>()

    val topNews: LiveData<News>
        get() = _topNews


    private val viewModelJob = Job()

    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    fun getSources(){
        coroutineScope.launch {

            val getSourcesDeferred = NewsApi(getApplication()).newsService.getSources(BuildConfig.apiNews)
            try {
                val resultList = getSourcesDeferred.await()
                _sources.value = resultList
            } catch (e: Exception) {
                Log.d("newsResult", e.message.toString())
            }
        }
    }

    fun getTopHeadLines() {
        coroutineScope.launch {

            val getTopHeadLinesDeferred =
                NewsApi(getApplication()).newsService.getTopHeadlines("IN", BuildConfig.apiNews)
            try {
                val resultList = getTopHeadLinesDeferred.await()
                _topNews.value = resultList

            } catch (e: Exception) {
                Log.d("topNewsResult", e.message.toString())
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}