package com.example.readcomicbook.fragments


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readcomicbook.MainActivity
import com.example.readcomicbook.R
import com.example.readcomicbook.adapter.ChapterDetailsAdapter
import com.example.readcomicbook.repository.ComicRepository
import com.example.readcomicbook.viewmodel.ComicViewModel
import com.example.readcomicbook.viewmodel.ComicViewModelFactory
import kotlinx.android.synthetic.main.fragment_chapter_details.*

class ChapterDetailsFragment : Fragment() {
    private val navArgs by navArgs<ChapterDetailsFragmentArgs>()
    private val myAdapter by lazy {
        ChapterDetailsAdapter()
    }
    private var numberOfChapter: Int? = null
    private lateinit var viewModel: ComicViewModel
    private var chapterNameList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chapter_details, container, false)

        val repository = ComicRepository()
        val viewModelFactory = ComicViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ComicViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imagesRecyclerView.adapter = myAdapter

        imagesRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.getComic(navArgs.comicSlug)
        viewModel.getChapter(navArgs.comicSlug, navArgs.chapterSlug)

        viewModel._comic.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                numberOfChapter = response.body()!!.chapters.size
                Log.d("Response", "number of chapter is $numberOfChapter ")
                for (chapterName in response.body()!!.chapters) {
                    chapterNameList.add(chapterName.name)
                }

                val arrayAdapter =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.support_simple_spinner_dropdown_item,
                        chapterNameList
                    )
                spinner.adapter = arrayAdapter
            }
        })

        viewModel._chapter.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                (requireActivity() as AppCompatActivity).supportActionBar?.title =
                    response.body()!!.name
                myAdapter.setData(response.body()!!.embededLink)
                if (numberOfChapter != null) {
                    previous_chapter_btn.isVisible = response.body()!!.slug.split("_")[1] != "1"

                    next_chapter_btn.isVisible =
                        response.body()!!.slug.split("_")[1].toInt() <= (numberOfChapter!! - 1)
                }
                if (viewModel.currentChapter > 0) {
                    spinner.setSelection(viewModel.currentChapter - 1)
                } else {
                    spinner.setSelection(0)
                }

            }
        })

        next_chapter_btn.setOnClickListener {
            getNextChapter()
        }


        previous_chapter_btn.setOnClickListener {
            getPreviousChapter()
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("Response", "Item $position $id")
                getChapterByPosition(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("Response", "Nothing Selected")
            }

        }

    }


    private fun getChapterByPosition(position: Int) {
        viewModel.getChapter(navArgs.comicSlug, "chapter_${position + 1}")

    }

    private fun getNextChapter() {
        if (viewModel.currentChapter < numberOfChapter!!) {
            viewModel.getChapter(navArgs.comicSlug, "chapter_${viewModel.currentChapter + 1}")
            chapter_scrollview.scrollTo(0, 0)
        }
    }

    private fun getPreviousChapter() {
        if (viewModel.currentChapter > 1) {
            viewModel.getChapter(navArgs.comicSlug, "chapter_${viewModel.currentChapter - 1}")
            chapter_scrollview.scrollTo(0, 0)
        }
    }
}