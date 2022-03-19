package com.example.readcomicbook

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.readcomicbook.constants.AppConstants
import com.example.readcomicbook.fragments.GenreFragmentArgs
import com.example.readcomicbook.repository.ComicRepository
import com.example.readcomicbook.viewmodel.ComicViewModel
import com.example.readcomicbook.viewmodel.ComicViewModelFactory
import com.google.android.material.navigation.NavigationView
import com.google.android.material.slider.RangeSlider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    companion object {
        lateinit var viewModel: ComicViewModel
        lateinit var repository: ComicRepository
    }

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var token: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        repository = ComicRepository()
        val viewModelFactory = ComicViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ComicViewModel::class.java)


        navController = findNavController(R.id.fragment)
        drawerLayout = findViewById(R.id.drawer_layout)
        navigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.loginFragment,
                R.id.completedComicsFragment,
                R.id.genreFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        navigationView.setNavigationItemSelectedListener(this)

        val prefs = getSharedPreferences(AppConstants.MY_PREF, Context.MODE_PRIVATE)
        token = prefs.getString(AppConstants.TOKEN, "").toString()

        viewModel._token.postValue(token)

        viewModel._token.observe(this, {
            Log.d("Response", "Token is $it")
            if (it != "") {
                viewModel.getLoggedInUser(it)

                navigationView.menu.findItem(R.id.loginFragment).isVisible = false
                navigationView.menu.findItem(R.id.logout).isVisible = true
            } else {
                Log.d("Response", "No token")
                navigationView.menu.findItem(R.id.loginFragment).isVisible = true
                navigationView.menu.findItem(R.id.logout).isVisible = false
            }
        })

        viewModel._loggedInUser.observe(this, { response ->
            if (response.isSuccessful) {
                logged_in_username_txt.text = response.body()!!.username
                logged_in_email_txt.text = response.body()!!.email
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.homeFragment -> {
                findNavController(R.id.fragment).navigate(R.id.homeFragment)
                drawerLayout.closeDrawers()
            }
            R.id.loginFragment -> {
                findNavController(R.id.fragment).navigate(R.id.loginFragment)
                drawerLayout.closeDrawers()
            }
            R.id.completedComicsFragment -> {
                findNavController(R.id.fragment).navigate(R.id.completedComicsFragment)
                drawerLayout.closeDrawers()
            }
            R.id.logout -> {
                val sharedPref = getSharedPreferences(AppConstants.MY_PREF, Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()
                editor?.putString(AppConstants.TOKEN, "")
                editor?.apply()
                logged_in_username_txt.text = ""
                logged_in_email_txt.text = ""
                viewModel._token.value = ""
                drawerLayout.closeDrawers()
            }

            R.id.action_genre -> {
                findNavController(R.id.fragment).navigate(
                    R.id.genreFragment,
                    GenreFragmentArgs("action").toBundle()
                )
                drawerLayout.closeDrawers()
            }

            R.id.mystery_genre -> {
                findNavController(R.id.fragment).navigate(
                    R.id.genreFragment,
                    GenreFragmentArgs("mystery").toBundle()
                )
                drawerLayout.closeDrawers()
            }
        }

        return true
    }

}