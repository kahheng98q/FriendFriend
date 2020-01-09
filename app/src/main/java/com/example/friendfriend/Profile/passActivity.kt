package com.example.friendfriend.Profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.friendfriend.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_pass.*

class passActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pass)
        val db= FirebaseFirestore.getInstance()
        var name = intent.getStringExtra("name")
        var image = intent.getStringExtra("uri")
        var pass = intent.getStringExtra("pass")

        confirmbtn.setOnClickListener {
            if (passOld.text.toString() == pass.toString()&&passNew.text.toString()==passNew2.text.toString()&&passOld.text.toString()!=null&&passNew.text.toString()!=null) {
                var p =Profile(image,name,passNew.text.toString())
                db.collection("Profile").document("jacky")
                    .set(p)
                    .addOnSuccessListener {

                        startActivity(Intent(this,ProfileActivity::class.java))
                        Toast.makeText(this,"sucess change", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener{
                        Toast.makeText(this,"fail", Toast.LENGTH_SHORT).show()
                    }

            } else {
                Toast.makeText(this, "passw not match", Toast.LENGTH_SHORT).show()
            }

        }
        cancelBtn.setOnClickListener{
            startActivity(Intent(this,ProfileActivity::class.java))
            Toast.makeText(this,"cancel changes", Toast.LENGTH_SHORT).show()
        }

    }
}
