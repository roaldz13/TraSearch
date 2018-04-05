package com.example.taquio.trasearch.Messages;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Samok.Conv;
import com.example.taquio.trasearch.Samok.Conv2;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Del Mar on 2/15/2018.
 */

public class BusInboxFragment extends Fragment {
    private static final String TAG = "BusInboxFragment";
    private RecyclerView mConvList;
    private DatabaseReference mConvDatabase;
    private DatabaseReference mMessageDatabase;
    private DatabaseReference mUsersDatabase;

    private FirebaseAuth mAuth;

    private String mCurrent_user_id;

    private View mMainView;


    public BusInboxFragment() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,@NonNull ViewGroup container,@NonNull Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: InboxStarted");
        mMainView = inflater.inflate(R.layout.business_fragment_inbox, container, false);

        mConvList = mMainView.findViewById(R.id.mConvList2);
        mAuth = FirebaseAuth.getInstance();

        mCurrent_user_id = mAuth.getCurrentUser().getUid();

        mConvDatabase = FirebaseDatabase.getInstance().getReference().child("Chat").child(mCurrent_user_id);

        mConvDatabase.keepSynced(true);
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mMessageDatabase = FirebaseDatabase.getInstance().getReference().child("Messages").child(mCurrent_user_id);
        mUsersDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mConvList.setHasFixedSize(true);
        mConvList.setLayoutManager(linearLayoutManager);


        // Inflate the layout for this fragment
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: Started");
        Query conversationQuery2 = mConvDatabase.orderByChild("timestamp");

        FirebaseRecyclerAdapter<Conv2, ConvViewHolder> firebaseConvAdapter2 = new FirebaseRecyclerAdapter<Conv2, ConvViewHolder>(
                Conv2.class,
                R.layout.business_inbox,
                ConvViewHolder.class,
                conversationQuery2
        ) {
            @Override
            protected void populateViewHolder(final ConvViewHolder convViewHolder, final Conv2 conv, int i) {



                final String list_user_id = getRef(i).getKey();
                Log.d(TAG, "populateViewHolder: " + list_user_id);
                Query lastMessageQuery2 = mMessageDatabase.child(list_user_id).limitToLast(1);

                lastMessageQuery2.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        String data = dataSnapshot.child("message").getValue().toString();
                        convViewHolder.setMessage(data, conv.isSeen());

                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                mUsersDatabase.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d(TAG, "testget: "+ list_user_id);
                        Log.d(TAG, "testget: "+ dataSnapshot.getKey());
                        Log.d(TAG, "testget: "+ dataSnapshot.child("userType").getValue());

                        if(dataSnapshot.child("userType").getValue().equals("non-business")){
                            final String userName = dataSnapshot.child("Name").getValue().toString();
                            if(dataSnapshot.hasChild("Image_thumb"))
                            {
                                String userThumb = dataSnapshot.child("Image_thumb").getValue().toString();
                                convViewHolder.setUserImage(userThumb, getContext());
                                Log.d(TAG, "Success: " +  userName + userThumb);
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            }
                            if(dataSnapshot.hasChild("online")) {

                                String userOnline = dataSnapshot.child("online").getValue().toString();
                                convViewHolder.setUserOnline(userOnline);
                            }

                            convViewHolder.setName(userName);


                            convViewHolder.myView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    Intent chatIntent = new Intent(getContext(), BusMessageActivity.class);
                                    chatIntent.putExtra("user_id", list_user_id);
                                    chatIntent.putExtra("user_name", userName);
                                    startActivity(chatIntent);

                                }
                            });
                        }
                        if(dataSnapshot.child("userType").getValue().equals("business")){
                            final String userName = dataSnapshot.child("bsnBusinessName").getValue().toString();
                            if(dataSnapshot.hasChild("image_thumb"))
                            {
                                String userThumb = dataSnapshot.child("image_thumb").getValue().toString();
                                convViewHolder.setUserImage(userThumb, getContext());
                                Log.d(TAG, "Success: ");
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                            }
                            if(dataSnapshot.hasChild("online")) {

                                String userOnline = dataSnapshot.child("online").getValue().toString();
                                convViewHolder.setUserOnline(userOnline);
                            }

                            convViewHolder.setName(userName);


                            convViewHolder.myView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {


                                    Intent chatIntent = new Intent(getContext(), BusMessageActivity.class);
                                    chatIntent.putExtra("user_id", list_user_id);
                                    chatIntent.putExtra("user_name", userName);
                                    startActivity(chatIntent);

                                }
                            });
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        };
        Log.d(TAG, "onStartindex: " + firebaseConvAdapter2 );
        mConvList.setAdapter(firebaseConvAdapter2);
    }


    public static class ConvViewHolder extends RecyclerView.ViewHolder {
        View myView;

        public ConvViewHolder(View itemView) {
            super(itemView);

            myView = itemView;

        }

        public void setMessage(String message, boolean isSeen){

            TextView userStatusView = myView.findViewById(R.id.allUsersEmail);
            userStatusView.setText(message);

            if(!isSeen){
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.BOLD);
            } else {
                userStatusView.setTypeface(userStatusView.getTypeface(), Typeface.NORMAL);
                userStatusView.setTextColor(Color.GRAY);
            }

        }

        public void setName(String name){

            TextView userNameView = myView.findViewById(R.id.allUsersName);
            userNameView.setText(name);

        }

        public void setUserImage(String thumb_image, Context ctx){

            CircleImageView userImageView = myView.findViewById(R.id.allUsersImg);
//            Picasso.with(ctx).load(thumb_image).placeholder(R.drawable.man).into(userImageView);
            Log.d(TAG, "setUserImage: na set"+ thumb_image);

            final ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.displayImage(thumb_image, userImageView);

        }

        public void setUserOnline(String online_status) {

            CircleImageView userOnlineView =  myView.findViewById(R.id.stat_icon);

            if(online_status.equals("online")){

                userOnlineView.setVisibility(View.VISIBLE);

            } else {

                userOnlineView.setVisibility(View.INVISIBLE);

            }

        }

    }
}
