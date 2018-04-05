package com.example.taquio.trasearch.Samok;

import android.content.Context;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.taquio.trasearch.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ReportActivity extends AppCompatActivity {

    private static final String TAG = "ReportActivity";
    private RecyclerView reported_list;
    private DatabaseReference mReportDatabase;
    private ImageView reported_Image;
    private String clicked_userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        reported_Image =findViewById(R.id.reported_Image);
        clicked_userID = getIntent().getStringExtra("userID");
        reported_list = findViewById(R.id.reported_list);
        String user_id = getIntent().getStringExtra("userID");

        Log.d(TAG, "onCreate: "+user_id);
        mReportDatabase = FirebaseDatabase.getInstance().getReference().child("Reports").child(user_id);

        reported_list.setLayoutManager(new LinearLayoutManager(this));



    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Reports,ReportedViewHolder> reportedAdapter = new FirebaseRecyclerAdapter<Reports, ReportedViewHolder>(
                Reports.class,
                R.layout.reported_sigle,
                ReportedViewHolder.class,
                mReportDatabase
        ) {
            @Override
            protected void populateViewHolder(final ReportedViewHolder viewHolder, Reports model, int position) {
                String list_ids = getRef(position).getKey();
                mReportDatabase.child(clicked_userID).child(list_ids).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDataChange: Photo Path: "+dataSnapshot.child("report_details").child("photo_path").getValue().toString());

                        Picasso.with(ReportActivity.this).load(dataSnapshot.child("report_details").child("photo_path").getValue().toString())
                                .into(reported_Image);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        };

        reported_list.setAdapter(reportedAdapter);
    }

    public static class ReportedViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public ReportedViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setImage (String Url, Context context)
        {

        }
    }


}
