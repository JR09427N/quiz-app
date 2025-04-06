package com.example.testapi

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.awaitResponse
import java.io.IOException

class YourRepository {
    suspend fun sendIdStringToFastAPI(stringToSend: String): String {
        return withContext(Dispatchers.IO) {
            val response = RetrofitClient.apiService.sendIdString(MessageRequest(stringToSend)).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.message ?: "No response body" // Safe call to access `message`
            } else {
                "Error: ${response.code()}"
            }
        }
    }

    suspend fun sendSelfieStringToFastAPI(stringToSend: String): String {
        return withContext(Dispatchers.IO) {
            val response = RetrofitClient.apiService.sendSelfieString(MessageRequest(stringToSend)).awaitResponse()
            if (response.isSuccessful) {
                response.body()?.message ?: "No response body" // Safe call to access `message`
            } else {
                "Error: ${response.code()}"
            }
        }
    }

    suspend fun submitAnswer(answer: AnswerSubmission): String {
        return withContext(Dispatchers.IO) {
            val response = try {
                RetrofitClient.apiService.submitAnswer(answer).awaitResponse()
            } catch (e: Exception) {
                return@withContext "Error: ${e.message}" // Handle network errors
            }

            if (response.isSuccessful) {
                response.body()?.string() ?: "No response body"
            } else {
                "Error: ${response.code()} - ${response.errorBody()?.string()}"
            }
        }
    }

    suspend fun getRecentAnswerCount(userId: Int, categoryId: Int): Int {
        val response = RetrofitClient.apiService.getAnswerCount(
            userId = userId,
            categoryId = categoryId
        )
        return if (response.isSuccessful) {
            response.body()?.count ?: 0
        } else {
            throw IOException("Failed to fetch answer count: ${response.errorBody()?.string()}")
        }
    }

    suspend fun addToFavorites(questionData: FavoriteRequest): String {
        return withContext(Dispatchers.IO) {
            val response = try {
                RetrofitClient.apiService.favoriteQuestion(questionData).awaitResponse()
            } catch (e: Exception) {
                return@withContext "Error: ${e.message}" // Handle network errors
            }

            if (response.isSuccessful) {
                response.body()?.string() ?: "No response body"
            } else {
                "Error: ${response.code()} - ${response.errorBody()?.string()}"
            }
        }
    }

    suspend fun addToReposts(questionData: RepostRequest): String {
        return withContext(Dispatchers.IO) {
            val response = try {
                RetrofitClient.apiService.repostQuestion(questionData).awaitResponse()
            } catch (e: Exception) {
                return@withContext "Error: ${e.message}" // Handle network errors
            }

            if (response.isSuccessful) {
                response.body()?.string() ?: "No response body"
            } else {
                "Error: ${response.code()} - ${response.errorBody()?.string()}"
            }
        }
    }

    suspend fun addToForwards(questionData: ForwardRequest): String {
        return withContext(Dispatchers.IO) {
            val response = try {
                RetrofitClient.apiService.forwardQuestion(questionData).awaitResponse()
            } catch (e: Exception) {
                return@withContext "Error: ${e.message}" // Handle network errors
            }

            if (response.isSuccessful) {
                response.body()?.string() ?: "No response body"
            } else {
                "Error: ${response.code()} - ${response.errorBody()?.string()}"
            }
        }
    }

    suspend fun fetchSections(): List<SectionData> {
        val response = RetrofitClient.apiService.readSections()
        if (response.isSuccessful) {
            return response.body() ?: emptyList()
        } else {
            throw IOException("Error: ${response.errorBody()?.string()}")
        }
    }
}
