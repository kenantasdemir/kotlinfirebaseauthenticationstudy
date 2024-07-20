package com.kenant42.kotlinauthenticationstudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.kenant42.kotlinauthenticationstudy.databinding.ActivityForgotPasswordBinding
import java.net.PasswordAuthentication

class ForgotPassword : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val auth = FirebaseAuth.getInstance()


        binding.sendPasswordResetEmailBtn.setOnClickListener {
            var username = binding.editTextEmail.text.toString().trim()
            if (!Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                Toast.makeText(
                    applicationContext,
                    "Please enter a valid formatted email ",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                auth.sendPasswordResetEmail(binding.editTextEmail.text.toString().trim())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "EMAIL SENT ", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            try {
                                throw task.exception!!
                            } catch (e: FirebaseAuthInvalidUserException) {
                                println("could not find an user that mathches with that email ")
                            } catch (e: Exception) {
                                println("An error occured while sending password reset email: ${e.message}")
                            }
                        }
                    }
            }
        }

    }
}