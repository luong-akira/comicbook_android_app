package com.example.readcomicbook.repository

import android.util.Log
import com.example.readcomicbook.data.RetrofitInstance
import com.example.readcomicbook.model.*
import retrofit2.Response

class ComicRepository {
    suspend fun getComics(): Response<Comics> {
        return RetrofitInstance.api.getComics()
    }

    suspend fun searchComic(search: String): Response<Comics> {
        return RetrofitInstance.api.searchComic(search)
    }

    suspend fun getComicsByGenre(genreName:String,pageNumber: Int = 1):Response<Comics>{
        return RetrofitInstance.api.getComicsByGenre(genreName,pageNumber)
    }

    suspend fun getComic(comicName: String): Response<Comic> {
        return RetrofitInstance.api.getComic(comicName)
    }

    suspend fun getChapter(comicSlug: String, chapterSlug: String): Response<Chapter> {
        return RetrofitInstance.api.getChapter(comicSlug, chapterSlug)
    }

    suspend fun getCompletedComic(pageNumber: Int = 1): Response<Comics> {
        return RetrofitInstance.api.getCompletedComics(pageNumber)
    }

    suspend fun login(user: LoginUser): Response<String> {
        return RetrofitInstance.api.login(user)
    }

    suspend fun register(user: RegisterUser): Response<String> {
        return RetrofitInstance.api.register(user)
    }

    suspend fun getLoggedInUser(token: String): Response<LoggedInUser> {
        return RetrofitInstance.api.getLoggedInUser(token)
    }

    suspend fun postCommentOnAComic(
        token: String,
        comicSlug: String,
        commentContent: PostComment
    ): Response<Comic> {
        return RetrofitInstance.api.postCommentOnAComic(token, comicSlug, commentContent)
    }
}