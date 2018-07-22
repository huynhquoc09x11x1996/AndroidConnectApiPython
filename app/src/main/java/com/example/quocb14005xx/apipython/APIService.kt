package com.example.quocb14005xx.apipython

import android.telecom.Call
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface APIService {

    @POST("/api/post_some_data")
    fun getPredictLabelPost(@Body body: JsonObject): retrofit2.Call<ResponseBody>


    companion object {
        fun create(): APIService {
            val gson = GsonBuilder().setLenient().create()
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://192.168.1.54:5000/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            return retrofit.create(APIService::class.java)
        }
    }
}