package com.mdzakisalman.movieapp.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdzakisalman.movieapp.R
import com.mdzakisalman.movieapp.data.model.Movie
import com.mdzakisalman.movieapp.ui.detail.DetailActivity

class MovieAdapter(private var movieList: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgPoster: ImageView = itemView.findViewById(R.id.imgPoster)
        val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvYear: TextView = itemView.findViewById(R.id.tvYear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movieList[position]
        holder.tvTitle.text = movie.title
        holder.tvYear.text = movie.year.toString()
        holder.tvRating.text = "⭐ ${movie.rating}"

        Glide.with(holder.itemView.context)
            .load(movie.posterUrl)
            .into(holder.imgPoster)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("movie_id", movie.id)
            context.startActivity(intent)
        }
    }

    fun setData(newList: List<Movie>) {
        movieList = newList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = movieList.size
}
