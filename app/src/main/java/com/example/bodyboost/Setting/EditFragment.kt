package com.example.bodyboost.Setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.exampl.ChangePassword
import com.example.bodyboost.R
import com.example.bodyboost.UserSingleton


class EditFragment : Fragment()  {
    private lateinit var username: TextView
    private lateinit var mail:TextView
    private lateinit var changepwdButton:Button
    private lateinit var changebmiButton:Button
    private lateinit var changepicture:ImageButton
    private lateinit var changeinfo : Button
    private val currentUser = UserSingleton.user
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit, container, false)
        username = view.findViewById(R.id.username)
        mail = view.findViewById(R.id.mail)

        if(currentUser!=null){
            username.text = currentUser.account
            mail.text = currentUser.email
        }
        changepwdButton = view.findViewById(R.id.pwd)
        changepwdButton.setOnClickListener {
            val intent = Intent(requireActivity(), ChangePassword::class.java)
            startActivity(intent)
        }
        changebmiButton = view.findViewById(R.id.bmi)
        changebmiButton.setOnClickListener {

        }
        changepicture = view.findViewById(R.id.picture)
        changepicture.setOnClickListener{

        }
        changeinfo = view.findViewById(R.id.info)
        changeinfo.setOnClickListener {
            val intent = Intent(requireActivity(), EditInfo::class.java)
            startActivity(intent)
        }
        return view
    }
}