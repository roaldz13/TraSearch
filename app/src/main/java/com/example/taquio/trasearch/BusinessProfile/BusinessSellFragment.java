package com.example.taquio.trasearch.BusinessProfile;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.taquio.trasearch.Models.ToBuy;
import com.example.taquio.trasearch.Models.ToSell;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Utils.ToBuyAdapter;
import com.example.taquio.trasearch.Utils.ToSellAdapter;
import com.example.taquio.trasearch.Utils.ToSellAdapter2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class BusinessSellFragment extends Fragment {
    private static final String TAG = "BusinessSellFragment";
    Button btn;
    private RecyclerView rec;
    public RelativeLayout m1, m2;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private ArrayList<ToSell> tosellList;
    private ToSellAdapter2 sellAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.business_profile_sell_fragment, container, false);
//        btn = (Button) view.findViewById(R.id.btnAddSell);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        m1 = view.findViewById(R.id.noitem);
        m2 = view.findViewById(R.id.forrecview);

        rec = view.findViewById(R.id.recview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rec.setLayoutManager(lm);
        rec.setItemAnimator(new DefaultItemAnimator());

        tosellList = new ArrayList<>();

        sellAdapter = new ToSellAdapter2(tosellList, getContext());

        sellMethod(view);

        hide(view);
        display(view);
        return view;
    }

    private void sellMethod(final View view) {
        FirebaseDatabase.getInstance().getReference().child("ToSell").child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot sp: dataSnapshot.getChildren()){

                            Log.d(TAG, "mao ni: " + sp.getValue(ToBuy.class));
                            Log.d(TAG, "c1: " + sp.getKey());
                            Log.d(TAG, "c2: " + currentUser.getUid());
                            tosellList.add(sp.getValue(ToSell.class));
                            if(tosellList.isEmpty()){
                                display(view);
                            }else {
                                hide(view);
                                rec.setAdapter(sellAdapter);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    public void hide(View view){

        m1.setVisibility(View.GONE);
        m2.setVisibility(View.VISIBLE);
    }
    public void display(View view){

        m1.setVisibility(View.VISIBLE);
        m2.setVisibility(View.GONE);
    }

}