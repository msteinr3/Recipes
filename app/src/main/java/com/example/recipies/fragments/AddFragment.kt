package com.example.recipies.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.recipies.R
import com.example.recipies.extra.Recipe
import com.example.recipies.extra.RecipeViewModel
import com.example.recipies.databinding.AddFragmentBinding
import java.io.File
import java.lang.StringBuilder

class AddFragment : Fragment() {

    private var _binding: AddFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: RecipeViewModel by activityViewModels()
    private var imageUri: Uri? = null
    private var index: Int? = null
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
        _binding = AddFragmentBinding.inflate(inflater, container, false);

        index = arguments?.getInt("index")
        return binding.root
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (index != null) {
            viewModel.getRecipes()?.observe(viewLifecycleOwner) {
                binding.title.text = it[index!!].title.toEditable()
                imageUri = Uri.parse(it[index!!].photo)
                binding.pic.setImageURI(imageUri)
                //category = it[index!!].category
                binding.ingredients.text = it[index!!].ingredients.toEditable()
                binding.instructions.text = it[index!!].instructions.toEditable()
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

            } else {
                val recipe = Recipe(
                    binding.title.text.toString(),
                    imageUri.toString(),
                    binding.ingredients.text.toString(),
                    binding.instructions.text.toString(),
                    spinner,
                    favorite = false,
                    internet = false
                )
                println(imageUri)
                viewModel.addRecipe(recipe)
                findNavController().navigate(R.id.action_addFragment_to_myListFragment)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

