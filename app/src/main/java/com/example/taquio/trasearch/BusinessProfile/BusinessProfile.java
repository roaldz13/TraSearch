package com.example.taquio.trasearch.BusinessProfile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.taquio.trasearch.BusinessHome.BusinessArticleFragment;
import com.example.taquio.trasearch.BusinessHome.BusinessHome;
import com.example.taquio.trasearch.BusinessHome.BusinessItemsFragment;
import com.example.taquio.trasearch.BusinessHome.BusinessVideoFragment;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Samok.BusMyProfileActivity;
import com.example.taquio.trasearch.Samok.HomeActivity2;
import com.example.taquio.trasearch.Samok.MyProfileActivity;
import com.example.taquio.trasearch.Samok.SectionsPagerAdapter;
import com.example.taquio.trasearch.Samok.SettingsActivity;
import com.example.taquio.trasearch.Utils.BusinessBottomNavigationViewHelper;
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

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class BusinessProfile extends AppCompatActivity {

    private static final String TAG = "BusinessProfile";
    private Context mContext = BusinessProfile.this;
    private static final int ACTIVITY_NUM = 2;
    private ViewPager mViewPager;

    TextView tvName, tvEmail, tvMobile, tvPhone, tvLocation;
    Button btnEdit, btnBuy, btnSell, buy2, sell2;
    ImageView verify, notVerify;
    private ImageView mBackArrow, settings;
    CircleImageView profPicImage;
    private DatabaseReference databaseReference;
    private FirebaseUser currentUser;
    private TextView notVerifiedLabel, verifiedLabel;

    String user_id;
    String verifier;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_profile);

        setupBottomNavigationView();

        mViewPager = (ViewPager) findViewById(R.id.busContainer);
        setupViewPager(mViewPager);
        tvName = (TextView) findViewById(R.id.busEditUser);
//        tvEmail = (TextView) findViewById(R.id.busUserEmail);
//        tvMobile = (TextView) findViewById(R.id.busUserNumber);
//        tvPhone = (TextView) findViewById(R.id.busTele);
//        tvLocation = (TextView) findViewById(R.id.busLoc);

        btnEdit = (Button) findViewById(R.id.busBtnEdit);
        btnSell = (Button) findViewById(R.id.btnSell);
        btnBuy = (Button) findViewById(R.id.btnBuy);
//        buy2 = findViewById(R.id.btnBuy2);
//        sell2 = findViewById(R.id.btnSell2);

        mBackArrow = findViewById(R.id.backArrow);
        settings = findViewById(R.id.accSetting);
        verify = (ImageView) findViewById(R.id.imVerify);
        verifiedLabel = findViewById(R.id.verifiedLabel);

        notVerify = (ImageView) findViewById(R.id.imNotVerify);
        notVerifiedLabel = findViewById(R.id.notVerifiedLabel);
        profPicImage = (CircleImageView) findViewById(R.id.busImageView10);

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back");
                startActivity(new Intent(BusinessProfile.this, BusinessHome.class));
//                if(getStringTag().equals("noAction")){
//                    getSupportFragmentManager().popBackStack();
//                    ((BusMyProfileActivity)getApplicationContext()).hideLayout();
//                    startActivity(new Intent(getApplicationContext(),BusinessHome.class));
//                }
//                if(getStringTag().equals("action")){
//                    getSupportFragmentManager().popBackStack();
//                    finish();
//                }
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SettingsActivity.class));

            }
        });
//        sell2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        buy2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(BusinessProfile.this, BusinessBuy2.class);
//                startActivity(i);
//            }
//        });
        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfile.this, BusinessBuy.class);
                startActivity(i);
            }
        });

        btnSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfile.this, BusinessSell.class);
                startActivity(i);
            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessProfile.this, BusinessEdit.class);
                startActivity(i);
            }
        });

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        user_id = currentUser.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                tvName.setText(dataSnapshot.child("bsnBusinessName").getValue().toString());
//                tvEmail.setText(dataSnapshot.child("bsnEmail").getValue().toString());
//                tvMobile.setText(dataSnapshot.child("bsnMobile").getValue().toString());
//                tvPhone.setText(dataSnapshot.child("bsnPhone").getValue().toString());
//                tvLocation.setText(dataSnapshot.child("bsnLocation").getValue().toString());
                Picasso.with(BusinessProfile.this).load(dataSnapshot.child("image").getValue().toString())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.man)
                        .into(profPicImage, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {
                                Picasso.with(BusinessProfile.this)
                                        .load(dataSnapshot
                                                .child("image").getValue().toString())
                                        .placeholder(R.drawable.man)
                                        .into(profPicImage);

                            }
                        });
                verifier = dataSnapshot.child("isVerified").getValue().toString();

                if(verifier.equals("true")) {
                    verify.setVisibility(View.VISIBLE);
                    verifiedLabel.setVisibility(View.VISIBLE);

                    notVerify.setVisibility(View.GONE);
                    notVerifiedLabel.setVisibility(View.GONE);
                }else if (verifier.equals("false")) {
                    verify.setVisibility(View.GONE);
                    verifiedLabel.setVisibility(View.GONE);

                    notVerify.setVisibility(View.VISIBLE);
                    notVerifiedLabel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
//        setupViewPager(mViewPager);
    }
    private String getStringTag(){
        Bundle bundle = getIntent().getBundleExtra("Action");
        if(bundle != null){
            return bundle.getString("Action");
        }else{
            return null;
        }
    }
    private void setupViewPager(ViewPager viewPager) {

        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new BusinessBuyFragment(), "Buy");
        adapter.addFragment(new BusinessSellFragment(), "Sell");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.busProfileTabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.businessBottomNavViewBar);
        BusinessBottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BusinessBottomNavigationViewHelper.enableNavigation(mContext,this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

}
