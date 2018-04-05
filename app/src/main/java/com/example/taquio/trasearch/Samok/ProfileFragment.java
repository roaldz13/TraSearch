package com.example.taquio.trasearch.Samok;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.taquio.trasearch.Models.Like;
import com.example.taquio.trasearch.Models.Photo;
import com.example.taquio.trasearch.Models.User;
import com.example.taquio.trasearch.Models.UserSettings;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Utils.BottomNavigationViewHelper;
import com.example.taquio.trasearch.Utils.FirebaseMethods;
import com.example.taquio.trasearch.Utils.GridImageAdapter;
import com.example.taquio.trasearch.Utils.UniversalImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Edward on 16/02/2018.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private static final int ACTIVITY_NUM = 4;
    private static final int NUM_GRID_COLUMNS = 3;

    OnGridImageSelectedListener mOnGridImageSelectedListener;

    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase, mUserDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    private FirebaseUser mUser;
    //widgets
    private TextView mName, mEmail, mPhone;
    private ProgressBar mProgressBar;
    private CircleImageView mProfilePhoto;
    private GridView gridView;
    private Toolbar toolbar;
    private ImageView profileMenu, mBackArrow;
    private BottomNavigationViewEx bottomNavigationView;
    private Context mContext;
    private ImageView settings, mybookmarks, notVerified, isVerified;
    //vars
    private int mFollowersCount = 0;
    private int mFollowingCount = 0;
    private int mPostsCount = 0;
    Boolean verifier = false;
    private TextView notVerifiedLabel, verifiedLabel;
    ImageView verify, notVerify;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        Log.d(TAG, "onCreateView: STARTING PROFILE FRAGMENT >>>>>>>");


        verify = (ImageView) view.findViewById(R.id.imVerify);
        verifiedLabel = view.findViewById(R.id.verifiedLabel);

        notVerify = (ImageView) view.findViewById(R.id.imNotVerify);
        notVerifiedLabel = view.findViewById(R.id.notVerifiedLabel);


        mProfilePhoto = view.findViewById(R.id.myProfile_image);
        mName = view.findViewById(R.id.myProfile_name);
//        mEmail = view.findViewById(R.id.myProfile_email);
//        mPhone = view.findViewById(R.id.myProfile_phone);
//        mProgressBar = (ProgressBar) view.findViewById(R.id.profileProgressBar);
        gridView = view.findViewById(R.id.gridView);
        toolbar = view.findViewById(R.id.profileToolBar);
        profileMenu = view.findViewById(R.id.profileMenu);
        settings = view.findViewById(R.id.accSetting);
        mybookmarks = view.findViewById(R.id.savebookmarks);
        bottomNavigationView = view.findViewById(R.id.bottomNavViewBar);
        mBackArrow = view.findViewById(R.id.backArrow);

        mContext = getActivity();

        mFirebaseMethods = new FirebaseMethods(getActivity());

        setupBottomNavigationView();
//        setupToolbar();

        setupFirebaseAuth();
        setupGridView();

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back");
                if(getStringTag().equals("noAction")){
                    getActivity().getSupportFragmentManager().popBackStack();
                    ((MyProfileActivity)getActivity()).hideLayout();
                    startActivity(new Intent(getContext(),HomeActivity2.class));
                }
                if(getStringTag().equals("action")){
                    getActivity().getSupportFragmentManager().popBackStack();
                    getActivity().finish();
                }
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SettingsActivity.class));

            }
        });
        Button editProfile = view.findViewById(R.id.myProfile_editBtn);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating to " + mContext.getString(R.string.edit_profile_fragment));
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
//                intent.putExtra(getString(R.string.calling_activity), getString(R.string.profile_activity));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
        mybookmarks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, SaveItemActivity.class));
            }
        });
        return view;
    }
    private String getStringTag(){
        Bundle bundle = this.getArguments();
        if(bundle != null){
            return bundle.getString("Action");
        }else{
            return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        try{
            mOnGridImageSelectedListener = (OnGridImageSelectedListener) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage() );
        }
        super.onAttach(context);
    }
    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    private void setupGridView(){
        Log.d(TAG, "setupGridView: Setting up image grid.");

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
        reference1.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                verifier = dataSnapshot.child("isVerified").getValue(Boolean.class);

                if(verifier.equals(true)) {
                    verify.setVisibility(View.VISIBLE);
                    verifiedLabel.setVisibility(View.VISIBLE);

                    notVerify.setVisibility(View.GONE);
                    notVerifiedLabel.setVisibility(View.GONE);
                }else if (verifier.equals(false)) {
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

        final ArrayList<Photo> photos = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("Users_Photos")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){

                    Log.d(TAG, "onDataChange: tesst " + singleSnapshot.getValue());
                    Photo photo = new Photo();
                    Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();

                    try {
                        photo.setPhoto_description(objectMap.get("photo_description").toString());
                        photo.setQuantity(objectMap.get("quantity").toString());
                        photo.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
                        photo.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                        photo.setDate_created(Long.parseLong(objectMap.get(getString(R.string.field_date_created)).toString()));
                        photo.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());

                        photos.add(photo);
                    }catch(NullPointerException e){
                        Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage() );
                    }
                }

                //setup our image grid
                int gridWidth = getResources().getDisplayMetrics().widthPixels;
                int imageWidth = gridWidth/NUM_GRID_COLUMNS;
                gridView.setColumnWidth(imageWidth);

                ArrayList<String> imgUrls = new ArrayList<String>();
                for(int i = 0; i < photos.size(); i++){
                    imgUrls.add(photos.get(i).getImage_path());
                }
                GridImageAdapter adapter = new GridImageAdapter(getActivity(),R.layout.layout_grid_imageview,
                        "", imgUrls);
                gridView.setAdapter(adapter);

                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mOnGridImageSelectedListener.onGridImageSelected(photos.get(position), ACTIVITY_NUM);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void setProfileWidgets(final UserSettings userSettings) {

        User user = userSettings.getUser();
        UniversalImageLoader.setImage(user.getImage(), mProfilePhoto, null, "");
        mName.setText(user.getName());
//        mEmail.setText(user.getEmail());
//        mPhone.setText(user.getPhoneNumber());
//
//        verifier = user.getVerify().toString();
//
//        if(verifier.equals("true")) {
//            isVerified.setVisibility(View.VISIBLE);
//            notVerified.setVisibility(View.GONE);
//        }else if (verifier.equals("false")) {
//            isVerified.setVisibility(View.GONE);
//            notVerified.setVisibility(View.VISIBLE);
//        }
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext,getActivity() ,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");


//        mUser = FirebaseAuth.getInstance().getCurrentUser();
//        final String uid = mUser.getUid();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: GETTING DATA FROM DATABASE >>>>>>>>>");

                setProfileWidgets(mFirebaseMethods.getUserSettings(dataSnapshot));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public interface OnGridImageSelectedListener{
        void onGridImageSelected(Photo photo, int activityNumber);
    }


}