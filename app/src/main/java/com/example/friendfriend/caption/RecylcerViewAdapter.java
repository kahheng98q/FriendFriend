package com.example.friendfriend.caption;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendfriend.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecylcerViewAdapter extends  RecyclerView.Adapter<RecylcerViewAdapter.ViewHolder> {

    private static final String TAG = "RecylcerViewAdapter";

    private ArrayList<String> mId = new ArrayList<>();
    private ArrayList<String> mCap = new ArrayList<>();
    private Context mContext;

    public RecylcerViewAdapter(Context context,ArrayList<String> id,ArrayList<String> caption){
        mCap = caption;
        mId = id;
        mContext = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Log.d(TAG,"onBindViewHolder: called.");

//        holder.id.setText(mId.get(position));
        holder.cap.setText(mCap.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick: clicked on: "+mId.get(position));
                Toast.makeText(mContext,mId.get(position),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext,CaptionActivity.class);
                intent.putExtra("caption_id",mId.get(position));
                intent.putExtra("caption_details",mCap.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mId.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView id;
        TextView cap;
        ConstraintLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id_txtView);
            cap = itemView.findViewById(R.id.cap_txtView);
            parentLayout = itemView.findViewById(R.id.paren_Layout);
        }
    }


}
