package com.example.taquio.trasearch.Samok;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taquio.trasearch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Del Mar on 2/7/2018.
 */

public class JunkShopsFragment extends Fragment {
    private static final String TAG = "JunkShopsFragment";
    //vars
    private Context mcontext = getActivity();
    private ArrayList<String> mUsers;
    private ArrayList<String> businessUser;
    private ArrayList<String> collectUser;
    private RecyclerView recyclerView;
    private ArrayList<Junkshop> junkshopList;
    private RecyclerViewAdapter adapter;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private int currentPage = 0;
    private static final int TOTAL_ITEM_EACH_LOAD = 2;


    public JunkShopsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_junkshops, container, false);
        Log.d(TAG, "onCreateView: STARTING JUNK SHOP FRAGMENT");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        recyclerView = view.findViewById(R.id.recyclerv_view);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        junkshopList = new ArrayList<>();

        adapter = new RecyclerViewAdapter(junkshopList, mcontext);

        readDatabase();

        return view;
    }

    private void readDatabase() {

        init();
        Query query = databaseReference.child("Users")
                                .orderByKey();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                          mUsers.add(ds.getKey());

                }
                getBusinessUser();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void init() {
        mUsers = new ArrayList<>();
        businessUser = new ArrayList<>();
        collectUser = new ArrayList<>();
    }

    private void getBusinessUser() {
        for(int i = 0; i<mUsers.size(); i++){
            Query q = databaseReference.child("Users")
                    .child(mUsers.get(i))
                    .child("userType");
//                    .orderByChild("userType")
//                    .equalTo("business");
            final int o = i;
            q.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: >> "+ dataSnapshot.getValue());
                    if(dataSnapshot.getValue().equals("business"))
                    {
                        businessUser.add(mUsers.get(o));

                        Log.d(TAG, "onDataChange: >> \n"+ mUsers.get(o));
                        Log.d(TAG, "sakpan "+ businessUser.size());
                        int i = 0;

                            Query query = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(mUsers.get(o));
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Log.d(TAG, "onDataChange: testing " +"[ "+businessUser.size()+"]"+ dataSnapshot.getValue(Junkshop.class));
                                    junkshopList.add(dataSnapshot.getValue(Junkshop.class));
                                    recyclerView.setAdapter(adapter);

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

//                    setDataAdapter();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }

    private void setDataAdapter() {
//       int i = 0;
//        while(i < businessUser.size())
//        {
//
//            Query query = FirebaseDatabase.getInstance().getReference().child("Users")
//                    .child(businessUser.get(i));
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {

//                    Log.d(TAG, "onDataChange: testing " +"[ "+businessUser.size()+"]"+ dataSnapshot.getValue(Junkshop.class));
//
//                    Junkshop myjunk = new Junkshop();
//
//                        Map<String, Object> objectMap = (HashMap<String, Object>) dataSnapshot.getValue();
//
//                        myjunk.setImagePermit(objectMap.get("imagePermit").toString());
//                        myjunk.setImage(objectMap.get("image").toString());
//                        myjunk.setImage_thumb(objectMap.get("image_thumb").toString());
//                        myjunk.setBsnBusinessName(objectMap.get("bsnBusinessName").toString());
//                        myjunk.setBsnEmail(objectMap.get("bsnEmail").toString());
//                        myjunk.setBsnLocation(objectMap.get("bsnLocation").toString());
//                        myjunk.setBsnMobile(objectMap.get("bsnMobile").toString());
//                        myjunk.setBsnPhone(objectMap.get("bsnPhone").toString());
//                        myjunk.setDeviceToken(objectMap.get("deviceToken").toString());
//                        myjunk.setUserId(objectMap.get("userId").toString());
//                        myjunk.setUserType(objectMap.get("userType").toString());

//                    junkshopList.add(dataSnapshot.getValue(Junkshop.class));
//                    recyclerView.setAdapter(adapter);
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//            i++;
//        }
    }
}

