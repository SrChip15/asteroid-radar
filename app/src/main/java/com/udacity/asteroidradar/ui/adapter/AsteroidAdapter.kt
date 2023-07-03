package com.udacity.asteroidradar.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemViewBinding

class AsteroidAdapter(private val clickListener: AsteroidClickListener) :
    ListAdapter<Asteroid, AsteroidAdapter.AsteroidViewHolder>(AsteroidDiffCallback) {

    class AsteroidViewHolder private constructor(private val binding: AsteroidItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: AsteroidClickListener, asteroid: Asteroid) {
            binding.asteroidClickListener = clickListener
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): AsteroidViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AsteroidItemViewBinding.inflate(layoutInflater, parent, false)
                return AsteroidViewHolder(binding)
            }
        }
    }

    companion object AsteroidDiffCallback : DiffUtil.ItemCallback<Asteroid>() {

        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return newItem === oldItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return newItem == oldItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(clickListener, getItem(position))
    }
}

class AsteroidClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}
