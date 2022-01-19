package com.example.roomdb_nisrinaai_23

import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb_nisrinaai_23.room.Movie
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.roomdb_nisrinaai_23.databinding.ListMovieBinding
import kotlinx.android.synthetic.main.list_movie.view.*
import java.util.*

class MovieAdapter(private val movies: ArrayList<Movie>, private val listener: OnAdapterChangeListener)
    : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(val binding:ListMovieBinding):
        RecyclerView.ViewHolder(binding.root)
    interface OnAdapterChangeListener {
        fun onRead(movie: Movie)
        fun onUpdate(movie: Movie)
        fun onDelete(movie: Movie)
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(ListMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie  = movies[position]
        holder.binding.textTitle.text = movie.title
        holder.binding.textTitle.setOnClickListener {
            listener.onRead( movie )
        }
        holder.binding.iconEdit.setOnClickListener {
            listener.onUpdate( movie )
        }
        holder.binding.iconDelete.setOnClickListener {
            listener.onDelete( movie )
        }
    }

    override fun getItemCount() = movies.size

    fun setData(list: List<Movie>){
        movies.clear()
        movies.addAll(list)
        notifyDataSetChanged()
    }

}