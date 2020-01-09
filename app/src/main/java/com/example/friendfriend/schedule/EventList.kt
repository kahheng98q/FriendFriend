package com.example.friendfriend.schedule

import android.app.Activity
import android.view.View
import android.widget.TextView
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.friendfriend.R


class EventList(private val context: Activity, private var time: ArrayList<String>, private var title: ArrayList<String>)
    : ArrayAdapter<String>(context, R.layout.event_list, title) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.event_list, null, true)

        val titleText = rowView.findViewById(R.id.text_title) as TextView
        val subtitleText = rowView.findViewById(R.id.text_description) as TextView

        titleText.text = title[position]
        subtitleText.text = time[position]

        return rowView
    }
}