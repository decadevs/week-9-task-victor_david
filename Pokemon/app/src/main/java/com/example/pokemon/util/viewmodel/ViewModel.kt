package com.example.pokemon.util.viewmodel

//import PokemonInDetail
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pokemon.model.IndividualDetails
import com.example.pokemon.model.Pokemon
import com.example.pokemon.requests.Service
import com.example.pokemon.requests.responses.PokemonItemResponse
import com.example.pokemon.requests.responses.Results
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.text.StringBuilder

class MyViewModel:ViewModel() {

    /**Variables needed on a global scope*/
    var details = MutableLiveData<IndividualDetails>()
    var list =  MutableLiveData<MutableList<Results>>()
    val pokemonList: LiveData<MutableList<Results>>
        get() = list
    var next:String? = null
    var failure = MutableLiveData<Boolean>()





    /**Function to get pokemon results from the api*/
    fun  getPokemon(){

        /**Use the service to call the api and work with the response*/
        Service.getApi().getPokemons().enqueue(
            object: Callback<PokemonItemResponse> {
                override fun onResponse(
                    call: Call<PokemonItemResponse>,
                    response: Response<PokemonItemResponse>
                ) {
                    next = response.body()?.next
                    val lists = response.body()?.results
                    list.value = lists?.toMutableList()

                }

                override fun onFailure(call: Call<PokemonItemResponse>, t: Throwable) {
                    failure.value = true
                }
            }
        )
    }

    /**Function to get a pokemon details from the api*/
    fun getPokemonDetails(id:String){

        /**Use the service to call the api and work with the response*/
            Service.getApi().getPokemonDetails(id).enqueue(
                object : Callback<Pokemon> {
                    override fun onResponse(
                        call: Call<Pokemon>,
                        response: Response<Pokemon>
                    ) {
                        /**Set the abilities into a string*/
                        val ability = StringBuilder()
                        for (i in response.body()?.abilities!!.indices){
                            ability.append("${response.body()?.abilities!![i].ability.name}\n")
                        }

                        /**Set the moves into a string*/
                        val moves = StringBuilder()
                        for (i in response.body()?.moves!!.indices){
                            moves.append("${response.body()?.moves!![i].move.name} \n")
                        }

                        /**Set the height and weight to variable strings  */
                        val height = " ${response.body()?.height.toString()}\n"
                        val weight = " ${response.body()?.weight.toString()}\n"

                        /**With the variables create an individual details object and update live data*/
                        details.value = IndividualDetails(height,weight,ability.toString(),moves.toString())

                    }

                    override fun onFailure(call: Call<Pokemon>, t: Throwable) {
                        failure.value = true
                    }
                }
            )
    }

}