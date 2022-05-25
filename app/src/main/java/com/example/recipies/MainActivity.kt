package com.example.recipies

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.recipies.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.json.JSONObject
import org.json.JSONTokener
import android.widget.*
import androidx.activity.viewModels
import java.io.*
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.Executors



//@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentByTag("fragment") as NavHostFragment
        navController = navHostFragment.navController
        val bottomNav = binding.bottomNav
        setupWithNavController(bottomNav, navController)

        fetch()
    }

    var client: OkHttpClient = OkHttpClient();

    private fun getRequest(): String? {
        var result: String? = null
        try {
            // Build request
            val request = Request.Builder()
                .url("https://spoonacular-recipe-food-nutrition-v1.p.rapidapi.com/recipes/random?number=2")
                .get()
                .addHeader("X-RapidAPI-Host", "spoonacular-recipe-food-nutrition-v1.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "e3529cf2e2mshfd875deaf5d88f0p1dec52jsn45165f63480a")
                .build()
            // Execute request
            val response = client.newCall(request).execute()
            result = response.body?.string()
            processJson(result)
        }
        catch(err:Error) {
            println("Error when executing get request: " + err.localizedMessage)
        }
        return result
    }

    private fun fetch() {
        lifecycleScope.launch(Dispatchers.IO) {
            getRequest()
        }
    }

    private val viewModel: RecipeViewModel by viewModels<RecipeViewModel>()

    private fun processJson(result: String?) {
        //println(result)
        val jsonObject = JSONTokener(result).nextValue() as JSONObject
        val jsonArray = jsonObject.getJSONArray("recipes")
        //println(jsonObject)
        println(jsonArray)
        for (i in 0 until jsonArray.length()) {
            val recipe = jsonArray.getJSONObject(i)
            println(recipe)
            val title = recipe["title"].toString()
            val instructions = recipe["instructions"].toString()
            val type = recipe.getJSONArray("dishTypes")
            val imageURL = recipe["image"]
            val ingrediencts = recipe.getJSONArray("extendedIngredients")
            var ingredienctsString = "";
            for (j in 0 until ingrediencts.length()) {
                val ingredient = ingrediencts.getJSONObject(j)
                ingredienctsString += "● " + ingredient["original"].toString() + " \n"
            }
            val recipeObj = Recipe(
                title,
                "",
                ingredienctsString,
                instructions,
                "other",
                favorite = false,
                internet = true
            )

            var mImage: Bitmap?
            // Declaring a webpath as a string
            val mWebPath = recipe["image"].toString()

            // Declaring and initializing an Executor and a Handler
            val myExecutor = Executors.newSingleThreadExecutor()
            val myHandler = Handler(Looper.getMainLooper())
            var imageUri = ""
            myExecutor.execute {
                mImage = mLoad(mWebPath)
                myHandler.post {
                    //mImageView.setImageBitmap(mImage)
                    if(mImage!=null){
                        println(mImage)
                        mSaveMediaToStorage(mImage, recipeObj, viewModel)
                    }
                }
            }
        }
    }

    // Function to establish connection and load image
    private fun mLoad(string: String): Bitmap? {
        val url: URL = mStringToURL(string)!!
        val connection: HttpURLConnection?
        try {
            connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val inputStream: InputStream = connection.inputStream
            val bufferedInputStream = BufferedInputStream(inputStream)
            return BitmapFactory.decodeStream(bufferedInputStream)
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error", Toast.LENGTH_LONG).show()
        }
        return null
    }

    // Function to convert string to URL
    private fun mStringToURL(string: String): URL? {
        try {
            return URL(string)
        } catch (e: MalformedURLException) {
            e.printStackTrace()
        }
        return null
    }

    private fun mSaveMediaToStorage(bitmap: Bitmap?, recipeObj: Recipe, viewModel: RecipeViewModel) {
        val filename = "${System.currentTimeMillis()}.jpg"
        var fos: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            this.contentResolver?.also { resolver ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
                    put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }
                val imageUri: Uri? = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
                fos = imageUri?.let { resolver.openOutputStream(it) }
                println(imageUri)
                recipeObj.photo = imageUri.toString()
                viewModel.addRecipe(recipeObj)
            }
        } else {
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }
        fos?.use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Toast.makeText(this , "Saved to Gallery" , Toast.LENGTH_SHORT).show()
        }
    }
}



