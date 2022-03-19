package com.example.readcomicbook.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.readcomicbook.R
import com.example.readcomicbook.adapter.ComicListAdapter
import com.example.readcomicbook.repository.ComicRepository
import com.example.readcomicbook.viewmodel.ComicViewModel
import com.example.readcomicbook.viewmodel.ComicViewModelFactory
import kotlinx.android.synthetic.main.fragment_home.*

class CompletedComicsFragment : Fragment() {
    private lateinit var viewModel: ComicViewModel
    private val myAdapter by lazy { ComicListAdapter() }
    private var currentPage: Int = 1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_completed_comics, container, false)

        val repository = ComicRepository()
        val viewModelFactory = ComicViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ComicViewModel::class.java)

        viewModel.getCompletedComics()
        return view
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<RecyclerView>(R.id.recyclerView).adapter = myAdapter
        view.findViewById<RecyclerView>(R.id.recyclerView).layoutManager =
            LinearLayoutManager(context)
        view.findViewById<Button>(R.id.next_page_btn).isVisible = false
        view.findViewById<Button>(R.id.previous_page_btn).isVisible = false
        viewModel._comics.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                response.body()?.docs?.let { myAdapter.setData(it, "CompletedComicsFragment") }
                view.findViewById<TextView>(R.id.page_number_txt).text =
                    response.body()!!.paginator.currentPage.toString()
                view.findViewById<Button>(R.id.next_page_btn).isVisible =
                    response.body()!!.paginator.hasNextPage
                view.findViewById<Button>(R.id.previous_page_btn).isVisible =
                    response.body()!!.paginator.hasPrevPage

                currentPage = response.body()!!.paginator.currentPage.toInt()
            } else {
                Log.d("Response", response.code()?.toString())
            }
        })

        view.findViewById<Button>(R.id.next_page_btn).setOnClickListener {
            viewModel.currentPage = currentPage + 1
            viewModel.getCompletedComics(viewModel.currentPage)
        }

        view.findViewById<Button>(R.id.previous_page_btn).setOnClickListener {
            viewModel.currentPage = currentPage - 1
            viewModel.getCompletedComics(viewModel.currentPage)
        }
    }
}