package com.example.taquio.trasearch.Utils;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.Models.Bookmark;
import com.example.taquio.trasearch.Samok.EditPostItem;
import com.example.taquio.trasearch.Models.Photo;
import com.example.taquio.trasearch.Models.User;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Samok.HomeActivity2;
import com.example.taquio.trasearch.Messages.MessageActivity;
import com.example.taquio.trasearch.Samok.MyProfileActivity;
import com.example.taquio.trasearch.Samok.SaveItemActivity;
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
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Edward 2018.
 */

public class ViewPostFragment extends Fragment{

    private static final String TAG = "ViewPostFragment";
    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseMethods mFirebaseMethods;
    //widgets
    private SquareImageView mPostImage;
    private BottomNavigationViewEx bottomNavigationView;
    private TextView mBackLabel, mCaption, mUsername, mTimestamp, mLikes, mItem;
    private ImageView mBackArrow, mEllipses, mHeartRed, mHeartWhite, mProfileImage, mBookmark, dm;
    //vars
    private Photo mPhoto;
    private boolean isBookmark = false;
    private int mActivityNumber = 0;
    private String photoUsername = "";
    private String profilePhotoUrl = "";
    private GestureDetector mGestureDetector;
    private Likes mHeart;
    private Boolean mLikedByCurrentUser;
    private StringBuilder mUsers;
    private String mLikesString = "";
    private User mCurrentUser, thisUser;
    Boolean saveLogic = false;
    private Context mContext = getActivity();
    public ViewPostFragment(){
        super();
        setArguments(new Bundle());
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_post, container, false);

        mPostImage = view.findViewById(R.id.post_image);
        bottomNavigationView = view.findViewById(R.id.bottomNavViewBar);
        mBackArrow = view.findViewById(R.id.backArrow);
        mBackLabel = view.findViewById(R.id.tvBackLabel);
        mCaption = view.findViewById(R.id.image_caption);
        mUsername = view.findViewById(R.id.username);
        mTimestamp = view.findViewById(R.id.image_time_posted);
        mEllipses = view.findViewById(R.id.ivEllipses);
        mHeartRed = view.findViewById(R.id.image_heart_red);
        mHeartWhite = view.findViewById(R.id.image_heart);
        mProfileImage = view.findViewById(R.id.profile_photo);
        mLikes = view.findViewById(R.id.image_likes);
        mItem = view.findViewById(R.id.item_quantity);
        mBookmark = view.findViewById(R.id.bookmark);
        dm = view.findViewById(R.id.direct_message);

//        mHeart = new Likes(mHeartWhite, mHeartRed);
//        mGestureDetector = new GestureDetector(getActivity(), new GestureListener());


        setupFirebaseAuth();
        setupBottomNavigationView();

        mEllipses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayAlertDialog();
            }
        });
        return view;
    }
    private void displayAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setTitle("CHOOSE AN ACTION");
        builder.setItems(new CharSequence[]
                        {"Update"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                Query query1 = myRef.child("Users")
                                                .orderByChild("userID")
                                                .equalTo(getPhotoFromBundle().getUser_id());
//                                                .equalTo(mPhoto.getUser_id());
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                                            thisUser = singleSnapshot.getValue(User.class);

                                            Intent i = new Intent(getContext(), EditPostItem.class);
                                            i.putExtra("user", thisUser);
                                            i.putExtra("photo", getPhotoFromBundle());
                                            getContext().startActivity(i);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                break;
                        }
                    }
                });
        builder.create().show();
    }
    private void init(){
        try{
            //mPhoto = getPhotoFromBundle();

            UniversalImageLoader.setImage(getPhotoFromBundle().getImage_path(), mPostImage, null, "");
            mPhoto = getPhotoFromBundle();
            getPhotoDetails();
//            Log.d(TAG, "init: GETTING BUNDLE >>>>>>>>>>>>> " +getPhotoFromBundle().getImage_path() );
//
////            mActivityNumber = getActivityNumFromBundle();();
//            String photo_id = getPhotoFromBundle().getPhoto_id();
//
//            Query query = FirebaseDatabase.getInstance().getReference()
//                    .child("Photos")
//                    .orderByKey()
//                    .equalTo(photo_id);
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//                        Photo newPhoto = new Photo();
//                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
//                        Log.d(TAG, "init: GETTING >> " +singleSnapshot.getValue());
//                        newPhoto.setPhoto_description(objectMap.get(getString(R.string.field_caption)).toString());
//                        newPhoto.setQuantity(objectMap.get(getString(R.string.field_tags)).toString());
//                        newPhoto.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
//                        newPhoto.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
//                        newPhoto.setDate_created(Long.parseLong(objectMap.get(getString(R.string.field_date_created)).toString()));
//                        newPhoto.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());
//
//                        mPhoto = newPhoto;
//
//                        getCurrentUser();
//                        getPhotoDetails();
//
//                    }
//
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    Log.d(TAG, "onCancelled: query cancelled.");
//                }
//            });

        }catch (NullPointerException e){
            Log.e(TAG, "onCreateView: NullPointerException: " + e.getMessage() );
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isAdded()){
            init();
        }
    }

    private void getPhotoDetails(){
        Log.d(TAG, "getPhotoDetails: retrieving photo details.");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("Users")
                .orderByChild("userID")
                .equalTo(getPhotoFromBundle().getUser_id());
//                .equalTo(mPhoto.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    mCurrentUser = singleSnapshot.getValue(User.class);
                }
                setupWidgets();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
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
    private void setupWidgets(){
        mTimestamp.setText(getDate(getPhotoFromBundle().getDate_createdLong(), "MMM dd, yyyy E hh:mm aa"));
        UniversalImageLoader.setImage(mCurrentUser.getImage(), mProfileImage, null, "");
        mUsername.setText(mCurrentUser.getName());
        mCaption.setText(getPhotoFromBundle().getPhoto_description());
        mItem.setText(getPhotoFromBundle().getQuantity());
        mBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLogic = true;
                Log.d(TAG, "onClick: BookMark Clicked");
//                Toast.makeText(mContext, "Bookmarked", Toast.LENGTH_SHORT).show();
//                if (saveLogic){
//                    Toast.makeText(mContext, "Bookmarked 2", Toast.LENGTH_SHORT).show();
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                    Toast.makeText(getContext(), "Datasnapshot " + dataSnapshot.getValue(), Toast.LENGTH_LONG).show();

                        if (!dataSnapshot.child("Bookmarks").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            Log.d(TAG, "onDataChange: Bookmarked no exist");
                            myRef.child("Bookmarks")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(mPhoto.getPhoto_id())
                                    .setValue("photoID");
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                            saveLogic = true;
                        }
                        else if(!dataSnapshot.child("Bookmarks").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild(mPhoto.getPhoto_id()))
                        {
                            Log.d(TAG, "onDataChange: Bookmarked no exist");
                            myRef.child("Bookmarks")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(mPhoto.getPhoto_id())
                                    .setValue("photoID");
                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                            saveLogic = true;
                        }
                        else
                        {
                            saveLogic = false;
                            Log.d(TAG, "onDataChange: Bookmark Exist");
                            for (DataSnapshot snapshot : dataSnapshot.child("Bookmarks").getChildren()) {
                                Log.d(TAG, "snap: " + snapshot.getKey());
                                if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(snapshot.getKey())) {
                                    Log.d(TAG, "comparator: " + snapshot.child(snapshot.getKey()).hasChild(mPhoto.getPhoto_id()));
                                    Log.d(TAG, "kompara: " + snapshot.child(snapshot.getKey()));
                                    Log.d(TAG, "tocompare: " + snapshot.child(snapshot.getKey()).child(mPhoto.getPhoto_id()).getKey().equals(mPhoto.getPhoto_id()));

                                    for (DataSnapshot sp : snapshot.getChildren()) {
                                        Log.d(TAG, "child: " + sp.getKey());
                                        if (sp.getKey().equals(mPhoto.getPhoto_id())) {
                                            Log.d(TAG, "removemark: " + mPhoto.getPhoto_id());

                                            isBookmark = true;
                                            myRef.child("Bookmarks")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .child(mPhoto.getPhoto_id())

                                                    .removeValue();
                                            Toast.makeText(getContext(), "Unsave", Toast.LENGTH_SHORT).show();
                                            saveLogic =false;
                                        }

                                    }
                                    if (!isBookmark) {
                                        Log.d(TAG, "addmark: " + mPhoto.getPhoto_id());
                                        myRef.child("Bookmarks")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child(mPhoto.getPhoto_id())
                                                .setValue("photoID");
                                        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                        saveLogic = false;
                                    }
                                }
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
//                }


            }
        });
        Query query = myRef.child("Users")
                .orderByChild("userID")
                .equalTo(mPhoto.getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {
                    final User user = singleSnapshot.getValue(User.class);

                    mProfileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Intent intent = new Intent(getActivity(), MyProfileActivity.class);
//                            intent.putExtra(getActivity().getString(R.string.calling_activity),
//                                    getActivity().getString(R.string.home_activity));
//                            intent.putExtra(getActivity().getString(R.string.intent_user), user);
//                            getActivity().startActivity(intent);
                            Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                            intent.putExtra("calling_your_own",
                                    getActivity().getString(R.string.home_activity));
                            Log.d(TAG, "onClick: Calling your OWN");
                            intent.putExtra(getActivity().getString(R.string.intent_user),user);
                            getActivity().startActivity(intent);
                        }
                    });
                    mUsername.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                            Intent intent = new Intent(getActivity(), MyProfileActivity.class);
//                            intent.putExtra(getActivity().getString(R.string.calling_activity),
//                                    getActivity().getString(R.string.home_activity));
//                            intent.putExtra(getActivity().getString(R.string.intent_user), user);
//                            getActivity().startActivity(intent);
                            Intent intent = new Intent(getActivity(), MyProfileActivity.class);
                            intent.putExtra("calling_your_own",
                                    getActivity().getString(R.string.home_activity));
                            Log.d(TAG, "onClick: Calling your OWN");
                            intent.putExtra(getActivity().getString(R.string.intent_user),user);
                            getActivity().startActivity(intent);
                        }
                    });
                    dm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getContext(), MessageActivity.class);
                            i.putExtra("user_id", mPhoto.getUser_id());
                            i.putExtra("user_name", user.getName());
                            getContext().startActivity(i);
                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: navigating back");
                if(getTheTag().equals("fromBookmark")){
                    getActivity().getSupportFragmentManager().popBackStack();
                    ((SaveItemActivity)getActivity()).showLayoutSave();
                }
                if(getTheTag().equals("fromProfile")){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                if(getTheTag().equals("fromHome")){
                    getActivity().getSupportFragmentManager().popBackStack();
                    ((HomeActivity2) getActivity()).showLayout();
                }
            }
        });



    }
    private String getTheTag(){
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            return bundle.getString("theCall");

        }else{
            return "";
        }
    }
    /**
     * retrieve the activity number from the incoming bundle from profileActivity interface
     * @return
     */

    private int getActivityNumFromBundle(){
        Log.d(TAG, "getActivityNumFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            return bundle.getInt(getString(R.string.activity_number));

        }else{
            return 0;
        }
    }

    /**
     * retrieve the photo from the incoming bundle from profileActivity interface
     * @return
     */
    private Photo getPhotoFromBundle(){
        Log.d(TAG, "getPhotoFromBundle: arguments: " + getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            return bundle.getParcelable(getString(R.string.photo));
        }else{
            return null;
        }
    }

    /**
     * BottomNavigationView setup
     */
    private void setupBottomNavigationView(){
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(getActivity(),getActivity() ,bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(mActivityNumber);
        menuItem.setChecked(true);
    }

    /**
     * Setup the firebase auth object
     */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth.");
        mActivityNumber = getActivityNumFromBundle();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };


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

}