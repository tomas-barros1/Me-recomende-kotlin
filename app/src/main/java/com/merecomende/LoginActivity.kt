package com.merecomende

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.merecomende.databinding.LoginActivityBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: LoginActivityBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        val sharedPref = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
        val rememberMe = sharedPref.getBoolean("rememberMe", false)
        val savedEmail = sharedPref.getString("email", "")
        val savedPassword = sharedPref.getString("password", "")

        if (rememberMe
            && !savedEmail.isNullOrEmpty()
            && !savedPassword.isNullOrEmpty()
        ) {
            loginUser(savedEmail, savedPassword)
            return
        }

        if (rememberMe) {
            binding.emailLoginField.setText(savedEmail)
            binding.passwordLoginField.setText(savedPassword)
            binding.rememberMe.isChecked = true
        }

        binding.loginButton.setOnClickListener {
            val email = binding.emailLoginField.text.toString()
            val password = binding.passwordLoginField.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginUser(email, password)
            } else {
                Toast.makeText(
                    this,
                    "Por favor, preencha todos os campos.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.signUpText.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.rememberMe.setOnCheckedChangeListener { _, isChecked ->
            val editor = sharedPref.edit()
            if (isChecked) {
                editor.putString("email", binding.emailLoginField.text.toString())
                editor.putString("password", binding.passwordLoginField.text.toString())
                editor.putBoolean("rememberMe", true)
                Toast.makeText(
                    this,
                    "Lembrar conta ativado",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                editor.clear()
                Toast.makeText(
                    this,
                    "Lembrar conta desativado",
                    Toast.LENGTH_SHORT
                ).show()
            }
            editor.apply()
        }
    }

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, ProfileAndChooseActivity::class.java))
                    finish()
                } else {
                    Toast.makeText(
                        this,
                        "Falha no login: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
