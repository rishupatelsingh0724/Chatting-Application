package com.example.chattingclone

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.chattingclone.databinding.ActivityLogInBinding
import com.example.chattingclone.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var dbRef:DatabaseReference
    lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.loginTextView.setOnClickListener {
            startActivity(Intent(this, LogIn::class.java))
            finish()
        }
        auth = FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener {
            val name = binding.etdName.text.toString()
            val email = binding.edtEmail.text.toString()
            val password = binding.edtConfirmPassword.text.toString()
            singUp(name, email, password)

        }
    }

    fun singUp(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                addUserToDatabase(name, email, auth.currentUser!!.uid)


                Toast.makeText(this, "Sign in success", Toast.LENGTH_SHORT).show()
//                startActivity(Intent(this, MainActivity::class.java))
//                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {
        dbRef=FirebaseDatabase.getInstance().getReference()
        dbRef.child("user").child(uid).setValue(User(name,email,uid))
    }
}