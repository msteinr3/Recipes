package com.example.recipies

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipies.databinding.FavoritesFragmentBinding
import com.example.recipies.databinding.SettingsFragmentBinding

class FavoritesFragment : Fragment() {

    private var _binding: FavoritesFragmentBinding? = null
    private val binding get() = _binding!!

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

        //filters recipes by favorite (true)
        var liked = RecipeManager.recipes.filter { it.favorite }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = RecipeAdapter(liked, object : RecipeAdapter.RecipeListener {
                override fun onRecipeClicked(index: Int) {
                    //pass index
                    val num = RecipeManager.recipes.indexOf(liked[index])
                    val bundle = bundleOf("index" to num)
                    findNavController().navigate(R.id.action_favoritesFragment_to_recipeFragment, bundle)
                }

            override fun onRecipeLongClicked(index: Int) {
                liked[index].favorite = !liked[index].favorite
                if (binding.grid.tag == "white") {
                    binding.recycler.layoutManager = LinearLayoutManager(requireContext())
                } else {
                    binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
                }
            }
        })

        binding.grid.setOnClickListener {
            if (binding.grid.tag == "white") {
                binding.grid.tag = "yellow"
                binding.grid.setBackgroundResource(R.color.yellow)
                binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
            } else {
                binding.grid.setBackgroundResource(R.color.white)
                binding.grid.tag = "white"
                binding.recycler.layoutManager = LinearLayoutManager(requireContext())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}