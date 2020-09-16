package com.example.whereyouat.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.example.whereyouat.R
import kotlinx.android.synthetic.main.fragment_find_friend.*


class FindFriendFragment : Fragment(R.layout.fragment_find_friend) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val sharedPref = this.activity?.getSharedPreferences("USER", Context.MODE_PRIVATE)

        val name = sharedPref?.getString("UserName","null")

        /**Toast message saying user logged in*/
        Toast.makeText(context,"Logged In As $name",Toast.LENGTH_SHORT).show()

        /**Set click listener to find friend*/
        btnFindFriend.setOnClickListener {
            val friendName = friendsName.text.toString()

            /**Make sure friendName isn't an empty string*/
            if (friendName.isNotEmpty()){

                Intent(context,MapsActivity::class.java).also {
                it.putExtra("USERNAME",name)
                it.putExtra("FRIEND_NAME",friendName)
                startActivity(it)
                }
                friendsName.text.clear()
            }else{
                /**Toast showing message on a blank input*/
                Toast.makeText(context,"Friend's name cannot be empty",Toast.LENGTH_LONG).show()
            }
        }
    }

    /**Set up the menu*/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.option,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    /**Actions taken when option is selected*/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.logOut){
            /**Log user out*/
            val sharedPref = this.activity?.getSharedPreferences("USER", Context.MODE_PRIVATE)
            val editor = sharedPref?.edit()
            editor?.putString("UserName", null)?.apply()
            editor?.putBoolean("LoggedIn",false)?.apply()

            /**Return to sign up fragment*/
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fvFrame,SignUpFragment())
                addToBackStack(null)
                commit()
            }
        }

        return true
    }

}