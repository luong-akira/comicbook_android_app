package com.example.readcomicbook.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.readcomicbook.R
import com.example.readcomicbook.model.Comic
import com.example.readcomicbook.model.Comment
import kotlinx.android.synthetic.main.comment_row.view.*

class CommentSectionAdapter(private val commentList: List<Comment>) :
    RecyclerView.Adapter<CommentSectionAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.comment_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.username_txt.text = commentList[position].username
        holder.itemView.comment_txt.text = commentList[position].comment
        holder.itemView.profile_picture.load("https://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Blogger.svg/1200px-Blogger.svg.png"){
            crossfade(true)
            placeholder(R.drawable.ic_baseline_cloud_download_24)
        }
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

}