package com.example.pokemon.requests

import android.util.Log
import com.example.pokemon.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Service {

    companion object{
        /**Create a retrofit builder with the gson converter */
        private val retrofitBuilder = Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(
            GsonConverterFactory.create())
        /**Build the retrofit builder*/
        private val retrofit = retrofitBuilder.build()

        /**Create an instance of the api service*/
        private val service = retrofit.create(PokemonApi::class.java)

        /**Return the api*/
        fun getApi():PokemonApi{
            return service
        }

    }


}