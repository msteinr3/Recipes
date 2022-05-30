package com.example.recipies.ui

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.recipies.R
import com.example.recipies.extra.RecipeViewModel
import com.example.recipies.databinding.RecipeFragmentBinding
import com.example.recipies.extra.Recipe
import com.example.recipies.utils.Loading
import com.example.recipies.utils.Success
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleRecipe : Fragment() {

    private var _binding: RecipeFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel : RecipeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecipeFragmentBinding.inflate(inflater, container, false);


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.recipe.observe(viewLifecycleOwner) {
            when(it.status) {
                is Success -> {
                    binding.progressBar.visibility = View.GONE
                    updateRecipe(it.status.data!!)
                    binding.characterCl.visibility = View.VISIBLE
                }
                is Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.characterCl.visibility = View.GONE
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
            val index = arguments?.getInt("index", 0)
            val bundle = bundleOf("index" to index)
            findNavController().navigate(R.id.action_singleRecipe_to_addRecipe, bundle)
        }
    }

    private fun updateRecipe(recipe: Recipe) {
        binding.title.text = recipe.title
        binding.category.text = recipe.category
        binding.ingredients.text = recipe.ingredients
        binding.instructions.text = recipe.instructions
        Glide.with(requireContext()).load(recipe.photo).circleCrop().into(binding.pic)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/*
        viewModel.recipe.observe(viewLifecycleOwner) {
            val index = arguments?.getInt("index", 0)
            val title = it.status.data
            //[index!!].title
            val pic = Uri.parse(it[index].photo)
            val category = it[index].category
            val ingredients = it[index].ingredients
            val instructions = it[index].instructions

            binding.title.text = title
            binding.pic.setImageURI(pic)
            binding.category.text = category
            binding.ingredients.text = ingredients
            binding.instructions.text = instructions
        }

 */