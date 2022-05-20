package com.example.recipies

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipies.databinding.RecipeFragmentBinding

class RecipeFragment : Fragment() {

    private var _binding: RecipeFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel : RecipeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RecipeFragmentBinding.inflate(inflater, container, false);

        viewModel.getRecipes()?.observe(viewLifecycleOwner) {
            val index = arguments?.getInt("index", 0)
            val title = it[index!!].title
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val index = arguments?.getInt("index", 0)

        binding.edit.setOnClickListener {
            val bundle = bundleOf("index" to index)
            findNavController().navigate(R.id.action_recipeFragment_to_addFragment, bundle)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

