package com.thiha.android4k.testfirebasevideostreaming.view.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.thiha.android4k.testfirebasevideostreaming.R

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var etPhone: EditText
    private lateinit var etPassword: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etPhone = findViewById(R.id.etPhoneNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btn_login)

    }

    override fun onResume() {
        super.onResume()

        auth = Firebase.auth

        if (auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
        } else {

            btnLogin.setOnClickListener {

                if (auth.currentUser != null) {
                    Toast.makeText(this, "Logged In Already", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    login(etPhone.text.toString(), etPassword.text.toString())
                }

            }

        }
    }

    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Toast.makeText(this@LoginActivity, "Welcome Back", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this@LoginActivity, "Error,No Account", Toast.LENGTH_SHORT).show()
            }
    }
}