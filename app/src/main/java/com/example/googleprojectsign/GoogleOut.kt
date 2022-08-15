package com.example.googleprojectsign

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.googleprojectsign.databinding.ActivityGoogleSignInBinding
import com.google.firebase.auth.FirebaseAuth

class GoogleOut : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var binding: ActivityGoogleSignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGoogleSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        val email = intent.getStringExtra("email")
        val displayName = intent.getStringExtra("name")
        binding.name.text = email
        binding.displayname.text = displayName
        binding.SignOutBtn.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,MainActivity::class.java))
        }

    }
}