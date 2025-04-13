package com.merecomende

import android.content.Intent
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
    private var chosenGenre: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecomendationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chosenGenre = intent.getStringExtra("choice")

        chosenGenre?.let {
            findMovieByGenre(it)
        } ?: run {
            Toast.makeText(this, "Gênero não selecionado", Toast.LENGTH_SHORT).show()
        }

        binding.returnButton.setOnClickListener {
            startActivity(Intent(this, ParametersActivity::class.java))
        }

    }

    private fun findMovieByGenre(genre: String) {
        binding.progressBar.visibility = View.VISIBLE
        database = FirebaseDatabase.getInstance().getReference("movies")

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.progressBar.visibility = View.GONE

                val filteredMovies = mutableListOf<DataSnapshot>()

                for (filmeSnapshot in snapshot.children) {
                    val gender = filmeSnapshot.child("genre").getValue(String::class.java)
                    if (gender.equals(genre, ignoreCase = true)) {
                        filteredMovies.add(filmeSnapshot)
                    }
                }

                if (filteredMovies.isNotEmpty()) {
                    val randomMovie = filteredMovies[Random.nextInt(filteredMovies.size)]

                    val title =
                        randomMovie.child("title").getValue(String::class.java) ?: "Sem título"
                    val imdb = randomMovie.child("imdb").getValue(String::class.java) ?: "Sem nota"
                    val rating =
                        randomMovie.child("rating").getValue(Double::class.java)?.toString()
                            ?: "Sem classificação"
                    val release =
                        randomMovie.child("release").getValue(Long::class.java)?.toString()
                            ?: "Sem data"
                    val gender =
                        randomMovie.child("genre").getValue(String::class.java) ?: "Sem gênero"

                    binding.txtRecommendedTitle.text = buildString {
                        append("Título: ")
                        append(title)
                    }
                    binding.txtRecommendedImdb.text = buildString {
                        append("IMDb: ")
                        append(imdb)
                    }
                    binding.txtRecommendedRating.text = buildString {
                        append("Classificação: ")
                        append(rating)
                    }
                    binding.txtRecommendedRelease.text = buildString {
                        append("Lançamento: ")
                        append(release)
                    }
                    binding.txtRecommendedGender.text = buildString {
                        append("Gênero: ")
                        append(gender)
                    }
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
