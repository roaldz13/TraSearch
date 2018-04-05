package com.example.taquio.trasearch.Samok;


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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.Messages.MessageActivity;
import com.example.taquio.trasearch.Models.Photo;
import com.example.taquio.trasearch.Models.User;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Utils.BottomNavigationViewHelper;
import com.example.taquio.trasearch.Utils.FirebaseMethods;
import com.example.taquio.trasearch.Utils.Likes;
import com.example.taquio.trasearch.Utils.SquareImageView;
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
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Edward 2018.
 */

public class BusViewPostFragment extends Fragment{

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
    private User mCurrentUser, thisUser;
    private Context mContext = getActivity();
    private RelativeLayout mlayout;
    public BusViewPostFragment(){
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busview_post, container, false);

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
//        mHeart = new Likes(mHeartWhite, mHeartRed);
//        mGestureDetector = new GestureDetector(getActivity(), new GestureListener());

        mEllipses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                displayAlertDialog();
            }
        });
        setupFirebaseAuth();
        setupBottomNavigationView();

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
                                                .equalTo(mPhoto.getUser_id());
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                                            thisUser = singleSnapshot.getValue(User.class);

                                            Intent i = new Intent(getContext(), EditPostItem.class);
                                            i.putExtra("user", thisUser);
                                            i.putExtra("photo", mPhoto);
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
            Log.d(TAG, "init: GETTING BUNDLE >>>>>>>>>>>>> " +getPhotoFromBundle().getImage_path() );
            mPhoto = getPhotoFromBundle();
            getPhotoDetails();
//            mActivityNumber = getActivityNumFromBundle();
//            Toast.makeText(getContext(), "ARAA AYY"+ mActivityNumber, Toast.LENGTH_SHORT).show();
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


//    private void getCurrentUser(){
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference
//                .child("Users")
//                .orderByKey()
//                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for ( DataSnapshot singleSnapshot :  dataSnapshot.getChildren()){
//                    mCurrentUser = singleSnapshot.getValue(User.class);
//                }
////                getLikesString();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Log.d(TAG, "onCancelled: query cancelled.");
//            }
//        });
//    }

//    private void addNewLike(){
//        Log.d(TAG, "addNewLike: adding new like");
//
//        String newLikeID = myRef.push().getKey();
//        Like like = new Like();
//        like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//        myRef.child("Photos")
//                .child(mPhoto.getPhoto_id())
//                .child(getString(R.string.field_likes))
//                .child(newLikeID)
//                .setValue(like);
//
//        myRef.child("Users_Photos")
//                .child(mPhoto.getUser_id())
//                .child(mPhoto.getPhoto_id())
//                .child(getString(R.string.field_likes))
//                .child(newLikeID)
//                .setValue(like);
//        myRef.child("Likes")
//                .child(mPhoto.getUser_id())
//                .child(newLikeID)
//                .child(mPhoto.getPhoto_id())
//                .child("user_id")
//                .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//
//        mHeart.toggleLike();
//        getLikesString();
//    }

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
//
        UniversalImageLoader.setImage(mCurrentUser.getImage(), mProfileImage, null, "");
        String name = mCurrentUser.getName();
        String[] arname = name.split(" ") ;
            mUsername.setText(arname[0]);
//            mLikes.setText(mLikesString);
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
                    String name = user.getName();
                    String[] arname = name.split(" ") ;
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
                            Intent i = new Intent(getContext(), MessageActivity.class);
                            i.putExtra("user_id", mPhoto.getUser_id());
                            i.putExtra("user_name", user.getName());
                            getContext().startActivity(i);
                        }
                    });
                    mlayout.setOnClickListener(new View.OnClickListener() {
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