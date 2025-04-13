package com.merecomende

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.merecomende.databinding.ActivityProfilleAndChooseBinding

class ProfileAndChooseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfilleAndChooseBinding
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val sharedPrefsName = "LoginPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilleAndChooseBinding.inflate(layoutInflater)
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

        val user = FirebaseAuth.getInstance().currentUser
        binding.profileEmail.text = user?.email

        val userId = user?.uid
        if (userId != null) {
            val userRef = database.getReference("users").child(userId)

            userRef.child("username").get().addOnSuccessListener { snapshot ->
                val username = snapshot.getValue(String::class.java)
                binding.profileUsername.text = username ?: "Nome não encontrado"
            }.addOnFailureListener {
                binding.profileUsername.text = "Erro ao carregar nome"
            }
        }
    }

    private fun clearLoginData() {
        val sharedPreferences = getSharedPreferences(sharedPrefsName, MODE_PRIVATE)
        sharedPreferences.edit() { clear() }
    }
}