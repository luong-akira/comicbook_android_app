package com.example.readcomicbook.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.navigateUp
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.readcomicbook.R
import com.example.readcomicbook.fragments.CompletedComicsFragmentDirections
import com.example.readcomicbook.fragments.GenreFragmentDirections
import com.example.readcomicbook.fragments.HomeFragmentDirections
import com.example.readcomicbook.model.Comic
import kotlinx.android.synthetic.main.comic_row.view.*

class ComicListAdapter : RecyclerView.Adapter<ComicListAdapter.MyViewHolder>() {
    private var myComicList = emptyList<Comic>()
    var navController: NavController? = null
    private var currentFragment: String = ""

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.comic_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemView.title_txt.text = myComicList[position].name
        holder.itemView.author_txt.text = myComicList[position].author
        //Picasso.get().load(myComicList[position].img).into(holder.itemView.coverComicPicture)
        holder.itemView.coverComicPicture.load(myComicList[position].img) {
            crossfade(true)
            placeholder(R.drawable.ic_baseline_cloud_download_24)

        }
        if(currentFragment=="HomeFragment"){
            holder.itemView.more_btn.setOnClickListener {
                navController = Navigation.findNavController(it)
                val action =
                    HomeFragmentDirections.actionHomeFragmentToComicDetailsFragment(myComicList[position])
                navController!!.navigate(action)
            }
        }else if(currentFragment == "CompletedComicsFragment"){
            holder.itemView.more_btn.setOnClickListener {
                navController = Navigation.findNavController(it)
                val action =
                    CompletedComicsFragmentDirections.actionCompletedComicsFragmentToComicDetailsFragment(myComicList[position])
                navController!!.navigate(action)
            }
        }else if(currentFragment == "GenreFragment"){
            holder.itemView.more_btn.setOnClickListener {
                navController = Navigation.findNavController(it)
                val action =
                    GenreFragmentDirections.actionGenreFragmentToComicDetailsFragment(myComicList[position])
                navController!!.navigate(action)
            }
        }

    }

    override fun getItemCount(): Int {
        return myComicList.size
    }

    fun setData(comicList: List<Comic>, newFragment: String) {
        myComicList = comicList
        currentFragment = newFragment
        notifyDataSetChanged()
    }
}