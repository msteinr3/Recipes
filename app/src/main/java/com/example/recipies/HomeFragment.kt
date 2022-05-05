package com.example.recipies

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils.replace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipies.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*
         //request internet permission
        //get info from API
        val title = ""
        val photo = ""
        val ingredients = ""
        val instructions = ""

        val recipe = Recipe(title, photo, ingredients, instructions, favorite = false, internet = true)
        RecipeManager.add(recipe)
         */

        //implement searchbar

        //filters recipes by internet (true)
        var internet = RecipeManager.recipes.filter { it.internet }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter =
            RecipeAdapter(internet, object : RecipeAdapter.RecipeListener {
                override fun onRecipeClicked(index: Int) {

                    //pass index
                    val num = RecipeManager.recipes.indexOf(internet[index])
                    val bundle = bundleOf("index" to num)
                    findNavController().navigate(R.id.action_homeFragment_to_recipeFragment, bundle)
                }

                override fun onRecipeLongClicked(index: Int) {
                    RecipeManager.recipes[index].favorite = !RecipeManager.recipes[index].favorite
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
                //binding.recycler.adapter = RecipeAdapter(RecipeManager.recipes)
            } else {
                binding.grid.setBackgroundResource(R.color.white)
                binding.grid.tag = "white"
                binding.recycler.layoutManager = LinearLayoutManager(requireContext())
                //binding.recycler.adapter = RecipeAdapter(RecipeManager.recipes)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}