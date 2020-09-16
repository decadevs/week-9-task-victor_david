package com.example.pokemon.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pokemon.R
import com.example.pokemon.requests.responses.Results
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pokemon_item.view.*

class PokemonAdapter(var list: List<Results>,var clicker: Clicker):RecyclerView.Adapter<PokemonAdapter.PokemonViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_item,parent,false)
        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        val currentPokemon = list[position]
        holder.initialize(currentPokemon,clicker)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class PokemonViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var nameView = itemView.pokemonName
        var imageView = itemView.pokemonImage

        fun initialize(currentPokemon:Results,clicker: Clicker){
            val url = currentPokemon.url.split("/")
            val id = url[url.size-2]
            nameView.text = currentPokemon.name

            Picasso.get()
                .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png")
                .into(imageView)


            itemView.setOnClickListener{
                clicker.onClickItem(currentPokemon,adapterPosition)
            }
        }
    }




    fun setPokemon(results:List<Results>){
        this.list = results
        notifyDataSetChanged()
    }




}

interface Clicker{
    fun onClickItem(currentPokemon: Results, position:Int)
}