package com.example.whereyouat.ui


import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.whereyouat.R
import com.example.whereyouat.model.Friend
import com.example.whereyouat.viewmodel.MyViewModel
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment(R.layout.fragment_sign_up) {



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val viewModel = ViewModelProvider(this).get(MyViewModel::class.java)


        /**Set button to go submit credentials typed in signing up*/
        btnSignUp.setOnClickListener {
            val name = etName.text.toString()
            val email = etEmail.text.toString()
            val phone = etPhoneNumber.text.toString()

            /**Verify Inputs are not empty*/
            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()){
                Toast.makeText(context,"Fields cannot be empty",Toast.LENGTH_LONG).show()
            }else{
                viewModel.AddUser(Friend(name))

                val sharedPref = this.activity?.getSharedPreferences("USER", Context.MODE_PRIVATE)
                val editor = sharedPref?.edit()


                editor?.putString("UserName", name)?.apply()
                editor?.putBoolean("LoggedIn",true)?.apply()




                //Toast.makeText(context,"Logged In As $name",Toast.LENGTH_LONG).show()

                fragmentManager?.beginTransaction()?.apply {
                    replace(R.id.fvFrame,FindFriendFragment())
                    commit()
                }
                fragmentManager?.popBackStack()
            }

        }

        /**Set button to go to the log in fragment*/
        btnToLogInPage.setOnClickListener {
            fragmentManager?.beginTransaction()?.apply {
                replace(R.id.fvFrame,LogInFragment())
                addToBackStack(null)
                commit()
            }
        }

    }


}