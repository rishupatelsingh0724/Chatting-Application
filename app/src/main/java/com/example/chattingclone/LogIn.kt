package com.example.chattingclone

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chattingclone.databinding.ActivityLogInBinding
import com.google.firebase.auth.FirebaseAuth

class LogIn : AppCompatActivity() {
    lateinit var binding: ActivityLogInBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLogInBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.signUpTextView.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }
        auth=FirebaseAuth.getInstance()
        binding.loginButton.setOnClickListener {
          val email=  binding.edtEmail.text.toString()
           val password= binding.edtPassword.text.toString()
            logIn(email,password)
        }
    }
    fun logIn(email:String,password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                startActivity(Intent(this,MainActivity::class.java))
                Toast.makeText(this, "Login success", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()
            }
    }
}