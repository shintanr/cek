package com.shinta.githubapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinta.githubapp.adapter.FollowAdapter
import com.shinta.githubapp.adapter.UserAdapter
import com.shinta.githubapp.data.response.FollowingFollowersResponseItem
import com.shinta.githubapp.databinding.FragmentFollowersBinding
import com.shinta.githubapp.databinding.FragmentFollowingBinding
import com.shinta.githubapp.helper.ViewModelFactory
import com.shinta.githubapp.modelview.DetailUserViewModel

class FollowingFragment : Fragment() {

    private lateinit var binding: FragmentFollowingBinding
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentFollowingBinding.bind(view)
        username = requireActivity().intent.extras?.getString("username").toString()

        binding.rvFollowingUser.layoutManager = LinearLayoutManager(requireActivity())

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireContext())
        val viewModel: DetailUserViewModel by viewModels {
            factory
        }

        val recycleView = binding.rvFollowingUser
        val adapter = UserAdapter()
        recycleView.layoutManager = LinearLayoutManager(requireContext())
        recycleView.adapter = adapter

        viewModel.getFollowingUser(username)

        viewModel.followingResponse.observe(viewLifecycleOwner) { following ->
            setFollowingData(following)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setFollowingData(following: List<FollowingFollowersResponseItem>) {
        val adapter = FollowAdapter()
        adapter.submitList(following)
        binding.rvFollowingUser.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        binding.progressBarFollowing.visibility = if (state) View.VISIBLE else View.GONE
    }
}
