package com.example.friendfriend.schedule

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.new_event.*
import java.text.SimpleDateFormat
import android.os.Build
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.time.LocalDate
import com.example.friendfriend.R


class NewEvent : AppCompatActivity() {

    lateinit var db: DocumentReference

    var checkOneDay = true

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_event)

        db = FirebaseFirestore.getInstance().document("")

        button_save.setOnClickListener { store()}//Call Save Event Function

        switch_day.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                end_date.isEnabled = false
                end_date.visibility = View.INVISIBLE
                text_until.visibility = View.INVISIBLE
                checkOneDay = false

            } else {
                end_date.isEnabled = true
                end_date.visibility = View.VISIBLE
                text_until.visibility = View.VISIBLE
                checkOneDay = true
            }
        }
    }

    //Save Event Function
    @RequiresApi(Build.VERSION_CODES.O)
    private fun store() {
        val titleT = findViewById<View>(R.id.input_add_title) as EditText
        val descriptionT = findViewById<View>(R.id.input_add_description) as EditText
        val startDateT = findViewById<View>(R.id.start_date) as TextView
        val endDateT = findViewById<View>(R.id.end_date) as TextView
        val startTimeT = findViewById<View>(R.id.start_time) as TextView
        val endTimeT = findViewById<View>(R.id.end_time) as TextView
        val venueT = findViewById<View>(R.id.input_venue) as EditText

        val title = titleT.text.toString().trim()
        val description = descriptionT.text.toString().trim()
        val startDate = startDateT.text.toString().trim()
        val endDate = endDateT.text.toString().trim()
        val startTime = startTimeT.text.toString().trim()
        val endTime = endTimeT.text.toString().trim()
        val venue = venueT.text.toString().trim()

        val current = LocalDateTime.now()
        val format = DateTimeFormatter.ofPattern("d-M-yyyy")
        val formatTime = DateTimeFormatter.ofPattern("HH:mm")
        val getCurrentDate = current.format(format)
        val getCurrentTime = current.format(formatTime)

        var checkDate: Boolean
        var checkTime: Boolean
        var checkTimeCurrent: Boolean
        var checkDT:Boolean

        if(checkOneDay){
            if(title.isNotEmpty() && description.isNotEmpty() && startDate.isNotEmpty() && endDate.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty() && venue.isNotEmpty()){
                checkDate = getCurrentDate.compareTo(startDate) != 1
                checkTime = startTime <= endTime
                checkTimeCurrent = startTime > getCurrentTime
                checkDT = if(!checkDate || !checkTime || !checkTimeCurrent){
                    Toast.makeText(this, "An event must be start from after now", Toast.LENGTH_LONG).show()
                    false
                }else if(!checkDate){
                    Toast.makeText(this, "End date must after start date", Toast.LENGTH_LONG).show()
                    false
                } else if(!checkTime){
                    Toast.makeText(this, "End time must after start time", Toast.LENGTH_LONG).show()
                    false
                }else{
                    true
                }

                if(checkDT) {
                    try {
                        var start = LocalDate.parse(startDate,format)
                        val end = LocalDate.parse(endDate,format)
                        while (!start.isAfter(end)) {
                            val items = HashMap<String, Any>()
                            items["Title"] = title
                            items["Description"] = description
                            items["Date"] = start.format(format)
                            items["StartTime"] = startTime
                            items["EndTime"] = endTime
                            items["Venue"] = venue

                            db.collection(start.format(format)).document("$startTime - $endTime: $title")
                                .set(items)
                                .addOnSuccessListener {
                                    Toast.makeText(this, "New event added. ", Toast.LENGTH_LONG).show()
                                }.addOnFailureListener() { exception: java.lang.Exception ->
                                    Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
                                }

                            start = start.plusDays(1)
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                    }


                    //val intent = Intent(this, ScheduleMain::class.java)
                    //startActivity(intent)
                }
            }else{
                Toast.makeText(this, "Please fill up the fields. ",Toast.LENGTH_LONG).show()
            }
        }else{
            if(title.isNotEmpty() && description.isNotEmpty() && startDate.isNotEmpty() && startTime.isNotEmpty() && endTime.isNotEmpty() && venue.isNotEmpty()){
                checkDate = getCurrentDate.compareTo(startDate) != 1
                checkTime = startTime <= endTime
                checkTimeCurrent = startTime > getCurrentTime
                checkDT = if(!checkDate || !checkTime || !checkTimeCurrent){
                    Toast.makeText(this, "An event must be start from after now", Toast.LENGTH_LONG).show()
                    false
                }else if(!checkDate){
                    Toast.makeText(this, "End date must after start date", Toast.LENGTH_LONG).show()
                    false
                } else if(!checkTime){
                    Toast.makeText(this, "End time must after start time", Toast.LENGTH_LONG).show()
                    false
                }else{
                    true
                }

                if(checkDT) {
                    try {
                        val items = HashMap<String, Any>()
                        items["Title"] = title
                        items["Description"] = description
                        items["Date"] = startDate
                        items["StartTime"] = startTime
                        items["EndTime"] = endTime
                        items["Venue"] = venue

                        db.collection(startDate).document("$startTime - $endTime: $title")
                            .set(items)
                            .addOnSuccessListener {
                                Toast.makeText(this, "New event added. ", Toast.LENGTH_LONG).show()
                            }.addOnFailureListener { exception: java.lang.Exception ->
                                Toast.makeText(this, exception.toString(), Toast.LENGTH_LONG).show()
                            }
                    } catch (e: Exception) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
                    }


                    //val intent = Intent(this, ScheduleMain::class.java)
                    //startActivity(intent)
                }
            }else{
                Toast.makeText(this, "Please fill up the fields. ",Toast.LENGTH_LONG).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickStartDate(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in Toast
            start_date.text = "$dayOfMonth-${monthOfYear + 1}-$year"
        }, year, month, day)
        dpd.show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickEndDate(view: View) {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            // Display Selected date in Toast
            end_date.text = "$dayOfMonth-${monthOfYear + 1}-$year"
        }, year, month, day)
        dpd.show()

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickStartTime(view: View) {
        val c = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)
            start_time.text = SimpleDateFormat("HH:mm").format(c.time)
        }
        TimePickerDialog(this, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun clickEndTime(view: View) {
        val c = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            c.set(Calendar.HOUR_OF_DAY, hour)
            c.set(Calendar.MINUTE, minute)
            end_time.text = SimpleDateFormat("HH:mm").format(c.time)
        }
        TimePickerDialog(this, timeSetListener, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true).show()
    }
}
