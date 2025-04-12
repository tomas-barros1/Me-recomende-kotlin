package com.merecomende

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.merecomende.databinding.ActivityParametersBinding

class ParametersActivity : AppCompatActivity() {

    private lateinit var binding: ActivityParametersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityParametersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToProfile.setOnClickListener {
            val intent = Intent(this, ProfileAndChoseActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = findViewById<android.widget.RadioButton>(checkedId)
            radioButton?.let {
                val escolha = it.text.toString()
                Log.d("ParametersActivity1", "Escolha do usuário: $escolha")

                val intent = Intent(this, RecomendationActivity::class.java)
                intent.putExtra("escolha", escolha)
                startActivity(intent)
                finish()
            }
        }
    }
}