package com.example.readcomicbook.data

import com.example.readcomicbook.model.*
import retrofit2.Response
import retrofit2.http.*


interface ComicApi {
    @GET("api/comic")
    suspend fun getComics(): Response<Comics>

    @GET("api/comic/search")
    suspend fun searchComic(@Query("search") search: String): Response<Comics>

    @GET("api/comic/genre/{genreName}")
    suspend fun getComicsByGenre(
        @Path("genreName") genreName: String,
        @Query("page") page: Int = 1
    ): Response<Comics>

    @GET("api/comic/completedComic")
    suspend fun getCompletedComics(@Query("page") page: Int = 1): Response<Comics>


    @GET("api/comic/{comicName}")
    suspend fun getComic(@Path("comicName") comicName: String): Response<Comic>

    @GET("api/comic/{comicSlug}/{chapterSlug}")
    suspend fun getChapter(
        @Path("comicSlug") comicSlug: String,
        @Path("chapterSlug") chapterSlug: String
    ): Response<Chapter>

    @POST("api/auth/login")
    @Headers("Content-Type: application/json")
    suspend fun login(@Body user: LoginUser): Response<String>

    @POST("api/user/register")
    @Headers("Content-Type: application/json")
    suspend fun register(@Body user: RegisterUser): Response<String>


    @GET("api/auth/login")
    suspend fun getLoggedInUser(@Header("x-auth-token") token: String): Response<LoggedInUser>


    @POST("api/comic/{comicSlug}")
    suspend fun postCommentOnAComic(
        @Header("x-auth-token") token: String,
        @Path("comicSlug") comicSlug: String,
        @Body commentContent: PostComment
    ): Response<Comic>


}