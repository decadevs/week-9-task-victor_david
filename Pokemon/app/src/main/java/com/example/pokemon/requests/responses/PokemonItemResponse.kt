package com.example.pokemon.requests.responses

import java.io.Serializable

data class PokemonItemResponse(
    var count:Int,
    var next:String?,
    var previous:String?,
    var results:Array<Results>
)

data class Results(
    var name:String,
    var url:String
):Serializable