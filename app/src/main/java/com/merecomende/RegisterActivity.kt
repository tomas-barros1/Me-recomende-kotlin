package com.merecomende

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.merecomende.databinding.RegisterActivityBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: RegisterActivityBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = RegisterActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.registerButton.setOnClickListener {
            validateFields()
        }

    }

    private fun validateFields() {
        val username = binding.usernameField.text.toString().trim()
        val email = binding.emailField.text.toString().trim()
        val password = binding.passwordField.text.toString().trim()
        val confirmPassword = binding.confirmPasswordField.text.toString().trim()

        if (username.isEmpty()) {
            binding.usernameField.error = "Nome é obrigatório"
            return
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailField.error = "Email inválido"
            return
        }

        if (password.isEmpty() || password.length < 6) {
            binding.passwordField.error = "A senha precisa ter pelo menos 6 caracteres"
            return
        }

        if (password != confirmPassword) {
            binding.confirmPasswordField.error = "As senhas não coincidem"
            return
        }

        registerUser(email, password)
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    auth.currentUser
                    Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT)
                        .show()
                    startActivity(Intent(this, LoginActivity::class.java))
                } else {
                    Toast.makeText(
                        this,
                        "Falha ao cadastrar: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
