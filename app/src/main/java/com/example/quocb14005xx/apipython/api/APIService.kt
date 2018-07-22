package com.example.quocb14005xx.apipython.api

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.Executors

interface APIService {

    @POST("/api/post_some_data")
    fun getPredictLabelPost(@Body body: JsonObject): retrofit2.Call<ResponseBody>


    companion object {
        fun create(): APIService {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://172.16.2.213:5000/")
                    .callbackExecutor(Executors.newSingleThreadExecutor())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            return retrofit.create(APIService::class.java)
        }
    }
}