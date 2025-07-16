package com.mdzakisalman.movieapp.ui.movie

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mdzakisalman.movieapp.data.DummyData
import com.mdzakisalman.movieapp.databinding.FragmentMovieBinding
import com.mdzakisalman.movieapp.ui.home.MovieAdapter
import com.mdzakisalman.movieapp.R
import androidx.core.content.ContextCompat

class MovieFragment : Fragment() {

    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieAdapter: MovieAdapter
    private var selectedGenre: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter = MovieAdapter(DummyData.movies)
        binding.rvMovieList.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = movieAdapter
        }

        setupGenreButtons()
        setupSearch()
    }

    private fun setupGenreButtons() {
        val genres = DummyData.movies.map { it.genre }.distinct()
        val container = binding.genreContainer
        container.removeAllViews()

        for (genre in genres) {
            val button = Button(requireContext()).apply {
                text = genre
                setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
                setBackgroundResource(R.drawable.bg_genre_button)
                setPadding(32, 8, 32, 8)

                setOnClickListener {
                    selectedGenre = if (selectedGenre == genre) null else genre
                    filterMovies()
                }
            }
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(8, 0, 8, 0)
            }
            container.addView(button, params)
        }
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) = filterMovies()
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterMovies() {
        val keyword = binding.etSearch.text.toString().lowercase()
        val filtered = DummyData.movies.filter {
            (selectedGenre == null || it.genre == selectedGenre) &&
                    (keyword.isEmpty() || it.title.lowercase().contains(keyword))
        }
        movieAdapter.setData(filtered)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
