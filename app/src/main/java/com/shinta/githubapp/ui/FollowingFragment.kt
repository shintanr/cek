package com.shinta.githubapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinta.githubapp.adapter.FollowAdapter
import com.shinta.githubapp.data.response.FollowingFollowersResponseItem
import com.shinta.githubapp.databinding.FragmentFollowingBinding
import com.shinta.githubapp.helper.ViewModelFactory
import com.shinta.githubapp.modelview.DetailUserViewModel

class FollowingFragment : Fragment() {

    private val viewModel: DetailUserViewModel by viewModels()
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var username: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        username = requireActivity().intent.extras?.getString("username").toString()
        setupRecyclerView()

        viewModel.getFollowingUser(username)

        viewModel.followingResponse.observe(viewLifecycleOwner) { following ->
            setFollowingData(following)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setupRecyclerView() {
        val adapter = FollowAdapter()
        binding.rvFollowingUser.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollowingUser.adapter = adapter
    }

    private fun setFollowingData(following: List<FollowingFollowersResponseItem>) {
        val adapter = binding.rvFollowingUser.adapter as? FollowAdapter
        adapter?.submitList(following)
    }

    private fun showLoading(state: Boolean) {
        binding.progressBarFollowing.visibility = if (state) View.VISIBLE else View.GONE
    }
}
