package com.example.roomdb_nisrinaai_23

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.roomdb_nisrinaai_23.databinding.ActivityAddBinding
import com.example.roomdb_nisrinaai_23.room.Constant
import com.example.roomdb_nisrinaai_23.room.Movie
import com.example.roomdb_nisrinaai_23.room.MovieDb
import kotlinx.android.synthetic.main.activity_add.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddActivity : AppCompatActivity() {

    val db by lazy { MovieDb(this) }
    private lateinit var binding: ActivityAddBinding

    private var movieID: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupListener()
        movieID = intent.getIntExtra("intent_id", 0)
    }

    fun setupView() {
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType) {
            Constant.TYPE_CREATE -> {
                binding.btnUpdate.visibility = View.GONE
            }
            Constant.TYPE_READ -> {
                binding.btnSave.visibility = View.GONE
                binding.btnUpdate.visibility = View.GONE
                getMovie()
            }
            Constant.TYPE_UPDATE -> {
                binding.btnSave.visibility = View.GONE
                getMovie()
            }
        }
    }

    private fun setupListener() {
        binding.btnSave.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().addMovie(
                    Movie(0, binding.etTitle.text.toString().trim(), binding.etDescription.text.toString().trim())
                )
                finish()
            }
        }
        binding.btnUpdate.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.movieDao().updateMovie(
                    Movie(movieID, binding.etTitle.text.toString().trim(), binding.etDescription.text.toString().trim())
                )
                finish()
            }
        }
    }

    fun getMovie() {
        movieID = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.movieDao().getMovies(movieID)[0]
            binding.etTitle.setText(movies.title)
            binding.etDescription.setText(movies.desc)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}