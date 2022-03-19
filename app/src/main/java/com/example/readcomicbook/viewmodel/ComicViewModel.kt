package com.example.readcomicbook.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.readcomicbook.data.RetrofitInstance
import com.example.readcomicbook.model.*
import com.example.readcomicbook.repository.ComicRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class ComicViewModel(private val repository: ComicRepository) : ViewModel() {

    val _comics: MutableLiveData<Response<Comics>> = MutableLiveData()
    val _comic: MutableLiveData<Response<Comic>> = MutableLiveData()
    val _chapter: MutableLiveData<Response<Chapter>> = MutableLiveData()
    val _loginResponse: MutableLiveData<Response<String>> = MutableLiveData()
    val _registerResponse: MutableLiveData<Response<String>> = MutableLiveData()
    val _loggedInUser: MutableLiveData<Response<LoggedInUser>> = MutableLiveData()
    val _token: MutableLiveData<String> = MutableLiveData<String>("")
    var currentPage: Int = 1
    var currentChapter: Int = 0


    fun getComics() {
        viewModelScope.launch {
            val comics = repository.getComics()
            _comics.value = comics
        }
    }

    fun getComicsByGenre(genreName: String,pageNumber: Int = 1) {
        viewModelScope.launch {
            val comics = repository.getComicsByGenre(genreName,pageNumber)
            _comics.value = comics
        }
    }

    fun searchComic(search: String) {
        viewModelScope.launch {
            val comics = repository.searchComic(search)
            _comics.value = comics
        }
    }

    fun getCompletedComics(pageNumber: Int = 1) {
        viewModelScope.launch {
            val comics = repository.getCompletedComic(pageNumber)
            _comics.value = comics
        }

    }

    fun getComic(comicName: String) {
        viewModelScope.launch {
            val comic = repository.getComic(comicName)
            _comic.value = comic
        }
    }

    fun getChapter(comicSlug: String, chapterSlug: String) {
        currentChapter = chapterSlug.split("_")[1].toInt()
        Log.d("Response", "Current Chapter $currentChapter")
        viewModelScope.launch {
            val chapter = repository.getChapter(comicSlug, chapterSlug)
            _chapter.value = chapter
        }
    }

    fun login(user: LoginUser) {
        viewModelScope.launch {
            val response = repository.login(user)
            _loginResponse.value = response

        }
    }

    fun register(user: RegisterUser) {
        viewModelScope.launch {
            val response = repository.register(user)
            _registerResponse.value = response
        }
    }

    fun getLoggedInUser(token: String) {
        viewModelScope.launch {
            val loggedInUser = repository.getLoggedInUser(token)
            _loggedInUser.value = loggedInUser
        }
    }

    fun postCommentOnAComic(
        token: String,
        comicSlug: String,
        commentContent: PostComment
    ) {
        viewModelScope.launch {
            repository.postCommentOnAComic(token, comicSlug, commentContent)
        }
    }
}