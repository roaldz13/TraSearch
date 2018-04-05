package com.example.taquio.trasearch.Utils;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.BusinessHome.BusinessHome;
import com.example.taquio.trasearch.Models.Photo;
import com.example.taquio.trasearch.Models.Report;
import com.example.taquio.trasearch.Models.User;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Messages.BusMessageActivity;
import com.example.taquio.trasearch.Samok.BusMyProfileActivity;
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
 * Created by Edward on 24/02/2018.
 */

public class BusOtherUserViewPost extends Fragment {

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
    private int mActivityNumber = 0;
    private String photoUsername = "";
    private String profilePhotoUrl = "";
    private GestureDetector mGestureDetector;
    private Likes mHeart;
    private Boolean mLikedByCurrentUser;
    private StringBuilder mUsers;
    private String mLikesString = "";
    private User mCurrentUser;
    private RelativeLayout mlayout;
    public BusOtherUserViewPost(){
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bus_otherview_post, container, false);
        mPostImage = view.findViewById(R.id.post_image);
        bottomNavigationView = view.findViewById(R.id.businessBottomNavViewBar);
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
//        mBookmark = view.findViewById(R.id.bookmark);
        mlayout = view.findViewById(R.id.messageLayout);
        dm = view.findViewById(R.id.direct_message);
//        mComment = view.findViewById(R.id.speech_bubble);
//        mComments = view.findViewById(R.id.image_comments_link);

//        mHeart = new Likes(mHeartWhite, mHeartRed);
//        mGestureDetector = new GestureDetector(getActivity(), new OtherUserViewPost.GestureListener());

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
                        {"Report"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch (which) {
                            case 0:
                                LayoutInflater li = LayoutInflater.from(getContext());
                                View promptView = li.inflate(R.layout.item_dialog, null);
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setView(promptView);
                                final EditText userInput = (EditText) promptView.findViewById(R.id.dialogDesc);
                                builder.setCancelable(false);
                                builder.setPositiveButton("Send", new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                        String report_id = myRef.push().getKey();
                                        Report report = new Report(userInput.getText().toString(),report_id, mPhoto.getPhoto_id());

//                                        myRef.child("Reports")
//                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                                .setValue(report);
//                                        Toast.makeText(getContext(), "Reported", Toast.LENGTH_LONG).show();
                                        myRef.child("Reports")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child(mPhoto.getUser_id())
                                                .child("report_message")
                                                .setValue(userInput.getText().toString());

                                        myRef.child("Reports")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .child(mPhoto.getUser_id())
                                                .child("postID")
                                                .setValue(mPhoto.getPhoto_id());
                                        Toast.makeText(getContext(), "Reported", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.dismiss();
                                    }
                                });
                                builder.create().show();
                                break;
                        }
                    }
                });
        builder.create().show();
    }
    private void init(){
        try{
            UniversalImageLoader.setImage(getPhotoFromBundle().getImage_path(), mPostImage, null, "");
            Log.d(TAG, "init: GETTING BUNDLE >>>>>>>>>>>>> " +getPhotoFromBundle().getImage_path() );
            mPhoto = getPhotoFromBundle();
            getPhotoDetails();
//            mActivityNumber = getActivityNumFromBundle();
//            Toast.makeText(getContext(), "ARAA AYY"+ mActivityNumber, Toast.LENGTH_SHORT).show();
//            String photo_id = getPhotoFromBundle().getPhoto_id();
//
//            Query query = FirebaseDatabase.getInstance().getReference()
//                    .child("Photos")
//                    .orderByChild(getString(R.string.field_photo_id))
//                    .equalTo(photo_id);
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//                        Photo newPhoto = new Photo();
//                        Map<String, Object> objectMap = (HashMap<String, Object>) singleSnapshot.getValue();
//
//                        newPhoto.setPhoto_description(objectMap.get(getString(R.string.field_caption)).toString());
//                        newPhoto.setQuantity(objectMap.get(getString(R.string.field_tags)).toString());
//                        newPhoto.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
//                        newPhoto.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
//                        newPhoto.setDate_created(Long.parseLong(objectMap.get(getString(R.string.field_date_created)).toString()));
//                        newPhoto.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());
//
//                        List<Comment> commentsList = new ArrayList<Comment>();
//                        for (DataSnapshot dSnapshot : singleSnapshot
//                                .child(getString(R.string.field_comments)).getChildren()){
//                            Comment comment = new Comment();
//                            comment.setUser_id(dSnapshot.getValue(Comment.class).getUser_id());
//                            comment.setComment(dSnapshot.getValue(Comment.class).getComment());
//                            comment.setDate_created(dSnapshot.getValue(Comment.class).getDate_created());
//                            commentsList.add(comment);
//                        }
//                        newPhoto.setComments(commentsList);
//
//                        mPhoto = newPhoto;
//
//                        getCurrentUser();
//                        getPhotoDetails();
//                        //getLikesString();
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

//    private void getLikesString(){
//        Log.d(TAG, "getLikesString: getting likes string");
//
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference
//                .child("Photos")
//                .child(mPhoto.getPhoto_id())
//                .child(getString(R.string.field_likes));
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                mUsers = new StringBuilder();
//                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//
//                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                    Query query = reference
//                            .child("Users")
//                            .orderByChild("userID")
//                            .equalTo(singleSnapshot.getValue(Like.class).getUser_id());
//                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//                                Log.d(TAG, "onDataChange: found like: " +
//                                        singleSnapshot.getValue(User.class).getUserName());
//
//                                mUsers.append(singleSnapshot.getValue(User.class).getUserName());
//                                mUsers.append(",");
//                            }
//
//                            String[] splitUsers = mUsers.toString().split(",");
//
//                            //mitch, mitchell.tabian
//                            mLikedByCurrentUser = mUsers.toString().contains(mCurrentUser.getUserName() + ",");
//
//                            int length = splitUsers.length;
//                            mLikesString = ""+length + " interested!";
////                            if(length == 1){
////                                mLikesString = "Liked by " + splitUsers[0];
////                            }
////                            else if(length == 2){
////                                mLikesString = "Liked by " + splitUsers[0]
////                                        + " and " + splitUsers[1];
////                            }
////                            else if(length == 3){
////                                mLikesString = "Liked by " + splitUsers[0]
////                                        + ", " + splitUsers[1]
////                                        + " and " + splitUsers[2];
////
////                            }
////                            else if(length == 4){
////                                mLikesString = "Liked by " + splitUsers[0]
////                                        + ", " + splitUsers[1]
////                                        + ", " + splitUsers[2]
////                                        + " and " + splitUsers[3];
////                            }
////                            else if(length > 4){
////                                mLikesString = "Liked by " + splitUsers[0]
////                                        + ", " + splitUsers[1]
////                                        + ", " + splitUsers[2]
////                                        + " and " + (splitUsers.length - 3) + " others";
////                            }
//                            Log.d(TAG, "onDataChange: likes string: " + mLikesString);
//                            setupWidgets();
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//                if(!dataSnapshot.exists()){
//                    mLikesString = "";
//                    mLikedByCurrentUser = false;
//                    setupWidgets();
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    private void getCurrentUser(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("Users")
                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
                    mCurrentUser = singleSnapshot.getValue(User.class);
                }
//                getLikesString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: query cancelled.");
            }
        });
    }

    private void getPhotoDetails(){
        Log.d(TAG, "getPhotoDetails: retrieving photo details.");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("Users")
                .orderByChild("userID")
                .equalTo(mPhoto.getUser_id());
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
        mTimestamp.setText(getDate(mPhoto.getDate_createdLong(), "MMM dd, yyyy E hh:mm aa"));
        UniversalImageLoader.setImage(mCurrentUser.getImage(), mProfileImage, null, "");
        mUsername.setText(mCurrentUser.getName());
//        mLikes.setText(mLikesString);
        mCaption.setText(mPhoto.getPhoto_description());
        mItem.setText(mPhoto.getQuantity());

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
                            Intent intent = new Intent(getActivity(), BusMyProfileActivity.class);
                            intent.putExtra(getActivity().getString(R.string.calling_activity),
                                    getActivity().getString(R.string.home_activity));
                            intent.putExtra(getActivity().getString(R.string.intent_user), user);
                            getActivity().startActivity(intent);
                        }
                    });
                    mUsername.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getActivity(), BusMyProfileActivity.class);
                            intent.putExtra(getActivity().getString(R.string.calling_activity),
                                    getActivity().getString(R.string.home_activity));
                            intent.putExtra(getActivity().getString(R.string.intent_user), user);
                            getActivity().startActivity(intent);
                        }
                    });
                    dm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getContext(), BusMessageActivity.class);
                            i.putExtra("user_id", mPhoto.getUser_id());
                            i.putExtra("user_name", user.getName());
                            getContext().startActivity(i);
                        }
                    });
                    mlayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(getContext(), BusMessageActivity.class);
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
                if(getTheTag().equals("fromProfile")){
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                if(getTheTag().equals("fromHome")){
                    getActivity().getSupportFragmentManager().popBackStack();
                    ((BusinessHome) getActivity()).showLayout();
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
            Toast.makeText(getContext(), "HEREEE >>>>> "+bundle.getInt(getString(R.string.activity_number)), Toast.LENGTH_SHORT).show();
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
        BusinessBottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BusinessBottomNavigationViewHelper.enableNavigation(getActivity(),getActivity() ,bottomNavigationView);
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

       /*
    ------------------------------------ Firebase ---------------------------------------------
     */

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

}
