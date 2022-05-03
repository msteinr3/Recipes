package com.example.recipies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipies.databinding.RecipeLayoutBinding

class RecipeAdapter(val recipes: List<Recipe>, private val callback: RecipeListener) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    interface RecipeListener {
        fun onRecipeClicked(index: Int)
        fun onRecipeLongClicked(index: Int)
    }

    inner class RecipeViewHolder(private val binding: RecipeLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener, View.OnLongClickListener {

        init {
            binding.root.setOnClickListener(this)
            binding.root.setOnLongClickListener(this)
        }

        override fun onClick(p0: View?) {
            callback.onRecipeClicked(adapterPosition)
        }

        override fun onLongClick(p0: View?): Boolean {
            callback.onRecipeLongClicked(adapterPosition)
            return true
        }

        fun bind(recipe: Recipe) {
            binding.recipeTitle.text = recipe.title
            Glide.with(binding.root).load(recipe.photo).centerCrop().into(binding.recipeImage)
            if (recipe.favorite) {
                binding.favorite.setImageResource(R.drawable.ic_baseline_favorite)
            } else {
                binding.favorite.setImageResource(R.drawable.ic_baseline_favorite_border)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder =
        RecipeViewHolder(
            RecipeLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false))


    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) =
        holder.bind(recipes[position])

    override fun getItemCount() = recipes.size
}