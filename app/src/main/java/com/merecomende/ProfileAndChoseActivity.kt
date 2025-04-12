package com.merecomende

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.merecomende.databinding.ActivityProfilleAndChoseBinding

class ProfileAndChoseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilleAndChoseBinding
    private lateinit var reference: DatabaseReference
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val sharedPrefsName = "LoginPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilleAndChoseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.selectMovie.setOnClickListener {
            startActivity(Intent(this, ParametersActivity::class.java))
            finish()
        }

        binding.logOut.setOnClickListener {
            clearLoginData()
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        binding.profileEmail.text = FirebaseAuth.getInstance().currentUser?.email
    }

    private fun clearLoginData() {
        val sharedPreferences = getSharedPreferences(sharedPrefsName, MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }
}