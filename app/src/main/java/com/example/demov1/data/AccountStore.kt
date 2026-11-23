package com.example.demov1.data

import android.content.Context
import android.util.Base64
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

class AccountStore(context: Context) {
    private val preferences = context.getSharedPreferences("local_accounts", Context.MODE_PRIVATE)

    val activeEmail: String?
        get() = preferences.getString("session", null)

    enum class Registration { SUCCESS, DUPLICATE_EMAIL, SAVE_FAILED }

    fun register(email: String, password: String): Registration = synchronized(registrationLock) {
        val key = "account:$email"
        if (preferences.contains(key)) return@synchronized Registration.DUPLICATE_EMAIL
        val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
        val verifier = "${encode(salt)}:${encode(deriveKey(password, salt))}"
        val saved = preferences.edit()
            .putString(key, verifier)
            .putString("session", email)
            .commit()
        if (saved) Registration.SUCCESS else Registration.SAVE_FAILED
    }

    fun signIn(email: String, password: String): Boolean {
        val verifier = preferences.getString("account:$email", null) ?: return false
        val (salt, hash) = verifier.split(':')
        val candidate = deriveKey(password, Base64.decode(salt, Base64.NO_WRAP))
        if (!MessageDigest.isEqual(candidate, Base64.decode(hash, Base64.NO_WRAP))) return false
        return preferences.edit().putString("session", email).commit()
    }

    fun signOut() {
        preferences.edit().remove("session").apply()
    }

    private fun encode(bytes: ByteArray): String = Base64.encodeToString(bytes, Base64.NO_WRAP)

    private fun deriveKey(password: String, salt: ByteArray): ByteArray {
        // Android 7 supports PBKDF2WithHmacSHA1 via SecretKeyFactory.
        val spec = PBEKeySpec(password.toCharArray(), salt, 1_300_000, 160)
        return try {
            SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1").generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }

    private companion object {
        val registrationLock = Any()
    }
}
