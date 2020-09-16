package com.example.pokemon.requests

//import PokemonInDetail
import com.example.pokemon.model.Pokemon
import com.example.pokemon.requests.responses.PokemonItemResponse
import retrofit2.Call

import retrofit2.http.GET
import retrofit2.http.Path



interface PokemonApi{

    @GET("pokemon/")
    fun getPokemons(): Call<PokemonItemResponse>

    @GET("pokemon/{id}")
    fun getPokemonDetails(@Path("id")key:String):Call<Pokemon>

    @GET
    fun loadMore():Call<PokemonItemResponse>
}