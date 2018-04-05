package com.example.taquio.trasearch.Samok;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taquio.trasearch.Models.User;
import com.example.taquio.trasearch.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllUsersFragment extends Fragment {
    private static final String TAG = "AllUsersFragment";
    private RecyclerView mFriendList;
    private FirebaseAuth mAuth;
    private DatabaseReference mFriendsDatabase
            ,mUsersDatabase;

    private String mCurrent_user_id;

    private View mMainView;

    public AllUsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentmMainView = inflater
        mMainView = inflater
                .inflate(R.layout.fragment_all_users,container,false);
        mFriendList = mMainView
                .findViewById(R.id.userList);
        mAuth = FirebaseAuth
                .getInstance();
        mCurrent_user_id = mAuth.
                getCurrentUser()
                .getUid();
        mUsersDatabase = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("Users");
        mUsersDatabase.keepSynced(true);

        mFriendList
                .setHasFixedSize(true);
        mFriendList
                .setLayoutManager(new LinearLayoutManager(getContext()));
        return mMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        final FirebaseRecyclerAdapter<AllUsers,AdminViewHolder> allUsersRecyclerAdapter = new FirebaseRecyclerAdapter<AllUsers,AdminViewHolder>(
                AllUsers.class,
                R.layout.all_users,
                AllUsersFragment.AdminViewHolder.class,
                mUsersDatabase.orderByChild("isVerified")
        ) {
            @Override
            protected void populateViewHolder(final AdminViewHolder viewHolder, final AllUsers model, int position) {
//                viewHolder.setDate(model.getDate());

                final String list_user_Id = getRef(position).getKey();

                Log.d(TAG, "populateViewHolder: UserID: "+list_user_Id);

                        mUsersDatabase.child(list_user_Id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onDataChange: " + dataSnapshot);
                                if(dataSnapshot.hasChild("userType"))
                                {
                                    String userType = dataSnapshot.child("userType").getValue().toString();
                                    Log.d(TAG, "onDataChange: UserType: "+userType);
                                    if (userType.equals("admin"))
                                    {
                                        String email = dataSnapshot.child("Email").getValue().toString();
                                        String Name = dataSnapshot.child("Name").getValue().toString();
                                        String profile_thuumb = dataSnapshot.child("Image_thumb").getValue().toString();
                                        String isOnline =  dataSnapshot.child("online").getValue().toString();
                                        Boolean isVerify = dataSnapshot.child("isVerified").getValue(Boolean.class);
                                        viewHolder.setEmail(email);
                                        viewHolder.setName(Name);
                                        viewHolder.setProfileImage(profile_thuumb,getContext());

                                        viewHolder.toVerify(isVerify);

                                        if(isOnline.equals("online"))
                                        {
                                            viewHolder.setuserOnline(true);
                                        }else
                                        {
                                            viewHolder.setuserOnline(false);
                                        }

                                    }
                                    else if (userType.equals("business"))
                                    {
                                        Log.d(TAG, "onDataChange: BusinessType");
                                        String email = dataSnapshot.child("bsnEmail").getValue().toString();
                                        String Name = dataSnapshot.child("bsnBusinessName").getValue().toString();
                                        String profile_thuumb = dataSnapshot.child("image").getValue().toString();
//                                        String isOnline =  dataSnapshot.child("online").getValue().toString();
                                        Boolean isVerify = dataSnapshot.child("isVerified").getValue(Boolean.class);
                                        viewHolder.setEmail(email);
                                        viewHolder.setName(Name);
                                        viewHolder.setProfileImage(profile_thuumb,getContext());

                                        viewHolder.toVerify(isVerify);

//                                        if(isOnline.equals("online"))
//                                        {
//                                            viewHolder.setuserOnline(true);
//                                        }else
//                                        {
//                                            viewHolder.setuserOnline(false);
//                                        }
                                    }
                                    else if(userType.equals("non-business"))
                                    {
                                        Log.d(TAG, "onDataChange: Free Type");
                                        String email = dataSnapshot.child("Email").getValue().toString();
                                        String Name = dataSnapshot.child("Name").getValue().toString();
                                        String profile_thuumb = dataSnapshot.child("Image_thumb").getValue().toString();
                                        Boolean isVerify =   dataSnapshot.child("isVerified").getValue(Boolean.class);
                                        Log.d(TAG, "kuha: " + isVerify);

                                        viewHolder.setEmail(email);
                                        viewHolder.setName(Name);
                                        viewHolder.setProfileImage(profile_thuumb,getContext());

                                        viewHolder.toVerify(isVerify);
                                        if(dataSnapshot.hasChild("online"))
                                        {
                                            String isOnline =  dataSnapshot.child("online").getValue().toString();
                                            if(isOnline.equals("online"))
                                            {
                                                viewHolder.setuserOnline(true);
                                            }else
                                            {
                                                viewHolder.setuserOnline(false);
                                            }
                                        }

                                    }
                                    else {
                                        Log.d(TAG, "onDataChange: NoType");
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


            }
        };
        mFriendList.setAdapter(allUsersRecyclerAdapter);

    }

    public static class AdminViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public AdminViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setEmail(String Email)
        {
            TextView emailField = mView.findViewById(R.id.allUsersEmail);
            emailField.setText(Email);
        }

        public void toVerify(Boolean isVerify)
        {
            final ImageView userNameView  = mView.findViewById(R.id.isVerify);
            Log.d(TAG, "toVerify: " + isVerify);
            if(isVerify)
            {
//                userNameView.setImageResource(R.drawable.verify);
                Picasso.with(mView.getContext()).load(R.drawable.verify)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.verify)
                        .into(userNameView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(mView.getContext())
                                        .load(R.drawable.verify)
                                        .placeholder(R.drawable.verify)
                                        .into(userNameView);
                            }
                        });
            }else
            {
//                userNameView.setImageResource(R.drawable.not_verify);
                Picasso.with(mView.getContext()).load(R.drawable.not_verify)
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.not_verify)
                        .into(userNameView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(mView.getContext())
                                        .load(R.drawable.not_verify)
                                        .placeholder(R.drawable.not_verify)
                                        .into(userNameView);
                            }
                        });
            }
        }
        public void setName (String Name)
        {
            TextView nameView = mView.findViewById(R.id.allUsersName);
            nameView.setText(Name);
        }
        public void setProfileImage (final String ImageURL, final Context cTHis)
        {
            final CircleImageView mImageHolder = mView.findViewById(R.id.allUsersImg);
            Picasso.with(cTHis).load(ImageURL)
                    .into(mImageHolder);
            Picasso.with(cTHis).load(ImageURL)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.user_default)
                    .into(mImageHolder, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(cTHis)
                                    .load(ImageURL)
                                    .placeholder(R.drawable.user_default)
                                    .into(mImageHolder);
                        }
                    });
        }
        public void setuserOnline (Boolean online_status)
        {
            CircleImageView userOnlineView = mView.findViewById(R.id.stat_icon);

            if(online_status)
            {
                userOnlineView.setVisibility(View.VISIBLE);
            }
            else
            {
                userOnlineView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
