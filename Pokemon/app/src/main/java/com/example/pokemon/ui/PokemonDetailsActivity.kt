package com.example.pokemon.ui

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.pokemon.R
import com.example.pokemon.requests.responses.Results
import com.example.pokemon.util.NetWorkConnection
import com.example.pokemon.util.viewmodel.MyViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pokemon_details.*

class PokemonDetailsActivity : AppCompatActivity() {

    /**Variables needed on a global scope*/
    private lateinit var viewModel:MyViewModel

    var inView:Boolean = true


    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_details)


        var netWorkConnection = NetWorkConnection(this)


        /**Get pokemon from serializable extra*/
        val pokemon = intent.getSerializableExtra("POKEMON") as Results
        /**Set Pokemon name on text view*/
        tvPokemonName.text = pokemon.name.toUpperCase()


        /**Get id from the pokemon's url*/
        val url = pokemon.url.split("/")
         id = url[url.size-2]

        /**Get view model to supply data*/
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)



        netWorkConnection.observe(this,Observer{
            if (it){
                    viewModel.getPokemonDetails(id)
                    initUI()
                }else{
                    unexpectedError()
                }

        })

        /**Function call to get the pokemon's details based on id*/
        //viewModel.getPokemonDetails(id)

        /**Observe the live data variables as to update the user interface as the data changes*/
        viewModel.details.observe(this, Observer {
            tvAbility.text = it.abilities
            tvHeight.text = it.height
            tvMoves.text = it.moves
            tvWeight.text = it.weight
        })

        viewModel.failure.observe(this, Observer {
            unexpectedError()
        })

        /**Fetch and display pokemon profile picture*/
        Picasso.get().load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png")
            .into(ivPokemonProfilePicture)


    }

    /**Function to remove views and bring views from and into display upon a failed response */
    private fun unexpectedError(){
        tvErrorProfile.visibility= View.VISIBLE
        ivErrorFetchingProfile.visibility = View.VISIBLE

        ivPokemonProfilePicture.visibility = View.GONE
        tvPokemonName.visibility = View.GONE
        heightHeader.visibility = View.GONE
        weightHeader.visibility = View.GONE
        abilityHeader.visibility = View.GONE
        movesHeader.visibility = View.GONE
        tvWeight.visibility = View.GONE
        tvHeight.visibility = View.GONE
        tvMoves.visibility = View.GONE
        tvAbility.visibility = View.GONE

    }

    private fun initUI(){
        tvErrorProfile.visibility= View.GONE
        ivErrorFetchingProfile.visibility = View.GONE

        ivPokemonProfilePicture.visibility = View.VISIBLE
        tvPokemonName.visibility = View.VISIBLE
        heightHeader.visibility = View.VISIBLE
        weightHeader.visibility = View.VISIBLE
        abilityHeader.visibility = View.VISIBLE
        movesHeader.visibility = View.VISIBLE
        tvWeight.visibility = View.VISIBLE
        tvHeight.visibility = View.VISIBLE
        tvMoves.visibility = View.VISIBLE
        tvAbility.visibility = View.VISIBLE
    }


    override fun onDestroy() {
        super.onDestroy()
        inView=false
    }
}