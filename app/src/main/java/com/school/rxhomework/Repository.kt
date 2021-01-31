package com.school.rxhomework

import io.reactivex.rxjava3.core.Observable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

object Repository {

    //fun getPosts(callBack: Callback<List<MainActivity.Adapter.Item>>) = NetworkSource.getPosts(callBack)
    fun getPosts(): Observable<List<MainActivity.Adapter.Item>> = NetworkSource.getPosts()

    object NetworkSource {
        private interface IPostApi {
            @GET("/posts")
            fun getPosts(): Observable<List<MainActivity.Adapter.Item>>
        }

        private val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()

        private val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()

        private val postApi = retrofit.create(IPostApi::class.java)

        //fun getPosts(callBack: Callback<List<MainActivity.Adapter.Item>>) = postApi.getPosts().enqueue(callBack)
        fun getPosts(): Observable<List<MainActivity.Adapter.Item>> = postApi.getPosts()
    }
}
