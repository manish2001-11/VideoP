package com.example.videop

import android.content.Context
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.videop.databinding.VideoViewBinding

class VideoAdapter(private val context:Context,private val videoList:ArrayList<Video>)
    : RecyclerView.Adapter<VideoAdapter.MyHolder>() {
    class MyHolder(binding: VideoViewBinding): RecyclerView.ViewHolder(binding.root) {
      val title =binding.videoNam
      val folder = binding.videoFol
        val duration =binding.videoDur
        val image=binding.videoImg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoAdapter.MyHolder {
        return MyHolder(VideoViewBinding.inflate(LayoutInflater.from(context),parent, false ))
    }

    override fun onBindViewHolder(holder: VideoAdapter.MyHolder, position: Int) {
        holder.title.text = videoList[position].title
        holder.folder.text =videoList[position].folderName
        holder.duration.text = DateUtils.formatElapsedTime(videoList[position].duration/1000)

        Glide.with(context)
            .asBitmap()
            .load(videoList[position].arUri)
            .apply(RequestOptions().placeholder(R.drawable.video_p).centerCrop())
            .into(holder.image)

    }

    override fun getItemCount(): Int {
     return videoList.size
    }
}
