package com.example.readcomicbook.fragments

import android.content.Context
import android.os.Bundle
import android.os.Message
import android.util.AndroidRuntimeException
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.readcomicbook.MainActivity
import com.example.readcomicbook.R
import com.example.readcomicbook.adapter.ComicListAdapter
import com.example.readcomicbook.constants.AppConstants.Companion.MY_PREF
import com.example.readcomicbook.constants.AppConstants.Companion.TOKEN
import com.example.readcomicbook.repository.ComicRepository
import com.example.readcomicbook.viewmodel.ComicViewModel
import com.example.readcomicbook.viewmodel.ComicViewModelFactory
import com.google.gson.JsonSyntaxException
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlin.Exception


class HomeFragment : Fragment() {
    private lateinit var viewModel: ComicViewModel
    private val myAdapter by lazy { ComicListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val repository = ComicRepository()
        val viewModelFactory = ComicViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ComicViewModel::class.java)

        viewModel.getComics()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.adapter = myAdapter
        recyclerView.layoutManager = LinearLayoutManager(context)
        next_page_btn.isVisible = false
        previous_page_btn.isVisible = false

        comic_searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchComic(comic_searchView.query.toString())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d("Response", "Text Changed")
                return true
            }

        })

        comic_searchView.setOnCloseListener(object : SearchView.OnCloseListener,
            android.widget.SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                viewModel.getComics()
                return true
            }
        })

        viewModel._comics.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                response.body()?.docs?.let { myAdapter.setData(it, "HomeFragment") }
                page_number_txt.text = response.body()!!.paginator.currentPage.toString()
                next_page_btn.isVisible = response.body()!!.paginator.hasNextPage
                previous_page_btn.isVisible = response.body()!!.paginator.hasPrevPage
            }
        })
    }
}