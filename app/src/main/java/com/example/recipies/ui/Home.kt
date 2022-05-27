package com.example.recipies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.recipies.R
import com.example.recipies.extra.RecipeAdapter
import com.example.recipies.extra.RecipeViewModel
import com.example.recipies.databinding.HomeFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Home : Fragment() {

    private var _binding: HomeFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel : RecipeViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = HomeFragmentBinding.inflate(inflater, container, false);

        binding.recycler.layoutManager = GridLayoutManager(requireContext(), 2)

        viewModel.getRecipes()?.observe(viewLifecycleOwner) {
            //filters recipes by internet (true)
            val internet = it.filter { it.internet }
            binding.recycler.adapter = RecipeAdapter(internet, object : RecipeAdapter.RecipeListener {

                override fun onRecipeClicked(index: Int) {
                    //pass index
                    val num = it.indexOf(internet[index])
                    val bundle = bundleOf("index" to num)
                    findNavController().navigate(R.id.action_home_to_singleRecipe, bundle)
                }

                override fun onRecipeLongClicked(index: Int) {
                    internet[index].favorite = !internet[index].favorite
                    viewModel.update(internet[index])

                    binding.recycler.adapter!!.notifyItemChanged(index)
                }
            })
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.more.setOnClickListener {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
