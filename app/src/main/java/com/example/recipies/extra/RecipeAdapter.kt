package com.example.recipies.extra

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.recipies.R
import com.example.recipies.databinding.RecipeLayoutBinding

class RecipeAdapter(private val listener : RecipeItemListener) :
    RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    private val recipes = ArrayList<Recipe>()

    inner class RecipeViewHolder(private val recipeBinding: RecipeLayoutBinding,
                           private val listener: RecipeItemListener)
        : RecyclerView.ViewHolder(recipeBinding.root),
        View.OnClickListener, View.OnLongClickListener {

        init {
            recipeBinding.root.setOnClickListener(this)
            recipeBinding.root.setOnLongClickListener(this)
        }

        fun bind(recipe: Recipe) {
            recipeBinding.recipeTitle.text = recipe.title
            Glide.with(recipeBinding.root).load(recipe.image).centerCrop().into(recipeBinding.recipeImage)

            if (recipe.favorite == true) {
                recipeBinding.favorite.setImageResource(R.drawable.ic_baseline_favorite)
            } else {
                recipeBinding.favorite.setImageResource(R.drawable.ic_baseline_favorite_border)
            }
        }

        override fun onClick(v: View?) {
            listener.onRecipeClick(recipes[adapterPosition].id)
        }

        override fun onLongClick(p0: View?): Boolean {
            listener.onRecipeLongClick(recipes[adapterPosition])
            return true
        }
    }

    fun setRecipes(recipes : Collection<Recipe>) {
        this.recipes.clear()
        this.recipes.addAll(recipes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val binding = RecipeLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecipeViewHolder(binding,listener)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) =
        holder.bind(recipes[position])

    override fun getItemCount() = recipes.size

    interface RecipeItemListener {
        fun onRecipeClick(recipeId : Int)
        fun onRecipeLongClick(recipe: Recipe)
    }
}

