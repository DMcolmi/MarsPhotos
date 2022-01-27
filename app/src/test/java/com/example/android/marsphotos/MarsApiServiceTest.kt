package com.example.android.marsphotos

import BaseTest
import com.example.android.marsphotos.network.MarsApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MarsApiServiceTest: BaseTest() {

    private lateinit var service: MarsApiService

    @Before
    fun setup(){
         //funizione che serve per specificare quale url intercettare e 'cattura' in un oggetto di tipo HttpUrl la risposta mockata
        val url = mockWebServer.url("/")

        //si crea un service concatenando Retrofit e Mochi
        service = Retrofit.Builder()
            .baseUrl(url) //aggiunto in un secondo momento
            .addConverterFactory(
                    MoshiConverterFactory.create(
                    Moshi.Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()
                    )
                )
            .build()
            .create(MarsApiService::class.java)
    }

    @Test
    fun api_service(){
        enqueue("mars_photos.json")

        runBlocking {
            val apiResponse = service.getPhotos()

            assertNotNull(apiResponse)
            assertTrue("The list was empty", apiResponse.isNotEmpty())
            assertEquals("The IDs did not match", "424905", apiResponse[0].id)
        }

    }

}