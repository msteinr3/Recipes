package com.example.recipies

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.recipies.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //fuck you!
        val navHostFragment = supportFragmentManager.findFragmentByTag("fuckyou") as NavHostFragment
        navController = navHostFragment.navController
        val bottomNav = binding.bottomNav
        setupWithNavController(bottomNav, navController)

    }
}



/*

        val homeFragment = HomeFragment()
        val settingsFragment = SettingsFragment()
        val favoritesFragment = FavoritesFragment()
        val addFragment = AddFragment()
        val myListFragment = MyListFragment()


        binding.bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.home -> {
                    switchFragment(homeFragment)
                    true
                }

                R.id.mine -> {
                    switchFragment(myListFragment)
                    true
                }

                R.id.favorites -> {
                    switchFragment(favoritesFragment)
                    true
                }

                R.id.add -> {
                    switchFragment(addFragment)
                    true
                }

                R.id.settings -> {
                    switchFragment(settingsFragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun switchFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.nav_fragment, fragment)
            commit()
        }
 */