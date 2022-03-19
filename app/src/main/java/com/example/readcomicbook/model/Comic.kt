package com.example.readcomicbook.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

data class Comics(
    val docs: List<Comic>,
    val paginator: Paginator
)

@Parcelize
data class Comic(
    val genres: List<String>,
    val isCompleted: Boolean,
    val totalCount: Long,
    val averageRating: Long,

    //@SerializedName("_id")
    val _id: String,

    val name: String,

    val chapters: @RawValue List<Chapter>,
    val comments: @RawValue List<Comment>,
    val slug: String,
    val description: String,
    val img: String,
    val date: String,
    val author: String,
    //@SerializedName("__v")
    val __v: Long,
    val rating: @RawValue List<Rating>
) : Parcelable



data class Chapter(
    val embededLink: List<String>,
    val counter: Long,
    val name: String,
    val number: String,
    val date: String,
    val slug: String,

//    @SerializedName("_id")
//    val id: String? = null
)

data class Comment(
    @SerializedName("_id")
    val id: String,

    val user: String,
    val username: String,
    val comment: String,
    val reply: List<Any?>,
    val likes: List<Any?>,
    val dislikes: List<Any?>,
    val date: String
)

data class Rating(
    @SerializedName("_id")
    val id: String,

    val user: String,
    val value: Long
)

data class Paginator(
    val totalDocs: Long,
    val limit: Long,
    val totalPages: Long,
    val currentPage: Long,
    val pagingCounter: Long,
    val hasPrevPage: Boolean,
    val hasNextPage: Boolean,
    val prevPage: Any? = null,
    val nextPage: Any? = null
)
