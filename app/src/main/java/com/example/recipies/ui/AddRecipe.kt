package com.example.recipies.ui

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.bumptech.glide.Glide
import com.example.recipies.MainActivity
import com.example.recipies.R
import com.example.recipies.databinding.AddRecipeBinding
import com.example.recipies.extra.*
import com.example.recipies.utils.Loading
import com.example.recipies.utils.Success
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

@AndroidEntryPoint
@ProvidedTypeConverter
class AddRecipe : Fragment() {

    private var _binding: AddRecipeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by viewModels()
    private val allViewModel: AllRecipesViewModel by viewModels()

    private var id: Int? = null
    private var recipe: Recipe? = null
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
        println("find host fragment")


        id = arguments?.getInt("id")

        if (id != null) {
            viewModel.recipe.observe(viewLifecycleOwner) {

                when (it.status) {
                    is Success -> {
                        binding.progressBar.visibility = View.GONE
                        recipe = it.status.data!!
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

        val resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                println("result launcher")
                if (result.resultCode == Activity.RESULT_OK) {
                    handleCameraImage(result.data)
                }
            }

        binding.camera.setOnClickListener {
            //smallImageCameraLauncher.launch(null)
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            resultLauncher.launch(cameraIntent)
            println("after intent start")
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
            println("image uri: $imageUri")
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
                    imageUri.toString(),
                    binding.category.text.toString(),
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

        if (id != null) {
            binding.deleteBtn.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.delete)
                builder.setMessage(getString(R.string.sure))
                builder.setPositiveButton(
                    getString(R.string.delete),
                    DialogInterface.OnClickListener { dialog, Id ->
                        viewModel.recipe.observe(viewLifecycleOwner) {
                            //allViewModel.deleteRecipe(it.status.data!!)
                            allViewModel.deleteRecipeById(binding.title.text.toString())
                        }
                        id?.let {
                            viewModel.setId(it)
                        }
                        //deletes, but crashes, idk why
                        findNavController().navigate(R.id.action_addRecipe_to_allRecipes)
                        dialog.cancel()
                    })
                builder.setNegativeButton(
                    getString(R.string.cancel),
                    DialogInterface.OnClickListener { dialog, id ->
                        dialog.cancel()
                    })
                val alert = builder.create()
                alert.setTitle(R.string.delete)
                alert.show()
            }
        } else {
            binding.deleteBtn.visibility = View.GONE
        }
    }

    private fun handleCameraImage(intent: Intent?) {
        val bitmap = intent?.extras?.get("data") as Bitmap
        binding.pic.setImageBitmap(bitmap)
        saveMediaToStorage(bitmap)
    }

    private fun saveMediaToStorage(bitmap: Bitmap) {
        //Generating a file name
        val filename = "${System.currentTimeMillis()}.jpg"

        //Output stream
        var fos: OutputStream? = null
        //var imageUri: Uri?

        //For devices running android >= Q
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            println("new android")
            //getting the contentResolver
            context?.contentResolver?.also { resolver ->

                //Content resolver will process the content values
                val contentValues = ContentValues().apply {

                    //putting file information in content values
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                //Inserting the contentValues to contentResolver and getting the Uri
                imageUri =
                    resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

                //Opening an outputstream with the Uri that we got
                fos = imageUri?.let { resolver.openOutputStream(it) }
                println("image uri in saving: $imageUri")
            }
        } else {
            println("old android")
            //android < Q
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            println(imagesDir)
            val image = File(imagesDir, filename)
            println(image)
            // error for older android, maybe permission?
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            println("saved to photos")
        }
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    private fun updateRecipe(recipe: Recipe) {
        binding.title.text = recipe.title.toEditable()
        binding.category.text = recipe.category?.toEditable()
        binding.pic.setImageURI(Uri.parse(recipe.image))
        imageUri = Uri.parse(recipe.image)
        binding.ingredients.text = recipe.ingredients?.toEditable()
        binding.instructions.text = deleteTags(recipe.instructions).toEditable()
        vegetarian = recipe.vegetarian
        binding.vegetarian.isChecked = vegetarian
        vegan = recipe.vegan
        binding.vegan.isChecked = vegan
        glutenFree = recipe.glutenFree
        binding.glutenFree.isChecked = glutenFree
        Glide.with(requireContext()).load(recipe.image).into(binding.pic)
    }

    private fun deleteTags(text: String): String {
        println("deleting tags")
        println(text)
        var newText = ""
        var add = true
        for (i in 0..text.length - 1) {
            if (text[i] == '<') {
                add = false
            }
            if (add) {
                newText += text[i]
            }
            if (text[i] == '>') {
                add = true
            }
        }
        return newText.replace(".", ".\n")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

