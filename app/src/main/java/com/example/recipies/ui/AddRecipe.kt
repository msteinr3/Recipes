package com.example.recipies.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.recipies.MainActivity
import com.example.recipies.R
import com.example.recipies.databinding.AddRecipeBinding
import com.example.recipies.extra.AllRecipesViewModel
import com.example.recipies.extra.Ingredient
import com.example.recipies.extra.Recipe
import com.example.recipies.extra.RecipeViewModel
import com.example.recipies.utils.Loading
import com.example.recipies.utils.Success
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class AddRecipe : Fragment() {

    private var _binding: AddRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels()
    private val allViewModel: AllRecipesViewModel by viewModels()

    private var id: Int? = null
    private var imageUri: Uri? = null
    private lateinit var file: File
    private var vegetarian: Boolean = false
    private var vegan: Boolean = false
    private var glutenFree: Boolean = false

    private val pickItemLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.pic.setImageURI(it)
            requireActivity().contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUri = it
        }

    private val smallImageCameraLauncher: ActivityResultLauncher<Void> =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) {
            binding.pic.setImageBitmap(it)
        }

    private val fullSizePhotoLauncher: ActivityResultLauncher<Uri> =
        registerForActivityResult(ActivityResultContracts.TakePicture()) {
            if (it) {
                Glide.with(this).load(file).into(binding.pic)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddRecipeBinding.inflate(inflater, container, false);

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        id = arguments?.getInt("id")

        if (id != null) {
            viewModel.recipe.observe(viewLifecycleOwner) {

                when (it.status) {
                    is Success -> {
                        binding.progressBar.visibility = View.GONE
                        updateRecipe(it.status.data!!)
                        binding.recipeCl.visibility = View.VISIBLE
                    }
                    is Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.recipeCl.visibility = View.GONE
                    }
                    is Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(requireContext(), it.status.message, Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }

            id?.let {
                viewModel.setId(it)
            }
        }

        binding.choosePic.setOnClickListener {
            pickItemLauncher.launch(arrayOf("image/*"))
            binding.pic.setImageURI(imageUri)
        }

        binding.camera.setOnClickListener {
            smallImageCameraLauncher.launch(null)
            /*
            file = File(
                requireActivity().getExternalFilesDir(
                    Environment.DIRECTORY_PICTURES
                ),
                "temp.jpg",
                fullSizePhotoLauncher.launch(Uri.fromFile(file))
            )
             */
        }

        binding.vegetarian.setOnClickListener {
            vegetarian = !vegetarian
        }

        binding.vegan.setOnClickListener {
            vegan = !vegan
        }

        binding.glutenFree.setOnClickListener {
            glutenFree = !glutenFree
        }

        binding.finishBtn.setOnClickListener {
            if (binding.title.text.toString().isEmpty() ||
                binding.ingredients.text.toString().isEmpty() ||
                binding.instructions.text.toString().isEmpty() ||
                binding.category.text.toString().isEmpty() ||
                imageUri == null
            ) {
                val builder = StringBuilder("Missing info:\n")

                if (binding.title.text.toString().isEmpty()) {
                    builder.append("Title, ")
                }
                if (binding.ingredients.text.toString().isEmpty()) {
                    builder.append("ingredients, ")
                }
                if (binding.instructions.text.toString().isEmpty()) {
                    builder.append("instructions, ")
                }
                if (binding.category.text.toString().isEmpty()) {
                    builder.append("category, ")
                }
                if (imageUri == null) {
                    builder.append("photo")
                }
                Toast.makeText(requireContext(), builder.toString(), Toast.LENGTH_LONG).show()

            } else {
                if (id == null) {
                    MainActivity.id += -1
                    id = MainActivity.id
                }
                val recipe = Recipe(
                    id!!,
                    binding.title.text.toString(),
                    binding.category.text.toString(),
                    imageUri.toString(),
                    //stringToIngredientsArray(binding.ingredients.text.toString()),
                    binding.ingredients.text.toString(),
                    binding.instructions.text.toString(),
                    vegetarian = vegetarian,
                    vegan = vegan,
                    glutenFree = glutenFree,
                    favorite = false,
                )
                allViewModel.addRecipe(recipe)
                findNavController().navigate(R.id.action_addRecipe_to_allRecipes)
            }
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun stringToIngredientsArray(ingredients: String): Array<Ingredient> {
        val separated = ingredients.split(", ", "\n")
        var ans = mutableListOf<Ingredient>()
        for (i in 0..separated.size) {
            ans.add(Ingredient(separated[i]))
        }
        return ans.toTypedArray()
    }

    private fun ingredientsArrayToString(arr: Array<Ingredient>): String {
        val builder = StringBuilder("")
        for (i in 0..arr.size) {
            builder.append(arr[i].name + ",\n")
        }
        return builder.toString()
    }

    private fun updateRecipe(recipe: Recipe) {
        binding.title.text = recipe.title.toEditable()
        binding.category.text = recipe.category.toEditable()
        binding.pic.setImageURI(Uri.parse(recipe.image))
        imageUri = Uri.parse(recipe.image)
        //binding.ingredients.text = ingredientsArrayToString(recipe.extendedIngredients).toEditable()
        binding.ingredients.text = recipe.extendedIngredients.toEditable()
        binding.instructions.text = recipe.instructions.toEditable()
        vegetarian = recipe.vegetarian
        binding.vegetarian.isChecked = vegetarian
        vegan = recipe.vegan
        binding.vegan.isChecked = vegan
        glutenFree = recipe.glutenFree
        binding.glutenFree.isChecked = glutenFree
        Glide.with(requireContext()).load(recipe.image).into(binding.pic)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

