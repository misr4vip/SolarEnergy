package com.example.solarenergy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.example.solarenergy.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    // Initialize Firebase Auth
    val  auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null)
        {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

        btn_register_signup.setOnClickListener {
    ////   validate user input
            val repwd = et_repwd.text.toString()
            val FirstName = et_fName.text.toString()
            val LastName = et_lName.text.toString()
            val pwd = et_pwd.text.toString()
            val email = et_email.text.toString()

            if (TextUtils.isEmpty(FirstName)) {
                et_fName.error = "Please enter your FirstName"
                et_fName.requestFocus()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(LastName)) {
                et_lName.error = "Please enter your LastName"
                et_lName.requestFocus()
                return@setOnClickListener
            }


            if (TextUtils.isEmpty(email)) {
                et_email.error = "Please enter your email"
                et_email.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                et_email.error = "Enter a valid email"
                et_email.requestFocus()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(pwd)) {
                et_pwd.error = "Enter a password"
                et_pwd.requestFocus()
                return@setOnClickListener
            }

            if (pwd != repwd) {
                et_repwd.error = "PassWord Dismatch"
                return@setOnClickListener
            }
            if (pwd.length < 6) {
                et_pwd.error = "PassWord  is weak"
                return@setOnClickListener
            }

     ////   create user
            auth.createUserWithEmailAndPassword(email, pwd)
                .addOnCompleteListener {
                    if (it.isSuccessful) {

                        Toast.makeText(baseContext, "Authentication success.", Toast.LENGTH_LONG).show()
                        val user = auth.currentUser
                        writeNewUser(user!!.uid,FirstName ,LastName,email)
                        val intent  = Intent(baseContext,MainActivity::class.java)
                        startActivity(intent)

                    } else {

                        Toast.makeText(baseContext, it.exception?.message+"Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }



                }
                .addOnFailureListener {
                    Toast.makeText(baseContext, it.message?.toString()+"Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
        }
    }

     fun writeNewUser(userId: String, fname: String,lname: String, email: String?) {
        val user = User(fname,lname, email)
        database.child("users").child(userId).setValue(user)
    }
}
