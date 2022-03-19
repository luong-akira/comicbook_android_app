package com.example.readcomicbook.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.readcomicbook.MainActivity
import com.example.readcomicbook.R
import com.example.readcomicbook.adapter.ComicDetailsAdapter
import com.example.readcomicbook.adapter.CommentSectionAdapter
import com.example.readcomicbook.constants.AppConstants
import com.example.readcomicbook.model.PostComment
import com.example.readcomicbook.repository.ComicRepository
import com.example.readcomicbook.viewmodel.ComicViewModel
import com.example.readcomicbook.viewmodel.ComicViewModelFactory
import kotlinx.android.synthetic.main.comment_row.view.*
import kotlinx.android.synthetic.main.fragment_comic_details.*


class ComicDetailsFragment : Fragment() {
    private val navArgs by navArgs<ComicDetailsFragmentArgs>()
    private val myAdapter by lazy { ComicDetailsAdapter(navArgs.currentComic.slug) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_comic_details, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentComic = navArgs.currentComic
        (requireActivity() as AppCompatActivity).supportActionBar?.title = currentComic.name
        image_details_imageView.load(currentComic.img) {
            crossfade(true)
            placeholder(R.drawable.ic_baseline_cloud_download_24)
        }
        name_details_txt.text = currentComic.name
        author_details_txt.text = currentComic.author
        genre_details_txt.text = currentComic.genres.joinToString(", ")
        average_rating_details_txt.text = currentComic.averageRating.toString()
        if (currentComic.isCompleted) {
            status_details_txt.text = "Completed"
        } else {
            status_details_txt.text = "OnGoing"
        }

        description_details_txt.text = currentComic.description

        comment_section_profile_picture.load("https://upload.wikimedia.org/wikipedia/commons/thumb/3/31/Blogger.svg/1200px-Blogger.svg.png"){
            crossfade(true)
            placeholder(R.drawable.ic_baseline_cloud_download_24)
        }

        comic_details_recyclerView.adapter = myAdapter
        comic_details_recyclerView.layoutManager = LinearLayoutManager(context)
        myAdapter.setData(currentComic.chapters)

        val commentAdapter = CommentSectionAdapter(currentComic.comments)

        comment_recyclerView.adapter = commentAdapter
        comment_recyclerView.layoutManager = LinearLayoutManager(context)

        val prefs = activity?.getSharedPreferences(AppConstants.MY_PREF, Context.MODE_PRIVATE)
        val token = prefs?.getString(AppConstants.TOKEN, "").toString()

        comment_btn.setOnClickListener(View.OnClickListener { view ->
            if(MainActivity.viewModel._token.value != ""){
                val comment = PostComment(comment_et.text.toString())
                MainActivity.viewModel.postCommentOnAComic(token, navArgs.currentComic.slug, comment)
                comment_et.setText("")
            }else{
                findNavController().navigate(R.id.loginFragment)
            }

        })


    }
}