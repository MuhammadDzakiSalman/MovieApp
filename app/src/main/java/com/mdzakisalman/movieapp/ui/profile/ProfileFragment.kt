package com.mdzakisalman.movieapp.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.mdzakisalman.movieapp.data.DummyData
import com.mdzakisalman.movieapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = DummyData.currentUser

        binding.tvUsername.text = user.username
        binding.tvEmail.text = user.email

        Glide.with(this)
            .load(user.profilePictureUrl)
            .circleCrop()
            .into(binding.imgProfile)

        binding.btnLogout.setOnClickListener {
            Toast.makeText(requireContext(), "Logout clicked", Toast.LENGTH_SHORT).show()
            // TODO: Tambahkan intent ke login screen atau reset session
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
