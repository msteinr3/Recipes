package com.example.recipies

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.recipies.databinding.RecipeFragmentBinding

class RecipeFragment : Fragment() {

    private var _binding: RecipeFragmentBinding? = null
    private val binding get() = _binding!!

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

        val index = arguments?.getInt("index", 0)

        binding.title.text = RecipeManager.recipes[index!!].title
        binding.pic.setImageURI(Uri.parse(RecipeManager.recipes[index].photo))
        binding.category.text = RecipeManager.recipes[index].category
        binding.ingredients.text = RecipeManager.recipes[index].ingredients
        binding.instructions.text = RecipeManager.recipes[index].instructions
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/*
binding.title.text = RecipeManager.recipes[index!!].title
        binding.pic.setImageURI(Uri.parse(RecipeManager.recipes[index].photo))
        binding.ingredients.text = RecipeManager.recipes[index].ingredients
        binding.instructions.text = RecipeManager.recipes[index].instructions


                var list = liked

        when {
            findNavController().currentBackStackEntry.toString() == R.id.favoritesFragment.toString() -> {
                list = liked
            }
            findNavController().currentBackStackEntry.toString() == R.id.homeFragment.toString() -> {
                list = internet
            }
            findNavController().currentBackStackEntry.toString() == R.id.myListFragment.toString() -> {
                list = mine
            }
        }
 */