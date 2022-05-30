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
import com.example.recipies.R
import com.example.recipies.databinding.FavoritesFragmentBinding
import com.example.recipies.extra.AllRecipesViewModel
import com.example.recipies.extra.RecipeAdapter
import com.example.recipies.utils.Loading
import com.example.recipies.utils.Success
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Favorites : Fragment(), RecipeAdapter.RecipeItemListener {

    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel : AllRecipesViewModel by viewModels()
    private  lateinit var  adapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FavoritesFragmentBinding.inflate(inflater, container,false);


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = RecipeAdapter(this)
        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycler.adapter = adapter

        viewModel.favoriteRecipes.observe(viewLifecycleOwner) {
            when(it.status) {
                is Loading -> binding.progressBar.visibility = View.VISIBLE

                is Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.setRecipes(it.status.data!!)
                }

                is Error -> {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(requireContext(),it.status.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onRecipeClick(recipeId: Int) {
        findNavController().navigate(R.id.action_favorites_to_singleRecipe,
            bundleOf("id" to recipeId))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/*
        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.getRecipes()?.observe(viewLifecycleOwner) {
            //filters recipes by favorite (true)
            val liked = it.filter { it.favorite }
            binding.recycler.adapter = RecipeAdapter(liked, object : RecipeAdapter.RecipeListener {

                override fun onRecipeClicked(index: Int) {
                    //pass index
                    val num = it.indexOf(liked[index])
                    val bundle = bundleOf("index" to num)
                    findNavController().navigate(R.id.action_favorites_to_singleRecipe, bundle)
                }

                override fun onRecipeLongClicked(index: Int) {
                    liked[index].favorite = !liked[index].favorite
                    viewModel.update(liked[index])

                    binding.recycler.adapter!!.notifyItemChanged(index)
                }
            })
        }
 */