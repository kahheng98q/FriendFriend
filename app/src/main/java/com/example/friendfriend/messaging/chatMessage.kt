package com.example.friendfriend.messaging

class chatMessage(val id: String, val text: String, val fromID: String, val toID: String, val timestamp: Long){
    constructor():this("","","","",-1)
}