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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.recipies.R
import com.example.recipies.databinding.AllRecipesBinding
import com.example.recipies.extra.AllRecipesViewModel
import com.example.recipies.extra.Recipe
import com.example.recipies.extra.RecipeAdapter
import com.example.recipies.extra.RecipeViewModel
import com.example.recipies.utils.Loading
import com.example.recipies.utils.Success
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllRecipes : Fragment(), RecipeAdapter.RecipeItemListener {

    private var _binding: AllRecipesBinding? = null
    private val binding get() = _binding!!
    private val allViewModel : AllRecipesViewModel by viewModels()
    private val viewModel : RecipeViewModel by viewModels()
    private  lateinit var  adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AllRecipesBinding.inflate(inflater, container, false);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecipeAdapter(this)
        binding.recycler.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recycler.adapter = adapter

        allViewModel.recipes.observe(viewLifecycleOwner) {
            when(it.status) {
                is Loading -> binding.progressBar.visibility = View.VISIBLE

                is Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.setRecipes(it.status.data!!)
                    binding.number.text = adapter.itemCount.toString()
                }

                is Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(),it.status.message, Toast.LENGTH_LONG).show()
                }
                else -> {}
            }
        }
    }

    override fun onRecipeClick(recipeId: Int) {
        findNavController().navigate(R.id.action_allRecipes_to_singleRecipe,
            bundleOf("id" to recipeId))
    }

    override fun onRecipeLongClick(recipe: Recipe) {
        viewModel.recipe.observe(viewLifecycleOwner) {
        }
        viewModel.setId(recipe.id)
        recipe.favorite = recipe.favorite != true

        viewModel.update(recipe)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
