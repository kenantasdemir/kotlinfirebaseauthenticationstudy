package com.kenant42.kotlinauthenticationstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.kenant42.kotlinauthenticationstudy.databinding.ActivityMainBinding
import com.kenant42.kotlinauthenticationstudy.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val emailVerified = intent.getBooleanExtra("emailVerified",false)


        binding.textViewName.text = "HELLO ${name.toString()}"
        binding.textViewEmail.text = "EMAIL  ${email.toString()}"
        binding.textViewIsEmailVerified.text = "IS EMAIL VERIFIED? ${emailVerified.toString()}"




        binding.buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this,"SIGNED OUT SUCCESSFULLY ",Toast.LENGTH_LONG).show()
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }



    }
}