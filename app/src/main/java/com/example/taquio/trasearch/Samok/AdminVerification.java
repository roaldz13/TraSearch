package com.example.taquio.trasearch.Samok;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.example.taquio.trasearch.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class AdminVerification extends AppCompatActivity {
    private static final String TAG = "AdminVerification";
    String user_id;
    DatabaseReference userDatabase,forVerification;
    FirebaseAuth mAuth;
    private Button ver_IDbtn,ver_Selfiebtn, toVerify, toRevoke;
    private ImageView ver_ID,ver_Selfie, permitBusiness;
    private Uri selfie,mID;
    private String mCurrentUser;
    private RelativeLayout m1;
    private LinearLayout l1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_verification);
        mAuth = FirebaseAuth.getInstance();
        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        forVerification = FirebaseDatabase.getInstance().getReference().child("ForVerification");

        m1 = findViewById(R.id.para_business);
        l1 = findViewById(R.id.para_nonbusiness);

        hideLayout();
        showLayout();

        ver_ID = findViewById(R.id.ver_ID);
        ver_Selfie = findViewById(R.id.ver_Selfie);
        ver_IDbtn = findViewById(R.id.ver_IDbtn);
        ver_Selfiebtn = findViewById(R.id.ver_Selfiebtn);
        toVerify = findViewById(R.id.ver_Skipbtn);
        toRevoke = findViewById(R.id.ver_UploadExec);
        permitBusiness = findViewById(R.id.ver_Permit);
        user_id  = getIntent().getStringExtra("user_id");
        Log.d(TAG, "onCreate: UserID "+user_id);
        forVerification.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("IDURL")||dataSnapshot.hasChild("selfieURL"))
                {
                    showLayout();
                    Log.d(TAG, "onDataChange: ID URRL"+dataSnapshot
                            .child("IDURL").getValue().toString());
                    toVerify.setText("Verify");
                    toRevoke.setText("Revoke");
                    String IDURL = dataSnapshot
                            .child("IDURL").getValue().toString();
                    Picasso.with(AdminVerification.this)
                            .load(IDURL)
                            .placeholder(R.drawable.no_image)
                            .into(ver_ID);
                    final ImagePopup imagePopup1 = new ImagePopup(AdminVerification.this);
                    imagePopup1.initiatePopupWithPicasso(IDURL);
                    imagePopup1.setBackgroundColor(Color.GREEN);
                    imagePopup1.animate();
                    imagePopup1.setFullScreen(true);

                            ver_ID.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagePopup1.viewPopup();
                        }
                    });

                    Log.d(TAG, "onDataChange: ID URRL"+dataSnapshot
                            .child("selfieURL").getValue().toString());
                    final String URL = dataSnapshot
                            .child("selfieURL").getValue().toString();

                    Picasso.with(AdminVerification.this)
                            .load(URL)
                            .placeholder(R.drawable.no_image)
                            .into(ver_Selfie);
                    final ImagePopup imagePopup = new ImagePopup(AdminVerification.this);
                    imagePopup.initiatePopupWithPicasso(URL);
                    imagePopup.setBackgroundColor(Color.GREEN);
                    imagePopup.animate();
                    imagePopup.setFullScreen(true);

                    ver_Selfie.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagePopup.viewPopup();
                        }
                    });
                    toVerify.setEnabled(true);
                    toRevoke.setEnabled(true);
                }else if(dataSnapshot.hasChild("Business_Permit")){
                    hideLayout();
                    toVerify.setText("Verify");
                    toRevoke.setText("Revoke");
                    String permit = dataSnapshot
                            .child("Business_Permit").getValue().toString();
                    Picasso.with(AdminVerification.this)
                            .load(permit)
                            .placeholder(R.drawable.no_image)
                            .into(permitBusiness);
                    final ImagePopup imagePopup1 = new ImagePopup(AdminVerification.this);
                    imagePopup1.initiatePopupWithPicasso(permit);
                    imagePopup1.setBackgroundColor(Color.GREEN);
                    imagePopup1.animate();
                    imagePopup1.setFullScreen(true);

                    permitBusiness.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            imagePopup1.viewPopup();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        toVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userDatabase.child(user_id).child("isVerified").setValue(true);
                forVerification.child(user_id).child("isVerified").setValue(true);
                startActivity(new Intent(AdminVerification.this,AdminActivity.class));
                finish();
            }
        });
        toRevoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }

    private void showLayout() {

        m1.setVisibility(View.GONE);
        l1.setVisibility(View.VISIBLE);
    }

    private void hideLayout() {

        m1.setVisibility(View.VISIBLE);
        l1.setVisibility(View.GONE);
    }

}
