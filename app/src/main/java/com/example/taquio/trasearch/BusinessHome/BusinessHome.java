package com.example.taquio.trasearch.BusinessHome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.taquio.trasearch.Guest.GuestHome;
import com.example.taquio.trasearch.Models.Photo;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Samok.ActivityLogin;
import com.example.taquio.trasearch.Samok.BusViewPostFragment;
import com.example.taquio.trasearch.Samok.SectionsPagerAdapter;
import com.example.taquio.trasearch.Utils.BusOtherUserViewPost;
import com.example.taquio.trasearch.Utils.BusinessBottomNavigationViewHelper;
import com.example.taquio.trasearch.Utils.BusinessMainFeedListAdapter;
import com.example.taquio.trasearch.Utils.Donate;
import com.example.taquio.trasearch.Utils.OtherUserViewPost;
import com.example.taquio.trasearch.Utils.UniversalImageLoader;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class BusinessHome extends AppCompatActivity
        implements BusinessMainFeedListAdapter.OnLoadMoreItemsListener {

    private static final String TAG = "BusinessHome";
    private Context mContext = BusinessHome.this;
    private static final int ACTIVITY_NUM = 0;
    FloatingActionButton floating;
    private FrameLayout mFrameLayout;
    private RelativeLayout mRelativeLayout;
    private DatabaseReference mUserDatabase;
    private DatabaseReference mUserRef;
    private FirebaseAuth mAuth;
    private ViewPager mViewPager;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private TextView notifications_badgeText;
    private EditText searchText;
    @Override
    public void onLoadMoreItems() {
        Log.d(TAG, "onLoadMoreItems: displaying more photos");

        BusinessItemsFragment fragment = (BusinessItemsFragment) getSupportFragmentManager()
                .findFragmentByTag("android:switcher:" + R.id.businessHomeContainer + ":" + mViewPager.getCurrentItem());
        if(fragment != null){
            fragment.displayMorePhotos();
        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_activity_home);

        MobileAds.initialize(BusinessHome.this, "ca-app-pub-3940256099942544~3347511713");
        mViewPager = findViewById(R.id.businessHomeContainer);
        mFrameLayout = findViewById(R.id.frame_container);
        mRelativeLayout = findViewById(R.id.homelayout);
        mAuth = FirebaseAuth.getInstance();
        mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        setUpFirebaseAuth();
        initImageLoader();
        setupBottomNavigationView();
        setupViewPager();

        searchText = (EditText) findViewById(R.id.searchV);
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessHome.this, GuestHome.class);
                startActivity(i);
            }
        });

        floating = (FloatingActionButton) findViewById(R.id.floatingButton);

        floating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(BusinessHome.this, Donate.class);
                startActivity(i);
            }
        });

    }
    public void onImageSelected(Photo item, int i, final String user_id) {


        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(user_id.equals(currentUser.getUid())) {

            BusViewPostFragment fragment = new BusViewPostFragment();
            Bundle args = new Bundle();
            args.putParcelable(getString(R.string.photo), item);
            args.putInt(getString(R.string.activity_number), i);
            args.putString("theCall", "fromHome");

            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack("View Post");
            transaction.commit();
        }else{
            BusOtherUserViewPost fragment = new BusOtherUserViewPost();
            Bundle args = new Bundle();
            args.putParcelable(getString(R.string.photo), item);
            args.putInt(getString(R.string.activity_number), i);
            args.putString("theCall", "fromHome");

            fragment.setArguments(args);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, fragment);
            transaction.addToBackStack("View Post");
            transaction.commit();
        }

    }
    public void hideLayout(){
        Log.d(TAG, "hideLayout: hiding layout");
        mRelativeLayout.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);
    }


    public void showLayout(){
        Log.d(TAG, "hideLayout: showing layout");
        mRelativeLayout.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);
    }
    @Override
    public void onBackPressed() {

        if(mFrameLayout.getVisibility() == View.VISIBLE){
            showLayout();
        }
//        super.onBackPressed();
    }
    private void initImageLoader() {
        UniversalImageLoader universalImageLoader = new UniversalImageLoader(mContext);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());
    }
    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause: OnPause Started");
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser!=null) {
            Log.d(TAG, "onPause: User Offline");
            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }
    private void sendToStart()
    {
        Log.d(TAG, "sendToStart: Back to login page");
        startActivity(new Intent(BusinessHome.this,ActivityLogin.class));
        finish();
    }
    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new BusinessVideoFragment());
//        adapter.addFragment(new BusinessArticleFragment());
        adapter.addFragment(new BusinessItemsFragment());
        ViewPager viewPager = (ViewPager) findViewById(R.id.businessHomeContainer);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.busHomeTabLayout);
        tabLayout.setupWithViewPager(viewPager);

//        tabLayout.getTabAt(0).setText("Videos");
//        tabLayout.getTabAt(1).setText("Articles");
        tabLayout.getTabAt(0).setText("Items");
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.businessBottomNavViewBar);
        BusinessBottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BusinessBottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

        Log.d(TAG, "setupBottomNavigationView: Start Notification Badge");
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationViewEx.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(1);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;

        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notlayout, bottomNavigationMenuView, false);
        DatabaseReference mSeen = FirebaseDatabase.getInstance().getReference().child("Chat").child(mAuth.getCurrentUser().getUid());
        Log.d(TAG, "setupBottomNavigationView: Start Counting....");

        mSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int resultCount =0;
                notifications_badgeText = findViewById(R.id.notifications_badgeText);

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
//                    Log.d(TAG, "" + childDataSnapshot.getValue()); //displays the key for the node
                    Log.d(TAG, "" + childDataSnapshot.child("seen").getValue());//gives the value for given keyname
                    if (childDataSnapshot.child("seen").getValue().toString().equals("false"))
                    {
                        Log.d(TAG, "onDataChange: RESULT IS FALSE");
                        resultCount++;
                    }
                }
                if(resultCount>0)
                {
                    notifications_badgeText.setText((resultCount++)+"");
                    notifications_badgeText.setVisibility(View.VISIBLE);
                }
                else
                {
                    notifications_badgeText.setVisibility(View.INVISIBLE);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        itemView.addView(badge);
    }
    private void checkCurrentUser(FirebaseUser user){

        if(user == null){
            Intent intent = new Intent(mContext, ActivityLogin.class);
            startActivity(intent);
        }
    }
    private void setUpFirebaseAuth(){
        Log.d(TAG, "setUpFirebase: FIREBASE SETTING UP....");
        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                checkCurrentUser(user);
                if(user != null){
                    Log.d(TAG, "onAuthStateChanged: SIGNED IN!"+ user.getUid());
                }else{
                    Log.d(TAG, "onAuthStateChanged: SIGNED OUT!");
                }
            }
        };
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Started");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null)
        {
            Log.d(TAG, "onStart: Calling back to start method");
            sendToStart();
        }
//        else
//        {
//            Log.d(TAG, "onStart: User Online");
//            mUserRef.child("online").setValue("true");
//        }
        mAuth.addAuthStateListener(mAuthStateListener);
        if(mAuth.getCurrentUser()!=null)
        {
            mUserDatabase.child("online").setValue("online");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            mAuth.removeAuthStateListener(mAuthStateListener);
            mUserDatabase.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }
}
