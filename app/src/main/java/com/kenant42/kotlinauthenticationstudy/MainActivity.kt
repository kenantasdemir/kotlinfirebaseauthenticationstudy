package com.kenant42.kotlinauthenticationstudy

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kenant42.kotlinauthenticationstudy.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    var callbackManager = CallbackManager.Factory.create()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)



        findViewById<Button>(R.id.signInWithFacebook).setOnClickListener {


            LoginManager.getInstance().logInWithReadPermissions(this, listOf("email", "public_profile","email"))
            LoginManager.getInstance().registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {


                    override fun onCancel() {
                        Toast.makeText(
                            this@MainActivity,
                            "login with Facebook cancelled",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onError(error: FacebookException) {
                        Toast.makeText(
                            this@MainActivity,
                            "an error occured while login with Facebook ${error?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    override fun onSuccess(result: LoginResult) {

                        if (result.accessToken.token != null) {
                            val credential = FacebookAuthProvider.getCredential(result.accessToken.token)
                            auth.signInWithCredential(credential)
                                .addOnCompleteListener{ task ->
                                    if (task.isSuccessful) {
                                        // Giriş başarılı
                                        val user = auth.currentUser
                                        Toast.makeText(this@MainActivity, "Welcome, ${user?.displayName} ${user?.email}", Toast.LENGTH_SHORT).show()
                                        val intent = Intent(this@MainActivity,SignInActivity::class.java)
                                        intent.putExtra("name",user!!.displayName)
                                        intent.putExtra("email",user!!.email)
                                        intent.putExtra("emailVerified",user!!.isEmailVerified)
                                        startActivity(intent)
                                    } else {
                                        Toast.makeText(this@MainActivity, "an error occured while login with Facebook ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                        } else {
                            Toast.makeText(this@MainActivity, "Could not get Facebook token", Toast.LENGTH_SHORT).show()
                        }
                    }


                })
        }

        findViewById<Button>(R.id.signInButton).setOnClickListener {
            googleSignIn()
        }

        binding.buttonEmailSignIn.setOnClickListener {
            val intent = Intent(this, EmailPasswordAuthenticationStudy::class.java)
            startActivity(intent)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)

    }

    private fun googleSignIn() {
        val signInClient = googleSignInClient.signInIntent
        launcher.launch(signInClient)
    }


    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                manageResults(task)
            }
        }

    private fun manageResults(task: Task<GoogleSignInAccount>) {
        val account: GoogleSignInAccount? = task.result
        if (account != null) {
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            auth.signInWithCredential(credential).addOnCompleteListener {
                if (task.isSuccessful) {


                    Toast.makeText(this, "ACCOUNT CREATED ", Toast.LENGTH_LONG).show()
                    verifyUser()
                } else {
                    Toast.makeText(this, task.exception.toString(), Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun verifyUser() {
        val user = Firebase.auth.currentUser
        user?.let {
            val intent = Intent(this, SignInActivity::class.java)
            intent.putExtra("name", it.displayName)
            intent.putExtra("email", it.email)
            intent.putExtra("emailVerified", it.isEmailVerified)

            startActivity(intent)

        }
    }

}