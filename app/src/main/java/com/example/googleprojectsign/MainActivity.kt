package com.example.googleprojectsign

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.googleprojectsign.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var googleSignInClient:GoogleSignInClient
    private lateinit var auth:FirebaseAuth
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("598296651591-qd98upj863vveu1tku712n2knni4peuh.apps.googleusercontent.com")
            .requestEmail()
            .build()
        googleSignInClient  =GoogleSignIn.getClient(this,gso)
        binding.btnGoogleWithGoogle.setOnClickListener{
            signIn()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent,RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                fireBaseAuthWithGoogle(account.idToken!!)
            }catch (e:ApiException){

            }
        }
    }
    private fun fireBaseAuthWithGoogle(idToken:String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){task->
                if (task.isSuccessful){
                    val user = auth.currentUser
                    updateUI(user)
                }else{
                    updateUI(null)
                }

            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user!=null){
            val intent = Intent(applicationContext,GoogleSignInActivity::class.java)
            intent.putExtra(EXTRA_NAME,user.displayName)
            startActivity(intent)        }
    }

    companion object{
        const val RC_SIGN_IN = 1001
        const val EXTRA_NAME =  "EXTRA NAME"
    }
}