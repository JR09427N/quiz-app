package com.example.testapi

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
import retrofit2.http.Path

interface ApiService {
    @POST("send_id_string")
    fun sendIdString(@Body messageRequest: MessageRequest): Call<MessageRequest>

    @POST("send_selfie_string")
    fun sendSelfieString(@Body messageRequest: MessageRequest): Call<MessageRequest>

    @GET("is_same_face")
    fun isMatch(): Call<IsMatchResponse>

    @GET("read_name_from_id")
    fun nameRead(): Call<ReadNameResponse>

    @GET("get_row")
    fun getQuestionRow(
        @Query("category_id") categoryId: Int,
        @Query("user_id") userId: Int
    ): Call<QuestionRowData>

    @POST("submit_answer/")
    fun submitAnswer(@Body answer: AnswerSubmission): Call<ResponseBody>

    @POST("delete_answer/")
    fun deleteAnswer(@Body answer: AnswerSubmission): Call<ResponseBody>

    @GET("answer_count")
    suspend fun getAnswerCount(
        @Query("user_id") userId: Int,
        @Query("category_id") categoryId: Int
    ): Response<AnswerCountResponse>

    @POST("/favorite_question/")
    fun favoriteQuestion(@Body request: FavoriteRequest): Call<ResponseBody>

    @POST("/repost_question/")
    fun repostQuestion(@Body request: RepostRequest): Call<ResponseBody>

    @POST("/forward_question/")
    fun forwardQuestion(@Body request: ForwardRequest): Call<ResponseBody>

    @GET("/read_sections/")
    suspend fun readSections(): Response<List<SectionData>>

    @POST("submit_question/")
    fun submitQuestion(@Body question: QuestionSubmission): Call<ResponseBody>

    @GET("get_question/")
    suspend fun getUserQuestion(): Response<List<UserQuestionData>>
}