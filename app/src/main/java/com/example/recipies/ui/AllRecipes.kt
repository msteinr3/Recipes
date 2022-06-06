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
        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.recycler.adapter = adapter

        allViewModel.recipes.observe(viewLifecycleOwner) {
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

/*
ItemTouchHelper(object : ItemTouchHelper.Callback() {
    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ) = makeFlag(ItemTouchHelper.ACTION_STATE_SWIPE, LEFT)
    //or makeFlag(ACTION_STATE_DRAG, UP or DOWN or LEFT or RIGHT)

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        //RecipeManager.remove(viewHolder.layoutPosition)
        /*
        viewModel.getRecipes()?.observe(viewLifecycleOwner) {
            val mine = it.filter { !it.internet }
            val index = it.indexOf(mine[viewHolder.layoutPosition])
            viewModel.delete(it[index])
            binding.recycler.adapter!!.notifyItemRemoved(viewHolder.layoutPosition)
        }
         */
    }
}).attachToRecyclerView(binding.recycler)
 */

/*
        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.getRecipes()?.observe(viewLifecycleOwner) {
            //filters recipes by internet (false)
            val mine = it.filter { !it.internet }
            binding.recycler.adapter = RecipeAdapter(mine, object : RecipeAdapter.RecipeListener {

                override fun onRecipeClicked(index: Int) {
                    //pass index
                    val num = it.indexOf(mine[index])
                    val bundle = bundleOf("index" to num)
                    findNavController().navigate(R.id.action_myList_to_singleRecipe, bundle)
                }

                override fun onRecipeLongClicked(index: Int) {
                    mine[index].favorite = !mine[index].favorite
                    viewModel.update(mine[index])

                    binding.recycler.adapter!!.notifyItemChanged(index)
                }
            })
        }
 */