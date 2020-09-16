package com.example.pokemon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pokemon.requests.responses.Results
import com.example.pokemon.ui.Clicker
import com.example.pokemon.ui.PokemonAdapter
import com.example.pokemon.ui.PokemonDetailsActivity
import com.example.pokemon.util.NetWorkConnection
import com.example.pokemon.util.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : AppCompatActivity(),Clicker {

    /**Variables needed on a global scope*/
    private lateinit var viewModel: MyViewModel
    var list = listOf<Results>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var netWorkConnection = NetWorkConnection(this)



        /**Set the recycler view's adapter*/
        val adapter = PokemonAdapter(list,this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        /**Get the view model that would feed the UI*/
        viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        netWorkConnection.observe(this, Observer {
            if (it){
                viewModel.getPokemon()
                initUI()
            }else unexpectedError()

        })

        /**Observe the live data variables as to update the user interface as the data changes*/
        viewModel.pokemonList.observe(this, Observer {
            adapter.setPokemon(it)
        })

        viewModel.failure.observe(this, Observer {
            if(it==true) unexpectedError()
        })

    }

    /**Function to remove views and bring views from and into display upon a failed response */
    private fun unexpectedError(){
        recyclerView.visibility = View.INVISIBLE
        ivErrorImage.visibility = View.VISIBLE
        tvErrorText.visibility = View.VISIBLE
    }


    fun initUI(){
        recyclerView.visibility = View.VISIBLE
        ivErrorImage.visibility = View.INVISIBLE
        tvErrorText.visibility = View.INVISIBLE
    }








    override fun onClickItem(currentPokemon: Results, position: Int) {
        /**Start intent to begin activity of the clicked pokemon's profile display*/
        Intent(this,PokemonDetailsActivity::class.java).also{
            it.putExtra("POKEMON",currentPokemon as Serializable)
            startActivity(it)
        }

    }
}