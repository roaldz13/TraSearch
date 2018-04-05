package com.example.taquio.trasearch.BusinessProfile;

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
import android.widget.RelativeLayout;

import com.example.taquio.trasearch.Models.ToBuy;
import com.example.taquio.trasearch.Models.ToSell;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Utils.ToBuyAdapter2;
import com.example.taquio.trasearch.Utils.ToSellAdapter2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class BusinessBuyFragment extends Fragment {
    private static final String TAG = "BusinessBuyFragment";

    private RecyclerView rec;
    public RelativeLayout m1, m2;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private ArrayList<ToBuy> tobuyList;
    private ToBuyAdapter2 buyAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.business_profile_buy_fragment, container, false);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        m1 = view.findViewById(R.id.noitem);
        m2 = view.findViewById(R.id.forrecview);

        rec = view.findViewById(R.id.recview);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(getContext());
        rec.setLayoutManager(lm);
        rec.setItemAnimator(new DefaultItemAnimator());

        tobuyList = new ArrayList<>();

        buyAdapter = new ToBuyAdapter2(tobuyList, getContext());

        buyMethod(view);

        hide(view);
        display(view);
        return view;
    }

    private void buyMethod(final View view) {
        FirebaseDatabase.getInstance().getReference().child("ToBuy").child(currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot sp: dataSnapshot.getChildren()){

                            Log.d(TAG, "mao ni: " + sp.getValue(ToBuy.class));
                            Log.d(TAG, "c1: " + sp.getKey());
                            Log.d(TAG, "c2: " + currentUser.getUid());
                            tobuyList.add(sp.getValue(ToBuy.class));
                            if(tobuyList.isEmpty()){
                                display(view);
                            }else {
                                hide(view);
                                rec.setAdapter(buyAdapter);
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