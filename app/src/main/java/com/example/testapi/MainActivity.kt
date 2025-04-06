package com.example.testapi

import android.os.Bundle
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope
import retrofit2.awaitResponse

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.widget.TextView
import com.example.testapi.RetrofitClient.apiService
import java.io.ByteArrayOutputStream
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class MainActivity : AppCompatActivity() {

    private val repository = YourRepository()
    private lateinit var tvMatchResponse: TextView
    private lateinit var tvNameResponse: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvMatchResponse = findViewById(R.id.tv_match_response)
        tvNameResponse = findViewById(R.id.tv_name)

        // convert image to string
        val imageString1 = convertImageToBase64(this, R.drawable.kevin_hart2)
        val imageString2 = convertImageToBase64(this, R.drawable.kevin_hart3)


        // send id image string to server
        lifecycleScope.launch {
            val response = repository.sendIdStringToFastAPI(imageString1)
            // Handle the response here
            println(response)
        }

        // send selfie image string to server
        lifecycleScope.launch {
            val response = repository.sendSelfieStringToFastAPI(imageString2)
            // Handle the response here
            println(response)
        }

        // get match response from server
        lifecycleScope.launch {
            delay(30000)
            checkFaceMatch()
            //readText()
        }
    }

    suspend fun readText() {
        try {
            val response = apiService.nameRead().awaitResponse()
            if (response.isSuccessful) {
                val nameResponse = response.body()
                nameResponse?.let {
                    runOnUiThread {
                        println("Received nameRead: ${it.nameRead}") // Log the value
                        tvNameResponse.text = "Name: ${it.nameRead}"
                    }
                }
            } else {
                val errorText = "Error: ${response.errorBody()?.string()}"
                println("Error Response: $errorText")  // Log the error
                runOnUiThread {
                    tvNameResponse.text = errorText
                }
            }
        } catch (e: Exception) {
            val exceptionText = "Exception: ${e.message}"
            println("Exception: $exceptionText")  // Log the exception
            runOnUiThread {
                tvNameResponse.text = exceptionText
            }
        }
    }

    private suspend fun checkFaceMatch() {
        try {
            val response = apiService.isMatch().awaitResponse()
            if (response.isSuccessful) {
                val matchResponse = response.body()
                matchResponse?.let {
                    val isMatchText = if (it.isMatch) "Faces match" else "Faces do not match"
                    println("Received isMatch: ${it.isMatch}")  // Log the value

                    // Update UI on the main thread
                    runOnUiThread {
                        tvMatchResponse.text = isMatchText
                    }
                }
            } else {
                val errorText = "Error: ${response.errorBody()?.string()}"
                println("Error Response: $errorText")  // Log the error
                runOnUiThread {
                    tvMatchResponse.text = errorText
                }
            }
        } catch (e: Exception) {
            val exceptionText = "Exception: ${e.message}"
            println("Exception: $exceptionText")  // Log the exception
            runOnUiThread {
                tvMatchResponse.text = exceptionText
            }
        }
    }

    // function to convert image to string
    fun convertImageToBase64(context: Context, imageResId: Int): String {
        val bitmap = BitmapFactory.decodeResource(context.resources, imageResId) // Load the image
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream) // Compress to JPEG

        val imageBytes = byteArrayOutputStream.toByteArray() // Get the byte array
        return Base64.encodeToString(imageBytes, Base64.DEFAULT) // Encode to Base64 and return
    }
}
