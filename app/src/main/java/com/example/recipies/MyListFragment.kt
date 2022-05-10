package com.example.recipies

import android.icu.lang.UCharacter.IndicPositionalCategory.LEFT
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.recipies.databinding.HomeFragmentBinding

class MyListFragment : Fragment() {

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

        //filters recipes by internet (false)
        var mine = RecipeManager.recipes.filter { !it.internet }
        //mine = mine.sortedBy { Recipe -> Recipe.title }

        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter =
            RecipeAdapter(mine, object : RecipeAdapter.RecipeListener {
                override fun onRecipeClicked(index: Int) {
                    //pass index and list
                    val num = RecipeManager.recipes.indexOf(mine[index])
                    val bundle = bundleOf("index" to num)
                    findNavController().navigate(R.id.action_myListFragment_to_recipeFragment, bundle)
                }

                override fun onRecipeLongClicked(index: Int) {
                    mine[index].favorite = !mine[index].favorite
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
                RecipeManager.remove(viewHolder.layoutPosition)
                binding.recycler.adapter!!.notifyItemRemoved(viewHolder.layoutPosition)
            }
        }).attachToRecyclerView(binding.recycler)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}