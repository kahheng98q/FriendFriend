package com.example.friendfriend.caption;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.friendfriend.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class ExampleCaption extends AppCompatDialogFragment {
//    private EditText editCaption;
    private TextView txt;
    private boolean result=false;
    private Context cnn;
    private String input;
    private int count ;


    private DatabaseReference mDatabase;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


//        EditText capIn = findViewById<EditText>(R.id.captionInput);
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = db.getReference("Captions");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_createc,null);

//        editCaption = builder.findViewById(R.id.captionInput);
        final EditText editCaption = (EditText) view.findViewById(R.id.captionInput);
        input = editCaption.getText().toString();
        builder.setView(view)
                .setTitle("Caption")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //String cp = "haha";
                        String cp = editCaption.getText().toString().trim();
                        String id = myRef.push().getKey();
                        Caption c = new Caption(id,cp);
                        myRef.child(id).setValue(c);
                        Toast.makeText(getContext(),"uploaded",Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(),editCaption.getText(),Toast.LENGTH_SHORT).show();
                    }
                });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        return builder.create();
    }

    private void WriteCaption(String Id, String caption){
        Caption cap = new Caption(Id,caption);
        mDatabase.child("captions").child(Id).setValue(caption);

    }

}
