package com.mdzakisalman.movieapp.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.mdzakisalman.movieapp.data.DummyData
import com.mdzakisalman.movieapp.databinding.FragmentBookmarkBinding
import com.mdzakisalman.movieapp.ui.home.MovieAdapter

class BookmarkFragment : Fragment() {

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    private lateinit var bookmarkAdapter: MovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bookmarkedMovies = DummyData.movies.filter { it.isBookmarked }

        bookmarkAdapter = MovieAdapter(bookmarkedMovies)

        binding.rvBookmarks.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = bookmarkAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh data saat kembali ke fragment
        bookmarkAdapter = MovieAdapter(DummyData.movies.filter { it.isBookmarked })
        binding.rvBookmarks.adapter = bookmarkAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
