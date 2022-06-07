package com.example.recipies

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.recipies.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentByTag("fragment") as NavHostFragment
        navController = navHostFragment.navController
        val bottomNav = binding.bottomNav
        setupWithNavController(bottomNav, navController)
        navController.addOnDestinationChangedListener { _, item, _ ->
            println("hund")
            println(item)

            if (item.id == R.id.addRecipe || item.id == R.id.singleRecipe) {
                bottomNav.visibility = View.GONE
            }
            else {
                bottomNav.visibility = View.VISIBLE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        // Creating a shared pref object
        val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()

        // write all the data entered by the user in SharedPreference and apply
        myEdit.putInt("id", id)
        myEdit.apply()
    }

    override fun onResume() {
        super.onResume()
        // Fetching stored data from SharedPreference
        val sh = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        id = sh.getInt("id", 0)
    }

    companion object {
        var id = -1
    }
}
