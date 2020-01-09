package com.example.friendfriend.schedule

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import com.example.friendfriend.R
import kotlinx.android.synthetic.main.event_details.*


class EventDetails : AppCompatActivity() {

    lateinit var db: DocumentReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_details)

        db = FirebaseFirestore.getInstance().document("")

        val intent = intent
        val dateSelected = intent.extras!!.getString("DateSelected")
        val timeSelected = intent.extras!!.getString("TimeSelected")

        db.collection(dateSelected.toString()).document(timeSelected.toString()).get()
            .addOnSuccessListener { document ->
                text_title.text = document.getString("Title")
                text_description.text = document.getString("Description")
                text_date.text = "Date: " + document.getString("StartDate")
                text_time.text = "Time: " + document.getString("StartTime") + " - " + document.getString("EndTime")
                text_venue.text = "Venue: " + document.getString("Venue")

            }
            .addOnFailureListener { exception ->
                Log.d("exist", "", exception)
            }

        button_delete.setOnClickListener { view ->
            db.collection(dateSelected.toString()).document(timeSelected.toString()).delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "Event Deleted. ", Toast.LENGTH_LONG).show()
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "" + exception, Toast.LENGTH_LONG).show()
                }
        }
    }
}