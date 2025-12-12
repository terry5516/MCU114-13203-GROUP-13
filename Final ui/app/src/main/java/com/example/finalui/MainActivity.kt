package com.example.finalui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var mainFragment: MainFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            mainFragment = MainFragment()
            replaceFragment(mainFragment!!)
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    mainFragment?.switchPage(MainFragment.PAGE_HOME)
                    true
                }
                R.id.navigation_search -> {
                    mainFragment?.switchPage(MainFragment.PAGE_SEARCH)
                    true
                }
                R.id.navigation_favorites -> {
                    mainFragment?.switchPage(MainFragment.PAGE_SCHEDULE)
                    true
                }
                R.id.navigation_profile -> {
                    mainFragment?.switchPage(MainFragment.PAGE_PROFILE)
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}