package com.mdzakisalman.movieapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mdzakisalman.movieapp.R
import com.mdzakisalman.movieapp.data.model.Movie

class SliderAdapter(private val sliderList: List<Movie>) :
    RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSliderPoster: ImageView = itemView.findViewById(R.id.imgSliderPoster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val movie = sliderList[position]
        Glide.with(holder.itemView.context)
            .load(movie.posterUrl)
            .into(holder.imgSliderPoster)
    }

    override fun getItemCount(): Int = sliderList.size
}
