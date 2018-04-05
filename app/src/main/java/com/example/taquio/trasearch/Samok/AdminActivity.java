package com.example.taquio.trasearch.Samok;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toolbar;

import com.example.taquio.trasearch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class AdminActivity extends AppCompatActivity {

    private static final String TAG = "MessagesActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = AdminActivity.this;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.mytoolbar);
        setSupportActionBar(toolbar);
        setupViewPager();
    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new UnverifiedUsers());
        adapter.addFragment(new AllUsersFragment());
        adapter.addFragment(new MaterialsFragment());
        adapter.addFragment(new ReportFragment());
        ViewPager viewPager = findViewById(R.id.adminContainer);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.adminTabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Not Verified");
        tabLayout.getTabAt(1).setText("Users");
        tabLayout.getTabAt(2).setText("Materials");
        tabLayout.getTabAt(3).setText("Reports");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//
        getMenuInflater().inflate(R.menu.notif_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        return super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.logout){
            mAuth = FirebaseAuth.getInstance();
            mAuth.signOut();
            startActivity(new Intent(AdminActivity.this, GuestSearch.class));
            finish();
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        if(mAuth.getCurrentUser()!=null)
        {
            mDatabase.child("online").setValue("online");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());
        if(mAuth.getCurrentUser()!=null)
        {
            mDatabase.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }
}
