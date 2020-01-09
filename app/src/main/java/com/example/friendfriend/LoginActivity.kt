package com.example.friendfriend
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity: AppCompatActivity(){
    lateinit var email :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        bt_login.setOnClickListener{
             email = edt_emails.text.toString()
             var password = edt_LoginPassword.text.toString()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email!!,password)
                .addOnCompleteListener {
                    if(!it.isSuccessful)return@addOnCompleteListener
                         login()
                     }
                .addOnFailureListener {
                    Toast.makeText(this, "Please re-enter your ID and Password", Toast.LENGTH_SHORT).show()
                }
        }
        DontHave.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun login(){
        val intents = Intent(this, MainActivity::class.java)

//        intent.putExtra("Email",email)

        intents.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intents)
    }

}