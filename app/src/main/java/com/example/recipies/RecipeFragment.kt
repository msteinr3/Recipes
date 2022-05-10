package com.example.recipies

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
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

        val title = RecipeManager.recipes[index!!].title
        val pic = Uri.parse(RecipeManager.recipes[index].photo)
        val category = RecipeManager.recipes[index].category
        val ingredients = RecipeManager.recipes[index].ingredients
        val instructions = RecipeManager.recipes[index].instructions

        binding.title.text = title
        binding.pic.setImageURI(pic)
        binding.category.text = category
        binding.ingredients.text = ingredients
        binding.instructions.text = instructions

        binding.edit.setOnClickListener {
            val bundle = bundleOf(
                ("title" to title),
                ("pic" to pic),
                ("category" to category),
                ("ingredients" to ingredients),
                ("instructions" to instructions)
            )
            //find index of recipe by title?
            //val i = RecipeManager.recipes<Recipe>.indexOf(title)
            //RecipeManager.remove(i)
            findNavController().navigate(R.id.action_recipeFragment_to_addFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
