package com.example.solarenergy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()


    }

    override fun onStart() {
        super.onStart()

        btn_Login_login.setOnClickListener {

            if (TextUtils.isEmpty(et_Login_User_name.text)) {
                et_email.error = "Please enter your email"
                et_email.requestFocus()
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(et_Login_Password.text)) {
                et_pwd.error = "Enter a password"
                et_pwd.requestFocus()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(et_Login_User_name.text.toString(),et_Login_Password.text.toString()).addOnCompleteListener {

                if (it.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(applicationContext,it.exception?.message + "Login Failed!",Toast.LENGTH_LONG).show()
                }
            }

        }

        btn_Sign_up.setOnClickListener {
            var intent = Intent(this,RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
