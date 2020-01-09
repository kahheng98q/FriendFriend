package com.example.friendfriend.schedule

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.schedule_main.*
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.friendfriend.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.content_main.*
import kotlin.collections.ArrayList
import kotlin.time.ExperimentalTime


class ScheduleMain : AppCompatActivity() {
    lateinit var eventClass: MutableList<NewEvent>
    lateinit var db: DocumentReference

    private var time = ArrayList<String>()
    private var title = ArrayList<String>()

    @ExperimentalTime
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.schedule_main)
        setSupportActionBar(toolbar)

        eventClass = mutableListOf()

        db = FirebaseFirestore.getInstance().document("")

        //db.keptSync(true)
        fab.setOnClickListener {
            /*val current = LocalDateTime.now()
            val formatTime = DateTimeFormatter.ofPattern("H:mm")
            val getCurrentDate = current.format(formatTime)

            db.collection("9-1-2020").get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        val before30 = LocalTime.parse(document.get("StartTime").toString(),formatTime)
                        val before = before30.minusMinutes(30)

                        if(getCurrentDate >= before.format(formatTime) && getCurrentDate < document.get("StartTime").toString()){
                            issueNotification()
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("exist", "", exception)
                }*/


            val intent = Intent(this, NewEvent::class.java)
            startActivity(intent)
        }

        var date = ""

        //Calendar Action
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView?.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val msg = "" + dayOfMonth + "-" + (month + 1) + "-" + year
            date = msg

            title.clear()
            time.clear()

            db.collection(msg).get()
                .addOnSuccessListener { documents ->
                    for(document in documents) {
                        title.add(document.get("Title").toString())
                        time.add(document.get("StartTime").toString() + " - " + document.get("EndTime").toString())
                    }
                    val myListAdapter = EventList(this,time,title)
                    list_view.adapter = myListAdapter
                }
                .addOnFailureListener { exception ->
                    Log.d("exist", "", exception)
                }
        }

        //List Action
        list_view.setOnItemClickListener(){adapterView, view, position, id ->
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)

            val intent = Intent(this, EventDetails::class.java)
            intent.putExtra("DateSelected", date)
            intent.putExtra("TimeSelected", time[itemIdAtPos.toInt()] +  ": " + title[itemIdAtPos.toInt()])
            startActivity(intent)
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun makeNotificationChannel(id: String, name: String, importance: Int) {
        val channel = NotificationChannel(id, name, importance)
        channel.setShowBadge(true) // set false to disable badges, Oreo exclusive

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    private fun issueNotification() {

        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL_1", "Example channel", NotificationManager.IMPORTANCE_DEFAULT)
        }
        // the check ensures that the channel will only be made
        // if the device is running Android 8+

        val notification = NotificationCompat.Builder(this, "CHANNEL_1")
        // the second parameter is the channel id.
        // it should be the same as passed to the makeNotificationChannel() method

        notification
            .setSmallIcon(R.mipmap.ic_launcher) // can use any other icon
            .setContentTitle("Notification!")
            .setContentText("Don't forget you have event later.")
            .setNumber(3) // this shows a number in the notification dots

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(1, notification.build())
        // it is better to not use 0 as notification id, so used 1.
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
