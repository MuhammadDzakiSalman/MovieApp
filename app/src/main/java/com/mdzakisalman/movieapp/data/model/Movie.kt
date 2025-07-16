package com.mdzakisalman.movieapp.data.model

data class Movie(
    var id: Int,
    var title: String,
    var description: String,
    var genre: String,
    var year: Int,
    var rating: Double,
    var posterUrl: String,
    var trailerUrl: String,
    var isBookmarked: Boolean,
    var actors: List<Int>,
    var episodes: List<String>
)
