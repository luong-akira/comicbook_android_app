package com.example.readcomicbook.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.readcomicbook.MainActivity
import com.example.readcomicbook.R
import com.example.readcomicbook.constants.AppConstants
import com.example.readcomicbook.model.RegisterUser
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signup_btn.setOnClickListener { register() }



        MainActivity.viewModel._registerResponse.observe(viewLifecycleOwner, { response ->
            if(response.isSuccessful){
                val sharedPref = activity?.getSharedPreferences(AppConstants.MY_PREF, Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                editor?.putString(AppConstants.TOKEN, response.body()!!.toString())
                editor?.apply()

                MainActivity.viewModel._token.value = response.body()!!

                findNavController().navigate(R.id.homeFragment)
            }
        })
    }

    private fun register(){
        if (fullname_et.text.isNotEmpty() && email_register_et.text.isNotEmpty() && password_register_et.text.isNotEmpty() && confirmpassword_register_et.text.isNotEmpty()) {
            if (password_register_et.text.toString() == confirmpassword_register_et.text.toString()) {
                val user = RegisterUser(
                    fullname_et.text.toString(),
                    email_register_et.text.toString(),
                    password_register_et.text.toString()
                )
                MainActivity.viewModel.register(user)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Your confirm password and your password must be the same",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(requireContext(), "Please enter all field", Toast.LENGTH_SHORT).show()
        }
    }

}