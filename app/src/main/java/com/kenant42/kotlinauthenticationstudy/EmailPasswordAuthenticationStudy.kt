package com.kenant42.kotlinauthenticationstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.kenant42.kotlinauthenticationstudy.databinding.ActivityEmailPasswordAuthenticationStudyBinding
import com.kenant42.kotlinauthenticationstudy.databinding.ActivityMainBinding
import java.security.InvalidParameterException

class EmailPasswordAuthenticationStudy : AppCompatActivity() {
    private lateinit var binding: ActivityEmailPasswordAuthenticationStudyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailPasswordAuthenticationStudyBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val auth = FirebaseAuth.getInstance()


        binding.buttonRegister.setOnClickListener {
            var username = binding.editTextUsername.text.toString().trim()
            var password = binding.editTextTextPassword.text.toString().trim()


            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password are mandatory ", Toast.LENGTH_SHORT).show()
            }
            auth.createUserWithEmailAndPassword(username, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "REGISTER SUCCESSFULL ", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "REGISTER FAILURE ", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.buttonLoginWithEmail.setOnClickListener {
            var username = binding.editTextUsername.text.toString().trim()
            var password = binding.editTextTextPassword.text.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                Toast.makeText(this, "Please enter a valid formatted email ", Toast.LENGTH_LONG)
                    .show()
            } else {
                try {
                    auth.signInWithEmailAndPassword(username, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "LOGIN SUCCESSFULL ", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, SignInActivity::class.java)
                                intent.putExtra("email", auth.currentUser!!.email)
                                startActivity(intent)
                            } else {
                                Toast.makeText(this, "LOGIN FAILURE ", Toast.LENGTH_LONG).show()
                            }

                        }
                    Log.e("CURRENT USER EMAIL IS ", "${auth.currentUser!!.email}")
                } catch (e: InvalidParameterException) {

                }
            }


        }

        binding.buttonResetPassword.setOnClickListener {

            val intent = Intent(this,EmailPasswordAuthenticationStudy::class.java)
            startActivity(intent)

        }
    }
}