package com.example.friendfriend.caption;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.friendfriend.R;

public class CaptionActivity extends AppCompatActivity {

    private static final String TAG = "CaptionActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caption_page);
        getIncomingIntent();
    }
    private void getIncomingIntent(){
        if(getIntent().hasExtra("caption_id")&& getIntent().hasExtra("caption_details")){

            String capId = getIntent().getStringExtra("caption_id");
            String capD = getIntent().getStringExtra("caption_details");

            setCaption(capId,capD);
        }
    }
    private void setCaption(String capId,String capD){
        TextView cid = findViewById(R.id.id);
        //cid.setText(capId);
        TextView cpD = findViewById(R.id.caption);
        cpD.setText(capD);

    }
}
