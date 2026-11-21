package com.example.demov1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.demov1.databinding.ActivityMainBinding
import com.example.demov1.ui.ExpenseFragment
import com.example.demov1.ui.HomeFragment
import com.example.demov1.ui.SettingsFragment


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNavigationView = binding.bottomNavigation
        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.action_home -> HomeFragment()
                R.id.transaction -> ExpenseFragment()
                R.id.action_settings -> SettingsFragment()
                else -> return@setOnItemSelectedListener false
            }

            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, fragment)
                .commit()
            true
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.fragmentContainer.id, HomeFragment())
                .commit()
        }
    }
}
