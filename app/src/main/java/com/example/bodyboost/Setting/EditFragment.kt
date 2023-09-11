package com.example.bodyboost.Setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.bodyboost.R
import com.example.bodyboost.UserSingleton

class EditFragment : Fragment()  {
    private lateinit var username: TextView
    private lateinit var mail:TextView
    private lateinit var changepwdButton:Button
    private lateinit var changebmiButton:Button
    private val currentUser = UserSingleton.user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        username = view.findViewById<TextView>(R.id.username)
        mail = view.findViewById<TextView>(R.id.mail)

        if(currentUser!=null){
            username.text = currentUser.account
            mail.text = currentUser.email
        }
        changepwdButton = view.findViewById<Button>(R.id.pwd)
        changepwdButton.setOnClickListener {
            if(currentUser!=null){

            }
        }
        changebmiButton = view.findViewById<Button>(R.id.bmi)
        changebmiButton.setOnClickListener {

        }
        return view
    }
}