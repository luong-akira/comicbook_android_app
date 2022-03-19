package com.example.readcomicbook.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.readcomicbook.R
import com.example.readcomicbook.fragments.ComicDetailsFragmentDirections
import com.example.readcomicbook.model.Chapter
import kotlinx.android.synthetic.main.chapter_row.view.*

class ComicDetailsAdapter(val slug: String) :
    RecyclerView.Adapter<ComicDetailsAdapter.MyViewHolder>() {
    private var chapterList = emptyList<Chapter>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.chapter_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.chapter_name_txt.text = chapterList[position].name
        holder.itemView.chapter_view_txt.text = chapterList[position].counter.toString()
        holder.itemView.uploaded_time_txt.text = chapterList[position].date
        holder.itemView.setOnClickListener {
            val action =
                ComicDetailsFragmentDirections.actionComicDetailsFragmentToChapterDetailsFragment(
                    slug,
                    chapterList[position].slug
                )
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return chapterList.size
    }

    fun setData(newChapterList: List<Chapter>) {
        chapterList = newChapterList
        notifyDataSetChanged()
    }
}