package com.example.friendfriend

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore

import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.register_activity.*
import java.util.*

class RegisterActivity : AppCompatActivity() {
    var note: HashMap<String, Object> = HashMap<String, Object>()
    lateinit var nEmail: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        bt_register.setOnClickListener {
            registration()
        }

        AlreadyHave.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        addImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }
    var ImageUri: Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data!= null){
            ImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver,ImageUri)
            addImageView.setImageBitmap(bitmap)
            // val bitmapDrawable = BitmapDrawable(bitmap)
            //addImage.setBackgroundDrawable(bitmapDrawable)
            addImage.alpha = 0f
        }
    }
    private fun registration(){
        val email = edt_email.text.toString()
        val password = edt_password.text.toString()

        nEmail=edt_email.text.toString().toLowerCase()
        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(baseContext, "Please enter text in Email or Password.", Toast.LENGTH_SHORT).show()
        }
        Log.d("RegisterActivity","Email is: " + email )
        Log.d("RegisterActivity", "Password is: "+ password)

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                note.put("Email", nEmail as Object)

                uploadImage()
            }
            .addOnFailureListener{
                // If sign in fails, display a message to the user.
                Log.d("RegisterActivity", "Error while creating user: ${it.message}")
            }
    }
    private fun uploadImage(){
        if (ImageUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(ImageUri!!)
            .addOnSuccessListener{
                Log.d("RegisterActivity", "Successfully uploaded: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity","File Location: $it")
                    note.put("Image", ImageUri.toString() as Object)
                    saveUser(it.toString())

                }
            }
            .addOnFailureListener{
                Log.d("RegisterActivity", "Failed to upload image")
            }
    }
    private fun saveUser(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, edt_username.text.toString(), profileImageUrl)
        val un =edt_username.text.toString()
        note.put("Name", edt_username.text.toString() as Object)

        Log.d("RegisterActivity","$uid")
        Log.d("RegisterActivity","$un")
        Log.d("RegisterActivity","$profileImageUrl")


        ref.setValue(user)
            .addOnSuccessListener{
                Log.d("RegisterActivity","Done saved")

                storeToFirestore()


            }.addOnFailureListener{
                Log.d("RegisterActivity", "Failed to upload user")
            }


    }
    fun storeToFirestore(){
        val db = FirebaseFirestore.getInstance()
        db.collection("New").document(nEmail)
            .set(note)
            .addOnSuccessListener {
                Toast.makeText(applicationContext, "Done", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }.addOnFailureListener{
                Log.d("RegisterActivity", "Failed to upload user")
            }
    }

}
@Parcelize
class User(val uid:String,val username: String, val profileImageUrl: String): Parcelable{
    constructor(): this("","","")
}
