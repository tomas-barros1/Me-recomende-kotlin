package com.merecomende

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.merecomende.databinding.ActivityRecomendationBinding
import kotlin.random.Random

class RecomendationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecomendationBinding
    private lateinit var database: DatabaseReference
    private var generoEscolhido: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecomendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        generoEscolhido = intent.getStringExtra("escolha")

        generoEscolhido?.let {
            buscarFilmePorGenero(it)
        } ?: run {
            Toast.makeText(this, "Gênero não selecionado", Toast.LENGTH_SHORT).show()
        }

        binding.returnButton.setOnClickListener {
            finish()
        }

    }

    private fun buscarFilmePorGenero(genero: String) {
        binding.progressBar.visibility = View.VISIBLE
        database = FirebaseDatabase.getInstance().getReference("filmes")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.progressBar.visibility = View.GONE

                val filmesFiltrados = mutableListOf<DataSnapshot>()

                for (filmeSnapshot in snapshot.children) {
                    val gender = filmeSnapshot.child("gender").getValue(String::class.java)
                    if (gender.equals(genero, ignoreCase = true)) {
                        filmesFiltrados.add(filmeSnapshot)
                    }
                }

                if (filmesFiltrados.isNotEmpty()) {
                    val filmeAleatorio = filmesFiltrados[Random.nextInt(filmesFiltrados.size)]

                    val title =
                        filmeAleatorio.child("title").getValue(String::class.java) ?: "Sem título"
                    val imdb =
                        filmeAleatorio.child("imdb").getValue(String::class.java) ?: "Sem nota"
                    val rating = filmeAleatorio.child("rating").getValue(String::class.java)
                        ?: "Sem classificação"
                    val release =
                        filmeAleatorio.child("release").getValue(String::class.java) ?: "Sem data"
                    var gender =
                        filmeAleatorio.child("gender").getValue(String::class.java) ?: "Sem gênero"

                    // Exibir na interface
                    binding.txtRecommendedTitle.text = "Título: $title"
                    binding.txtRecommendedImdb.text = "IMDb: $imdb"
                    binding.txtRecommendedRating.text = "Classificação: $rating"
                    binding.txtRecommendedRelease.text = "Lançamento: $release"
                    binding.txtRecommendedGender.text = "Gênero: $gender"
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@RecomendationActivity,
                    "Erro: ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
