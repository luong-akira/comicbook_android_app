package com.example.readcomicbook.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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


class GenreFragment : Fragment() {
    private lateinit var viewModel: ComicViewModel
    private val navArgs by navArgs<GenreFragmentArgs>()
    private val myAdapter by lazy { ComicListAdapter() }
    private var currentPage: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_genre, container, false)

        val repository = ComicRepository()
        val viewModelFactory = ComicViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ComicViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentGenre = navArgs.currentGenre

        view.findViewById<RecyclerView>(R.id.recyclerView).adapter = myAdapter
        view.findViewById<RecyclerView>(R.id.recyclerView).layoutManager =
            LinearLayoutManager(context)
        view.findViewById<Button>(R.id.next_page_btn).isVisible = false
        view.findViewById<Button>(R.id.previous_page_btn).isVisible = false

        (requireActivity() as AppCompatActivity).supportActionBar?.title = currentGenre.capitalize()

        viewModel.getComicsByGenre(currentGenre)
        viewModel._comics.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                Log.d("Response",response.body()!!.docs.toString())
                response.body()?.docs?.let { myAdapter.setData(it, "GenreFragment") }
                view.findViewById<TextView>(R.id.page_number_txt).text =
                    response.body()!!.paginator.currentPage.toString()
                view.findViewById<Button>(R.id.next_page_btn).isVisible =
                    response.body()!!.paginator.hasNextPage
                view.findViewById<Button>(R.id.previous_page_btn).isVisible =
                    response.body()!!.paginator.hasPrevPage

                currentPage = response.body()!!.paginator.currentPage.toInt()
            }
        })

        view.findViewById<Button>(R.id.next_page_btn).setOnClickListener {
            viewModel.currentPage = currentPage + 1
            viewModel.getComicsByGenre(currentGenre,viewModel.currentPage)
        }

        view.findViewById<Button>(R.id.previous_page_btn).setOnClickListener {
            viewModel.currentPage = currentPage - 1
            viewModel.getComicsByGenre(currentGenre,viewModel.currentPage)
        }
    }
}