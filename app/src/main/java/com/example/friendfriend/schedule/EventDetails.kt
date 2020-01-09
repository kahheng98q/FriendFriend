package com.example.friendfriend.schedule

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import com.example.friendfriend.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.event_details.*


class EventDetails : AppCompatActivity() {

    lateinit var db: DocumentReference
    private var selfName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_details)

        val user = FirebaseAuth.getInstance().currentUser
        user?.let {

            selfName = user.email.toString()

        }

        db = FirebaseFirestore.getInstance().document("")

        val intent = intent
        val dateSelected = intent.extras!!.getString("DateSelected")
        val timeSelected = intent.extras!!.getString("TimeSelected")

        db.collection(selfName).document(selfName).collection(dateSelected.toString()).document(timeSelected.toString()).get()
            .addOnSuccessListener { document ->
                text_title.text = document.getString("Title")
                text_description.text = document.getString("Description")
                text_date.text = "Date: " + document.getString("Date")
                text_time.text = "Time: " + document.getString("StartTime") + " - " + document.getString("EndTime")
                text_venue.text = "Venue: " + document.getString("Venue")

            }
            .addOnFailureListener { exception ->
                Log.d("exist", "", exception)
            }

        button_delete.setOnClickListener { view ->
            db.collection(selfName).document(selfName).collection(dateSelected.toString()).document(timeSelected.toString()).delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Event Deleted. ", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "" + exception, Toast.LENGTH_LONG).show()
                }
            val intent = Intent(this, ScheduleMain::class.java)
            startActivity(intent)
        }
    }
}