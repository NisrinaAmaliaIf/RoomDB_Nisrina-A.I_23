package com.example.roomdb_nisrinaai_23

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.roomdb_nisrinaai_23.databinding.ActivityMainBinding
import com.example.roomdb_nisrinaai_23.room.Constant
import com.example.roomdb_nisrinaai_23.room.Movie
import com.example.roomdb_nisrinaai_23.room.MovieDb
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_movie.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext



class MainActivity : AppCompatActivity() {
    val db by lazy { MovieDb(this) }
    lateinit var movieAdapter: MovieAdapter

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater )
        setContentView(binding.root)

        setupListener()
        setupRecyclerView()
    }

    override fun onStart() {

        super.onStart()
        loadMovies()
    }
    fun loadMovies(){
        CoroutineScope(Dispatchers.IO).launch {
            val movies = db.movieDao().getMovies()
            Log.d("MainActivity", "dbresponse: $movies")
            withContext(Dispatchers.Main){
                movieAdapter.setData(movies)
            }
        }
    }

    private fun setupListener(){
        binding.addMovie.setOnClickListener {
            intentEdit(0, Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(movieId: Int, intentType: Int){
        startActivity(
            Intent(this@MainActivity, AddActivity::class.java)
                .putExtra("intent_id", movieId)
                .putExtra("intent_type", intentType)
        )
    }

    private fun setupRecyclerView(){
        movieAdapter = MovieAdapter(arrayListOf(), object : MovieAdapter.OnAdapterChangeListener{
            override fun onRead(movie: Movie) {
                // read detail note
                intentEdit(movie.id, Constant.TYPE_READ)

            }

            override fun onUpdate(movie: Movie) {
                intentEdit(movie.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(movie: Movie) {
               deleteDialog(movie)
                }
        })

        binding.rvMovie.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = movieAdapter
        }
    }
    private fun deleteDialog(movie : Movie){
        val alertDeleteDialog = AlertDialog.Builder(this)
        alertDeleteDialog.apply {
            setTitle("Confirmation")
            setMessage("Yakin nih mau menghapus ${movie.title} ?")
            setNegativeButton("Cancel deh") { dialogInterface, i -> dialogInterface.dismiss() }
            setPositiveButton("Gas Delete") { dialogInterface, i ->
                dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.movieDao().deleteMovie(movie)
                    loadMovies()
                }
            }
        }
        alertDeleteDialog.show()
    }
}





