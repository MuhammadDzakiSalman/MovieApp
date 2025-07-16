package com.mdzakisalman.movieapp.ui.detail

import android.os.Bundle
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.mdzakisalman.movieapp.R
import com.mdzakisalman.movieapp.data.DummyData
import com.mdzakisalman.movieapp.data.model.Movie
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_MOVIE_ID = "movie_id"
    }

    private lateinit var youtubePlayerView: YouTubePlayerView
    private lateinit var imgPoster: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvRating: TextView
    private lateinit var tvGenre: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnBookmark: Button

    private var isBookmarked: Boolean = false
    private lateinit var episodeGrid: GridLayout
    private lateinit var actorContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        // View Binding manual
        youtubePlayerView = findViewById(R.id.youtubePlayerView)
        imgPoster = findViewById(R.id.imgPoster)
        tvTitle = findViewById(R.id.tvTitle)
        tvYear = findViewById(R.id.tvYear)
        tvRating = findViewById(R.id.tvRating)
        tvGenre = findViewById(R.id.tvGenre)
        tvDescription = findViewById(R.id.tvDescription)
        btnBookmark = findViewById(R.id.btnBookmark)
        episodeGrid = findViewById(R.id.episodeGrid)
        actorContainer = findViewById(R.id.actorContainer)

        // Get movie ID
        val movieId = intent.getIntExtra(EXTRA_MOVIE_ID, -1)
        val movie = DummyData.movies.find { it.id == movieId }

        if (movie != null) {
            lifecycle.addObserver(youtubePlayerView)
            showMovieDetails(movie)
            showEpisodes(movie.episodes)
            showActors(movie.actors)
        } else {
            Toast.makeText(this, "Movie not found", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun showMovieDetails(movie: Movie) {
        tvTitle.text = movie.title
        tvYear.text = "Year: ${movie.year}"
        tvRating.text = "⭐ ${movie.rating}"
        tvGenre.text = "Genre: ${movie.genre}"
        tvDescription.text = movie.description
        isBookmarked = movie.isBookmarked

        Glide.with(this)
            .load(movie.posterUrl)
            .into(imgPoster)

        setupBookmarkButton()

        val videoId = extractYoutubeVideoId(movie.trailerUrl)
        youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.cueVideo(videoId, 0f)
            }
        })
    }

    private fun showEpisodes(episodes: List<String>) {
        episodeGrid.removeAllViews()
        val context = this

        for (episode in episodes) {
            val btn = Button(context).apply {
                text = episode
                textSize = 12f
                setPadding(8, 4, 8, 4)
                setTextColor(getColor(android.R.color.white))
                background = ContextCompat.getDrawable(context, R.drawable.bg_episode_button)
            }
            val params = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            episodeGrid.addView(btn, params)
        }
    }

    private fun showActors(actorIds: List<Int>) {
        actorContainer.removeAllViews()

        val context = this
        val actors = DummyData.actors.filter { actorIds.contains(it.id) }

        for (actor in actors) {
            val card = LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0)
                }
                gravity = android.view.Gravity.CENTER
            }

            val img = ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(150, 150)
                scaleType = ImageView.ScaleType.CENTER_CROP
                background = context.getDrawable(R.drawable.actor_image_bg)
                clipToOutline = true
            }

            Glide.with(context)
                .load(actor.photoUrl)
                .into(img)

            val name = TextView(context).apply {
                text = actor.name
                textSize = 12f
                gravity = android.view.Gravity.CENTER
                maxLines = 1
                ellipsize = android.text.TextUtils.TruncateAt.END
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = 8
                }
            }

            card.addView(img)
            card.addView(name)

            actorContainer.addView(card)
        }
    }

    private fun setupBookmarkButton() {
        updateBookmarkText()

        btnBookmark.setOnClickListener {
            isBookmarked = !isBookmarked
            updateBookmarkText()
            Toast.makeText(this, if (isBookmarked) "Bookmarked!" else "Removed from bookmark", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateBookmarkText() {
        btnBookmark.text = if (isBookmarked) "Remove Bookmark" else "Bookmark"
    }

    private fun extractYoutubeVideoId(url: String): String {
        return try {
            val uri = android.net.Uri.parse(url)
            uri.getQueryParameter("v") ?: url.substringAfterLast("/")
        } catch (e: Exception) {
            ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        youtubePlayerView.release()
    }
}
