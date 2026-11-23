package com.example.demov1.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.example.demov1.MainActivity
import com.example.demov1.R
import com.example.demov1.data.AccountStore
import com.example.demov1.databinding.ActivityAuthBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val accounts = AccountStore(this)
        if (accounts.activeEmail != null) {
            openDashboard()
            return
        }

        val binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val padding = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime()
            )
            view.setPadding(padding.left, padding.top, padding.right, padding.bottom)
            insets
        }

        val registering = intent.getBooleanExtra(EXTRA_REGISTER, false)
        if (registering) {
            binding.authTitle.setText(R.string.auth_register)
            binding.authSubtitle.setText(R.string.auth_register_intro)
            binding.submit.setText(R.string.auth_register)
            binding.switchMode.setText(R.string.auth_go_to_login)
            binding.confirmLayout.isVisible = true
            binding.password.setAutofillHints("newPassword")
        }

        binding.switchMode.setOnClickListener {
            if (registering) finish()
            else startActivity(Intent(this, AuthActivity::class.java).putExtra(EXTRA_REGISTER, true))
        }

        binding.submit.setOnClickListener {
            binding.emailLayout.error = null
            binding.passwordLayout.error = null
            binding.confirmLayout.error = null
            binding.feedback.isVisible = false

            val email = binding.email.text.toString().trim().lowercase(Locale.ROOT)
            val password = binding.password.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                binding.emailLayout.error = getString(R.string.auth_invalid_email)
                binding.email.requestFocus()
                return@setOnClickListener
            }
            if (password.isEmpty() || (registering && password.length < 8)) {
                binding.passwordLayout.error = getString(
                    if (registering) R.string.auth_short_password else R.string.auth_missing_password
                )
                binding.password.requestFocus()
                return@setOnClickListener
            }
            if (registering && password != binding.confirm.text.toString()) {
                binding.confirmLayout.error = getString(R.string.auth_password_mismatch)
                binding.confirm.requestFocus()
                return@setOnClickListener
            }

            binding.submit.isEnabled = false
            binding.switchMode.isEnabled = false
            lifecycleScope.launch {
                val error = withContext(Dispatchers.IO) {
                    when {
                        registering -> when (accounts.register(email, password)) {
                            AccountStore.Registration.SUCCESS -> null
                            AccountStore.Registration.DUPLICATE_EMAIL -> R.string.auth_email_exists
                            AccountStore.Registration.SAVE_FAILED -> R.string.auth_save_error
                        }
                        accounts.signIn(email, password) -> null
                        else -> R.string.auth_login_error
                    }
                }
                if (error == null) {
                    openDashboard()
                } else {
                    binding.feedback.setText(error)
                    binding.feedback.isVisible = true
                    binding.submit.isEnabled = true
                    binding.switchMode.isEnabled = true
                }
            }
        }
    }

    private fun openDashboard() {
        startActivity(Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
        finish()
    }

    private companion object {
        const val EXTRA_REGISTER = "register"
    }
}
