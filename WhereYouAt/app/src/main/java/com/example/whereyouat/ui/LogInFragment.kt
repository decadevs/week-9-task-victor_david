package com.example.whereyouat.ui

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.whereyouat.R
import com.example.whereyouat.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.fragment_log_in.*


class LogInFragment : Fragment(R.layout.fragment_log_in) {



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        /**Button to check if user is on the database*/
        btnLogIn.setOnClickListener {
            val userName = etLogInName.text.toString()
            val userEmail = etLogInEmail.text.toString()

            /**Check if fields are not empty*/
            if (userName.isNotEmpty() && userEmail.isNotEmpty()) {
                viewModel.findUser(userName)

                /**Using the livedata stream to determine if the user is found or not*/
                viewModel.found.observe(this, Observer {
                    if (it) {
                        val sharedPref =
                            this.activity?.getSharedPreferences("USER", Context.MODE_PRIVATE)
                        /**Set shared preferences to logged in */
                        val editor = sharedPref?.edit()
                        editor?.putString("UserName", userName)?.apply()
                        editor?.putBoolean("LoggedIn", true)?.apply()

                        fragmentManager?.popBackStack()
                        fragmentManager?.beginTransaction()?.apply {
                            replace(R.id.fvFrame, FindFriendFragment())
                            commit()
                        }

                        /**Error toast message showing that the user isn't in the database*/
                    }else Toast.makeText(context,"User not found",Toast.LENGTH_SHORT).show()
                })

                /**Error toast message saying saying fields can't be blank */
            }else Toast.makeText(context,"Fields cannot be empty",Toast.LENGTH_SHORT).show()


        }

    }
}