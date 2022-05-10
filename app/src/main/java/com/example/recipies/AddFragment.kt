package com.example.recipies

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.recipies.databinding.AddFragmentBinding
import java.lang.StringBuilder

class AddFragment : Fragment() {

    private var _binding: AddFragmentBinding? = null
    private val binding get() = _binding!!
    private var imageUri: Uri? = null

    private val pickItemLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.OpenDocument()) {
            binding.pic.setImageURI(it)
            requireActivity().contentResolver.takePersistableUriPermission(
                it,
                Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
            imageUri = it
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AddFragmentBinding.inflate(inflater, container, false);

        return binding.root
    }

    private fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //set category
        //need to delete existing recipe?

        val title = arguments?.getString("title", "")
        val pic = arguments?.getParcelable<Uri>("pic")
        val category = arguments?.getString("category", "")
        val ingredients = arguments?.getString("ingredients", "")
        val instructions = arguments?.getString("instructions", "")

        binding.title.text = title?.toEditable()
        binding.ingredients.text = ingredients?.toEditable()
        binding.instructions.text = instructions?.toEditable()
        //binding.dropdown.???
        binding.pic.setImageURI(pic)
        imageUri = pic


        var spinner = ""
        val adapter = ArrayAdapter.createFromResource(requireContext(), R.array.categories, R.layout.drop_down)
        binding.dropdown.setAdapter(adapter)
        binding.dropdown.setOnItemClickListener { adapterView, view, i, l ->
            spinner = adapterView.getItemAtPosition(i).toString()
        }

        binding.choosePic.setOnClickListener {
            pickItemLauncher.launch(arrayOf("image/*"))
            binding.pic.setImageURI(imageUri)
        }

        binding.camera.setOnClickListener {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivity(i)
            //save result
            //binding.pic.setImageURI(imageUri)
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
                    binding.title.text.toString(),
                    imageUri.toString(),
                    binding.ingredients.text.toString(),
                    binding.instructions.text.toString(),
                    spinner,
                    favorite = false,
                    internet = false
                )
                RecipeManager.add(recipe)
                findNavController().navigate(R.id.action_addFragment_to_myListFragment)
                //need to hit back can't hit + icon
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

