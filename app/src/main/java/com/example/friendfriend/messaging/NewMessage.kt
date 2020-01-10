package com.example.friendfriend.messaging

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.friendfriend.R
import com.example.friendfriend.User
import com.example.friendfriend.addFriend.Friend
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row.view.*

class NewMessage : AppCompatActivity() {

    val db= FirebaseFirestore.getInstance()
    val friends= ArrayList<Friend>()
    lateinit var selfName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)
        supportActionBar?.title = "Select Friend"
        val adapter = GroupAdapter<ViewHolder>()
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//        adapter.add(UserItem())
//
//        recycleViewMsg.adapter = adapter
        getCurrentUser()
        retrieveFriends(selfName)
        friends.forEach {
            adapter.add()
        }
    }
    companion object{
        val USER_KEY = "USER_KEY"
    }
    private fun getCurrentUser(){
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {

            selfName = user.email.toString()

        }
    }
    fun retrieveFriends(email:String){
        db.collection("New").document(email)
            .get()
            .addOnSuccessListener {
                    document->
                if (document!=null&&document.exists()){
                    //  Toast.makeText(applicationContext,document.id,Toast.LENGTH_SHORT).show()
                    Log.d("exist","Document data:${document.data}")
                    friends.add(
                        Friend(
                            document.get("Name").toString(), document.get("Address").toString()
                            , document.get("Email").toString(), document.get("Image").toString()
                        )
                    )
                }
            }
            .addOnFailureListener{
                    exception ->
                Log.d("Error DB","Fail",exception)
            }
    }
//    private fun retrieveFriend(){
//        val ref = FirebaseDatabase.getInstance().getReference("/users")
//        ref.addListenerForSingleValueEvent(object: ValueEventListener{
//            override fun onDataChange(p0: DataSnapshot) {
//                val adapter = GroupAdapter<GroupieViewHolder>()
//                p0.children.forEach{
//                    val user = it.getValue(User::class.java)
//                    if(user!=null)
//                    adapter.add(UserItem(user))
//                }
//                adapter.setOnItemClickListener{ item, view ->
//                    val userItem = item as UserItem
//                    val intent = Intent(view.context, ChatLog::class.java)
//                   // intent.putExtra(USER_KEY, item.user.username)
//                    intent.putExtra(USER_KEY, userItem.user)
//                    startActivity(intent)
//                }
//                recycleViewMsg.adapter = adapter
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
//    }
}

class UserItem(val user: User): Item<GroupieViewHolder>(){
    override fun bind(GroupieViewHolder: GroupieViewHolder, position: Int) {
        GroupieViewHolder.itemView.username_tv.text = user.username
        Picasso.get().load(user.profileImageUrl).into(GroupieViewHolder.itemView.imageView_f)
    }

    override fun getLayout(): Int {
        return R.layout.user_row
    }
}

