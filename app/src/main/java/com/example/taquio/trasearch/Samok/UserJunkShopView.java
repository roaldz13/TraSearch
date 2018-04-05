package com.example.taquio.trasearch.Samok;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.Messages.MessageActivity;
import com.example.taquio.trasearch.Models.ToBuy;
import com.example.taquio.trasearch.Models.ToSell;
import com.example.taquio.trasearch.Models.User;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Utils.BottomNavigationViewHelper;
import com.example.taquio.trasearch.Utils.ToBuyAdapter;
import com.example.taquio.trasearch.Utils.ToSellAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class UserJunkShopView extends AppCompatActivity {

    private static final String TAG = "BusinessProfile";
    private Context mContext = UserJunkShopView.this;
    private static final int ACTIVITY_NUM = 0;

    TextView tvName, tvEmail, tvMobile, tvPhone, tvLocation;
    Button btnBuy, btnSell, btnEdit,btnRoute, dm;
    ImageView verify, notVerify, back;
    CircleImageView profPicImage;
    private BottomNavigationViewEx bottomNavigationView;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    String user_id;
    String verifier;
    private RecyclerView tobuy, tosell;
    private ArrayList<String> tobuyMat;
    private ArrayList<String> tosellMat;
    private ArrayList<ToBuy> tobuyList;
    private ArrayList<ToSell> tosellList;
    private ToBuyAdapter buyAdapter;
    private ToSellAdapter sellAdapter;

//    UserJunkShopView(){}

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_activity_profile);
        Log.d(TAG, "onCreate: business profile 2 sstarting");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        //For BUY
        tobuy = findViewById(R.id.recycler1);
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        tobuy.setLayoutManager(lm);
        tobuy.setItemAnimator(new DefaultItemAnimator());

        tobuyList = new ArrayList<>();

        buyAdapter = new ToBuyAdapter(tobuyList, this);
//---------------------------------------------------
        tosell = findViewById(R.id.recycler2);
        RecyclerView.LayoutManager lm1 = new LinearLayoutManager(this);
        tosell.setLayoutManager(lm1);
        tosell.setItemAnimator(new DefaultItemAnimator());

        tosellList = new ArrayList<>();

        sellAdapter = new ToSellAdapter(tosellList, this);


        //For Sell
        final Junkshop bundle = (Junkshop) getIntent().getParcelableExtra("busprofile");
        getBuy(bundle);
        getSell(bundle);


//        setupViewPager();
        dm = findViewById(R.id.dm);
        tvName = (TextView) findViewById(R.id.busEditUser);
//        tvEmail = (TextView) findViewById(R.id.busUserEmail);
        tvMobile = (TextView) findViewById(R.id.busUserNumber);
//        tvPhone = (TextView) findViewById(R.id.busTele);
        tvLocation = (TextView) findViewById(R.id.busLoc);
//        btnBuy = (Button) findViewById(R.id.btnBuy);
//        btnSell = (Button) findViewById(R.id.btnSell);
        btnRoute = findViewById(R.id.route);
        back = findViewById(R.id.backArrow);
//        btnEdit = (Button) findViewById(R.id.busBtnEdit);
//        bottomNavigationView = findViewById(R.id.bottomNavViewBar);

        verify = (ImageView) findViewById(R.id.imVerify);
        notVerify = (ImageView) findViewById(R.id.imNotVerify);
        profPicImage = (CircleImageView) findViewById(R.id.busImageView10);

//        setupBottomNavigationView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(UserJunkShopView.this, HomeActivity2.class));
            }
        });
        //pass data to MapActivity
        btnRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Junkshop junkshop = (Junkshop) getIntent().getParcelableExtra("busprofile");
                Intent i = new Intent(UserJunkShopView.this, MapActivity.class);
                i.putExtra("CallFrom", "fromprofile");
                i.putExtra("BusinessDetail", bundle);
                startActivity(i);
            }
        });
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.child("Users").orderByChild("userID").equalTo(bundle.getUserID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                            final Junkshop user = singleSnapshot.getValue(Junkshop.class);
                            dm.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent i = new Intent(mContext, MessageActivity.class);
                                    i.putExtra("user_id", bundle.getUserID());
                                    i.putExtra("user_name", user.getBsnBusinessName());
                                    mContext.startActivity(i);
                                }
                            });
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        tvName.setText(bundle.getBsnBusinessName());
//        tvEmail.setText(bundle.getBsnEmail());
        tvMobile.setText(bundle.getBsnMobile());
//        tvPhone.setText(bundle.getBsnPhone());
        tvLocation.setText(bundle.getBsnLocation());
        Picasso.with(UserJunkShopView.this).load(bundle.getImage())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.man)
                .into(profPicImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(UserJunkShopView.this)
                                .load(bundle.getImage())
                                .placeholder(R.drawable.man)
                                .into(profPicImage);

                    }
                });
        verifier = bundle.getVerified().toString();

        if(verifier.equals("true")) {
            verify.setVisibility(View.VISIBLE);
            notVerify.setVisibility(View.GONE);
        }else if(verifier.equals("false")) {
            verify.setVisibility(View.GONE);
            notVerify.setVisibility(View.VISIBLE);
        }
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        user_id = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(bundle.getUserID());




    }

    private void getSell(final Junkshop bundle) {
        FirebaseDatabase.getInstance().getReference().child("ToSell").child(bundle.getUserID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot sp: dataSnapshot.getChildren()){

                            Log.d(TAG, "mao ni: " + sp.getValue(ToBuy.class));
                            Log.d(TAG, "c1: " + sp.getKey());
                            Log.d(TAG, "c2: " + bundle.getUserID());
                            tosellList.add(sp.getValue(ToSell.class));
                            tosell.setAdapter(sellAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    private void getBuy(final Junkshop bundle) {
        FirebaseDatabase.getInstance().getReference().child("ToBuy").child(bundle.getUserID())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

//                        tobuyList.add(dataSnapshot.getValue(ToBuy.class));
//                        Log.d(TAG, "tobuy value : "+ dataSnapshot.getValue(ToBuy.class));
//                        tobuy.setAdapter(buyAdapter);
                        for(DataSnapshot sp: dataSnapshot.getChildren()){

                            Log.d(TAG, "mao ni: " + sp.getValue(ToBuy.class));
                            Log.d(TAG, "c1: " + sp.getKey());
                            Log.d(TAG, "c2: " + bundle.getUserID());
                            tobuyList.add(sp.getValue(ToBuy.class));
                            tobuy.setAdapter(buyAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

//    private void setupBottomNavigationView(){
//        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
//        BottomNavigationViewHelper.enableNavigation(mContext,this ,bottomNavigationView);
//        Menu menu = bottomNavigationView.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//    }

}
