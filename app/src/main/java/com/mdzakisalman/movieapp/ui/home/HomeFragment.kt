package com.mdzakisalman.movieapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.mdzakisalman.movieapp.R
import com.mdzakisalman.movieapp.data.DummyData
import com.mdzakisalman.movieapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var sliderAdapter: SliderAdapter
    private lateinit var movieAdapter: MovieAdapter

    private lateinit var sliderRunnable: Runnable
    private var currentPage = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Setup Slider
        val sliderMovies = DummyData.movies.take(5)
        sliderAdapter = SliderAdapter(sliderMovies)
        binding.sliderViewPager.adapter = sliderAdapter

        // 1. Tambahkan padding di ViewPager agar terlihat ada jarak antar item
        binding.sliderViewPager.apply {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
            setPadding(4, 0, 4, 0) // padding kanan kiri, atur sesuai kebutuhan
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        // 2. Gunakan MarginPageTransformer + animasi aman
        val compositeTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(24)) // Jarak antar item
            addTransformer { page, position ->
                val r = 1 - kotlin.math.abs(position)
                page.scaleY = 0.95f + r * 0.05f
                page.alpha = 0.7f + r * 0.3f
            }
        }

        binding.sliderViewPager.setPageTransformer(compositeTransformer)


        // Indikator
        setupIndicator(sliderMovies.size)

        binding.sliderViewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                setCurrentIndicator(position)
            }
        })

        // Auto-scroll every 3 seconds
        sliderRunnable = Runnable {
            if (_binding != null && binding.sliderViewPager.adapter != null) {
                val next = (currentPage + 1) % sliderAdapter.itemCount
                binding.sliderViewPager.setCurrentItem(next, true)
                binding.sliderViewPager.postDelayed(sliderRunnable, 3000)
            }
        }
        binding.sliderViewPager.postDelayed(sliderRunnable, 3000)

        // 2. Setup Movie Grid
        movieAdapter = MovieAdapter(DummyData.movies)
        binding.rvMovies.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = movieAdapter
            isNestedScrollingEnabled = false // agar bisa scroll bareng dengan NestedScrollView
        }
    }


    private fun setupIndicator(count: Int) {
        val indicators = arrayOfNulls<ImageView>(count)
        val layout = binding.sliderIndicator
        layout.removeAllViews()

        for (i in 0 until count) {
            indicators[i] = ImageView(requireContext())
            indicators[i]?.apply {
                setImageResource(if (i == 0) R.drawable.indicator_active else R.drawable.indicator_inactive)
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(8, 0, 8, 0)
                }
                layout.addView(this, params)
            }
        }
    }

    private fun setCurrentIndicator(index: Int) {
        val childCount = binding.sliderIndicator.childCount
        for (i in 0 until childCount) {
            val imageView = binding.sliderIndicator.getChildAt(i) as ImageView
            imageView.setImageResource(
                if (i == index) R.drawable.indicator_active
                else R.drawable.indicator_inactive
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.sliderViewPager.removeCallbacks(sliderRunnable)
        _binding = null
    }
}
