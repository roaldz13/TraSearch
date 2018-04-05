package com.example.taquio.trasearch.Samok;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taquio.trasearch.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment {
    private static final String TAG = "ReportFragment";
    private View mMainView;
     DatabaseReference mReportsDatabase,mUsersDatabase;
    private RecyclerView mReportsList;

    public ReportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mMainView = inflater
                .inflate(R.layout.fragment_report,container,false);
        mReportsList = mMainView.findViewById(R.id.report_list);
        mReportsDatabase = FirebaseDatabase.getInstance().getReference().child("Reports");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");


        mReportsList.setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Reports,ReportsViewHolder> ReportsAdapter = new FirebaseRecyclerAdapter<Reports, ReportsViewHolder>(
                Reports.class,
                R.layout.report_single,
                ReportsViewHolder.class,
                mReportsDatabase
        ) {
            @Override
            protected void populateViewHolder(final ReportsViewHolder viewHolder, Reports model, int position) {
                final String reporterUserID_list = getRef(position).getKey();
                mReportsDatabase.child(reporterUserID_list).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUsersDatabase.child(reporterUserID_list).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(final DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child("userType").getValue().equals("non-business")){
                                    viewHolder.setReporterName(dataSnapshot.child("Email").getValue().toString());
                                    viewHolder.setReportersDesc(dataSnapshot.child("Name").getValue().toString());
                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.d(TAG, "onClick: Passing: "+dataSnapshot.child("userID").getValue().toString());
                                            startActivity(new Intent(getContext(),ReportViewActivity.class)
                                                    .putExtra("userID",dataSnapshot.child("userID").getValue().toString()));

                                        }
                                    });
                                }else if(dataSnapshot.child("userType").getValue().equals("business")){
                                    viewHolder.setReporterName(dataSnapshot.child("bsnEmail").getValue().toString());
                                    viewHolder.setReportersDesc(dataSnapshot.child("bsnBusinessName").getValue().toString());
                                    viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.d(TAG, "onClick: Passing: "+dataSnapshot.child("userID").getValue().toString());
                                            startActivity(new Intent(getContext(),ReportViewActivity.class)
                                                    .putExtra("userID",dataSnapshot.child("userID").getValue().toString()));

                                        }
                                    });
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        mReportsList.setAdapter(ReportsAdapter);
    }

    public static class ReportsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public ReportsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setReporterName (String reporter)
        {
            TextView reporterName = itemView.findViewById(R.id.report_nameofReporter);
            reporterName.setText(reporter);
        }
        public void setReportersDesc (String desc)
        {
            TextView reporterDesc = itemView.findViewById(R.id.report_discription);
            reporterDesc.setText(desc);
        }
    }

}
