package com.example.readcomicbook.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.readcomicbook.MainActivity
import com.example.readcomicbook.R
import com.example.readcomicbook.constants.AppConstants.Companion.MY_PREF
import com.example.readcomicbook.constants.AppConstants.Companion.TOKEN
import com.example.readcomicbook.model.LoginUser
import com.example.readcomicbook.repository.ComicRepository
import com.example.readcomicbook.viewmodel.ComicViewModel
import com.example.readcomicbook.viewmodel.ComicViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {
    private lateinit var viewModel: ComicViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val repository = ComicRepository()
        val viewModelFactory = ComicViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ComicViewModel::class.java)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signin_btn.setOnClickListener {
            val email = email_register_et.text.toString()
            val password = password_register_et.text.toString()
            val user = LoginUser(email, password)
            viewModel.login(user)
        }

        signup_in_loginscreen_txt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }



        viewModel._loginResponse.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                Log.d("Response", response.body()!!)
                val sharedPref = activity?.getSharedPreferences(MY_PREF, Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                editor?.putString(TOKEN, response.body()!!.toString())
                editor?.apply()

                MainActivity.viewModel._token.value = response.body()!!

                findNavController().navigate(R.id.homeFragment)
            } else {
                Toast.makeText(requireContext(), "Invalid Credentials", Toast.LENGTH_SHORT).show()
            }
        })


    }
}