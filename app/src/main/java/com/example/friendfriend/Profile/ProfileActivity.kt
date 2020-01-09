package com.example.friendfriend.Profile

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.friendfriend.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class ProfileActivity : AppCompatActivity() {

    var IMAGE_PICK_CODE = 1000
    var CAMERA_REQUEST_CODE = 123
    val db= FirebaseFirestore.getInstance()
    lateinit var imageUri: Uri
    lateinit var name:String
    lateinit var pass:String
    lateinit var p : Profile



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        var image = findViewById<View>(R.id.circularImageView)

//        Toast.makeText(this,"fk",Toast.LENGTH_SHORT).show()
        getProfile()
        Thread.sleep(3000)
        image.setOnClickListener(){
            openFileChooser()
        }
        UpdateBtn.setOnClickListener(){
            update()
        }

        btnChangePass.setOnClickListener {
            passPage()

        }
    }

    private fun passPage() {
        val intent = Intent(this,passActivity::class.java)
//        var Aname = p.name
//        intent.putExtra("a",p.name.toString())
        intent.putExtra("name",p.name.toString())
        intent.putExtra("pass",p.password.toString())
        intent.putExtra("uri",p.imageint.toString())
        startActivity(intent)
    }

    private fun getProfile() {
        db.collection("Profile").document("jacky")
            .get()
            .addOnSuccessListener {

                if (it != null && it.exists()) {
                    name = it.get("name").toString()
                    editTextName.setText(name)
                    imageUri = Uri.parse(it.get("pic").toString())
                    pass = it.get("password").toString()
                    p = Profile(it.get("imageint").toString(),name,pass)

                    try {
                        Picasso.get().load(it.get("imageint").toString()).into(circularImageView)
                    }catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }

            }
            .addOnFailureListener{
                    exception ->
                //                Toast.makeText(MainActivity(),"not",Toast.LENGTH_SHORT).show()
                Log.d("Error DB","Fail to load account",exception)
            }

    }

    private fun update() {
        if (imageUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(imageUri!!)
            .addOnSuccessListener{
                Log.d("UpdateActivity", "Successfully updated: ${it.metadata?.path}")
                ref.downloadUrl.addOnSuccessListener {
                    Log.d("UpdateActivity","File Location: $it")
                    Toast.makeText(this,"yes", Toast.LENGTH_SHORT).show()
                    p = Profile(it.toString(),name,pass)
                    updateUser(it.toString())
                }
            }
            .addOnFailureListener{
                Toast.makeText(this,it.toString(), Toast.LENGTH_SHORT).show()
                Log.d("RegisterActivity", "Failed to upload image")
            }

    }

    private fun updateUser(imageURI: String) {
//        Toast.makeText(this,imageURI.toString(),Toast.LENGTH_SHORT).show()
        db.collection("Profile").document("jacky")
            .set(p)
            .addOnSuccessListener {
                Toast.makeText(this,"sucess", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener{
                Toast.makeText(this,"fail", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openFileChooser() {

//            val builder = AlertDialog.Builder(this)
//            builder.setTitle("Profile pic")
//            builder.setMessage("Choose the method u want")
//            builder.setPositiveButton("take selfie"){dialog, which ->
//                Toast.makeText(this,"smile",Toast.LENGTH_SHORT).show()
//                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//
//                if (intent.resolveActivity(packageManager) != null) {
//                    startActivityForResult(Intent.createChooser(intent,"select photo"), CAMERA_REQUEST_CODE)
//                }
//            }
//            builder.setNegativeButton("gallery"){dialog, which ->
        Toast.makeText(this,"select a photo", Toast.LENGTH_SHORT).show()
        val intent= Intent()
        intent.type="image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent,"select picture"),IMAGE_PICK_CODE)
//            }
//        builder.show()

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK) {

            imageUri = data?.data!!
            try {
                Picasso.get().load(imageUri).into(circularImageView)
//                Toast.makeText(this,imageUri.toString(),Toast.LENGTH_SHORT).show()
            }catch (e: Throwable) {
                e.printStackTrace()
            }
        }else if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            imageUri =data?.data!!
            val extras = data?.getExtras()
            val imageBitMap = extras?.get("data") as Bitmap
            circularImageView.setImageBitmap(imageBitMap)
//            Toast.makeText(this,imageUri.toString(),Toast.LENGTH_SHORT).show()
        }
        else{
            Toast.makeText(this,"fail to upload", Toast.LENGTH_SHORT).show()
        }
    }
}
