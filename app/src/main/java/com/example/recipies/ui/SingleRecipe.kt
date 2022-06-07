package com.example.recipies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.room.TypeConverters
import com.bumptech.glide.Glide
import com.example.recipies.R
import com.example.recipies.databinding.SingleRecipeBinding
import com.example.recipies.extra.Ingredient
import com.example.recipies.extra.IngredientsConverters
import com.example.recipies.extra.Recipe
import com.example.recipies.extra.RecipeViewModel
import com.example.recipies.utils.Loading
import com.example.recipies.utils.Success
import dagger.hilt.android.AndroidEntryPoint
import java.lang.StringBuilder

@AndroidEntryPoint
@TypeConverters(IngredientsConverters::class)
class SingleRecipe : Fragment() {

    private var _binding: SingleRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel : RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SingleRecipeBinding.inflate(inflater, container, false);


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

/*
        viewModel.recipe.observe(viewLifecycleOwner) {
        }
        viewModel.setId(recipe.id)
        recipe.instructions = deleteTags(recipe.instructions)
        viewModel.update(recipe)

 */


        viewModel.recipe.observe(viewLifecycleOwner) {
            when(it.status) {
                is Success -> {
                    binding.progressBar.visibility = View.GONE
                    updateRecipe(it.status.data!!)
                    binding.recipeCl.visibility = View.VISIBLE
                }
                is Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.recipeCl.visibility = View.GONE
                }
                is Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(),it.status.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        arguments?.getInt("id")?.let {
            viewModel.setId(it)
        }

        binding.edit.setOnClickListener {
            val id = arguments?.getInt("id")
            val bundle = bundleOf("id" to id)
            findNavController().navigate(R.id.action_singleRecipe_to_addRecipe, bundle)
        }
    }

    private fun updateRecipe(recipe: Recipe) {
        binding.title.text = recipe.title
        binding.category.text = recipe.category
        //binding.ingredients.text = ingredientsArrayToString(recipe.extendedIngredients)
        binding.ingredients.text = recipe.ingredients
        binding.instructions.text = deleteTags(recipe.instructions)
        Glide.with(requireContext()).load(recipe.image).into(binding.pic)

        val diet = StringBuilder("")
        if (recipe.vegetarian) {
            diet.append("Vegetarian ")
        }
        if (recipe.vegan) {
            diet.append(" Vegan ")
        }
        if (recipe.glutenFree) {
            diet.append(" Gluten Free")
        }
        binding.dietary.text = diet.toString()
    }

    private fun ingredientsArrayToString(arr : Array<Ingredient>) : String {
        val builder = StringBuilder("")
        for (i in 0..arr.size) {
            builder.append("${arr[i].name} + ,\n")
        }
        return builder.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun deleteTags(text: String): String {
        var newText = ""
        var add = true
        for (i in 0..text.length - 1) {
            if (text[i] == '<') {
                add = false
            }
            if (add) {
                newText += text[i]
            }
            if (text[i] == '>') {
                add = true
            }
        }
        return newText.replace(".", ".\n")
    }
}
