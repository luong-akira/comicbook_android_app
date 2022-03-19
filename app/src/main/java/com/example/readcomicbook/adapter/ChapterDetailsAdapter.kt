package com.example.readcomicbook.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.readcomicbook.R
import com.example.readcomicbook.constants.AppConstants.Companion.BASE_URL
import kotlinx.android.synthetic.main.images_in_chapter_row.view.*

class ChapterDetailsAdapter : RecyclerView.Adapter<ChapterDetailsAdapter.MyViewHolder>() {
    private var imagesList = emptyList<String>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.images_in_chapter_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (!imagesList[position].startsWith("/")) {
            holder.itemView.image_in_chapter.load(imagesList[position]) {
                crossfade(true)
                placeholder(R.drawable.ic_baseline_cloud_download_24)
            }
        } else {
            holder.itemView.image_in_chapter.load(BASE_URL + imagesList[position]) {
                crossfade(true)
                placeholder(R.drawable.ic_baseline_cloud_download_24)
            }
        }
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

    fun setData(newImageList: List<String>) {
        imagesList = newImageList
        notifyDataSetChanged()
    }

}