package com.example.demov1.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.demov1.MainActivity
import com.example.demov1.data.AccountStore
import com.example.demov1.databinding.PlashBinding

class Splash: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val prefs = getSharedPreferences("setting", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("isDrankMode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
        super.onCreate(savedInstanceState)
        if (AccountStore(this).activeEmail != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }
        val binding = PlashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, AuthActivity::class.java))
            finish()
        }
    }
}
