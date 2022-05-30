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
    private val viewModel : RecipeViewModel by viewModels()
    private val allViewModel : AllRecipesViewModel by viewModels()

    private var id : Int? = null
    private var imageUri: Uri? = null
    private lateinit var spinner : String
    private lateinit var file: File

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

        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.categories,
            R.layout.drop_down
        )
        binding.dropdown.setAdapter(adapter)
        binding.dropdown.setOnItemClickListener { adapterView, view, i, l ->
            spinner = adapterView.getItemAtPosition(i).toString()
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

        binding.finishBtn.setOnClickListener {
            if (binding.title.text.toString().isEmpty() ||
                binding.ingredients.text.toString().isEmpty() ||
                binding.instructions.text.toString().isEmpty() ||
                spinner.isEmpty() ||
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
                if (spinner.isEmpty()) {
                    builder.append("category, ")
                }
                if (imageUri == null) {
                    builder.append("photo")
                }
                Toast.makeText(requireContext(), builder.toString(), Toast.LENGTH_LONG).show()

            } else {
                val recipe = Recipe(
                    0,  //need different IDs for each
                    binding.title.text.toString(),
                    spinner,
                    imageUri.toString(),
                    stringToIngredientsArray(binding.ingredients.text.toString()),
                    binding.instructions.text.toString(),
                    favorite = false,
                )
                //println(imageUri)
                allViewModel.addRecipe(recipe)
                findNavController().navigate(R.id.action_addRecipe_to_allRecipes)
            }
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun stringToIngredientsArray(ingredients : String) : Array<Ingredient> {
        val separated = ingredients.split(" ", ", ", "\n")
        var ans = mutableListOf<Ingredient>()
        for (i in 0..separated.size) {
            ans.add(Ingredient(separated[i]))
        }
        return ans.toTypedArray()
    }

    private fun ingredientsArrayToString(arr : Array<Ingredient>) : String {
        val builder = java.lang.StringBuilder("")
        for (i in 0..arr.size) {
            builder.append(arr[i].name + ",\n")
        }
        return builder.toString()
    }

    private fun updateRecipe(recipe: Recipe) {
    binding.title.text = recipe.title.toEditable()
    //binding.category.text = recipe.category.toEditable()
    binding.ingredients.text = ingredientsArrayToString(recipe.extendedIngredients).toEditable()
    binding.instructions.text = recipe.instructions.toEditable()
    Glide.with(requireContext()).load(recipe.image).into(binding.pic)
}

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/*
} else if (index != null) {

viewModel.getRecipes()?.observe(viewLifecycleOwner) {
    it[index!!].title = binding.title.text.toString()
    it[index!!].photo = imageUri.toString()
    it[index!!].ingredients = binding.ingredients.text.toString()
    it[index!!].instructions = binding.instructions.text.toString()
    it[index!!].category = spinner

    viewModel.update(it[index!!])
}
 */


/*
if (id != null) {
    //val recipe : Recipe = viewModel._recipe(id)
    viewModel.recipe.observe(viewLifecycleOwner) {




        binding.title.text = it[index!!].title.toEditable()
        imageUri = Uri.parse(it[index!!].photo)
        binding.pic.setImageURI(imageUri)
        //category = it[index!!].category
        binding.ingredients.text = it[index!!].ingredients.toEditable()
        binding.instructions.text = it[index!!].instructions.toEditable()
    }
}
//get id from bundle
//use id to get recipe object
//parse info from recipe

arguments?.getInt("id")?.let {
    viewModel.setId(it)
}

 */
