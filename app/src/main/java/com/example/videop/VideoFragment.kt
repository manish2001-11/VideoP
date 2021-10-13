package com.example.videop

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.videop.databinding.FragmentVideoBinding


class VideoFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view =inflater.inflate(R.layout.fragment_video,container,false)
        val binding =FragmentVideoBinding.bind(view)


        binding.videoRv.setHasFixedSize(true)
        binding.videoRv.setItemViewCacheSize(11)
        binding.videoRv.layoutManager = LinearLayoutManager(requireContext())
        binding.videoRv.adapter = VideoAdapter(requireContext(),MainActivity.videoList)

        return view
    }


}