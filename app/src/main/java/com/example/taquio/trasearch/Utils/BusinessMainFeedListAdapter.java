package com.example.taquio.trasearch.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.BusinessHome.BusinessHome;
import com.example.taquio.trasearch.Models.Photo;
import com.example.taquio.trasearch.Models.User;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Messages.BusMessageActivity;
import com.example.taquio.trasearch.Samok.BusMyProfileActivity;
import com.example.taquio.trasearch.Samok.EditPostItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * Created by Edward 2018.
 */

public class BusinessMainFeedListAdapter extends ArrayAdapter<Photo> {


    private static final String TAG = "BusinessMainFeedListAda";
    OnLoadMoreItemsListener mOnLoadMoreItemsListener;
    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private DatabaseReference mReference;
    private String currentUsername = "";

    //firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    public BusinessMainFeedListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Photo> objects) {
        super(context, resource, objects);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLayoutResource = resource;
        this.mContext = context;
        currentUser = mAuth.getCurrentUser();
        mReference = FirebaseDatabase.getInstance().getReference();

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
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder holder;

        if(convertView == null){
            convertView = mInflater.inflate(mLayoutResource, parent, false);
            holder = new ViewHolder();

            holder.username = convertView.findViewById(R.id.username);
            holder.image = convertView.findViewById(R.id.post_image);
            holder.caption = convertView.findViewById(R.id.image_caption);
            holder.timeDetla = convertView.findViewById(R.id.image_time_posted);
            holder.likegreen = convertView.findViewById(R.id.image_heart_red);
            holder.likeblack = convertView.findViewById(R.id.image_heart);
            holder.likes = convertView.findViewById(R.id.image_likes);
            holder.ellipsis = convertView.findViewById(R.id.ivEllipses);
            holder.mprofileImage = convertView.findViewById(R.id.profile_photo);
            holder.dm =convertView.findViewById(R.id.direct_message);
            holder.bookmark =convertView.findViewById(R.id.bookmark);
            holder.messageLayout = convertView.findViewById(R.id.messageLayout);


            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.photo = getItem(position);
//        holder.detector = new GestureDetector(mContext, new GestureListener(holder));
        holder.users = new StringBuilder();
//        holder.liker = new Likes(holder.likeblack, holder.likegreen);
//        final String newLikeID = mReference.push().getKey();
//        final Like like = new Like();
//        like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

//
//        holder.likeblack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                holder.mProceslike =true;
//                mReference.child("Photos")
//                        .child(holder.photo.getPhoto_id())
//                        .child(mContext.getString(R.string.field_likes))
//                        .addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//                                if(holder.mProceslike){
//                                    if(dataSnapshot.child(newLikeID).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//                                        mReference.child("Photos")
//                                                .child(holder.photo.getPhoto_id())
//                                                .child(mContext.getString(R.string.field_likes))
//                                                .child(newLikeID)
//                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
//                                        holder.mProceslike = false;
//                                    }else{
//                                        mReference.child("Photos")
//                                                .child(holder.photo.getPhoto_id())
//                                                .child(mContext.getString(R.string.field_likes))
//                                                .child(newLikeID)
//                                                .setValue(like);
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//            }
//        });
//        holder.setPhotoLike(newLikeID, holder.photo, holder.likeblack);
        //get the current users username (need for checking likes string)
        getCurrentUsername();

        //get likes string
//        getLikesString(holder);

        //set the caption
        holder.caption.setText(getItem(position).getPhoto_description());

        //set the comment
//        List<Comment> comments = getItem(position).getComments();
//        holder.comments.setText("#" + comments.size());
//        holder.comments.setText("View all " + comments.size() + " comments");
//        holder.comments.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d(TAG, "onClick: loading comment thread for " + getItem(position).getPhoto_id());
//                ((HomeActivity2)mContext).onCommentThreadSelected(getItem(position),
//                        mContext.getString(R.string.home_activity));
////
////                //going to need to do something else?
//                ((HomeActivity2)mContext).hideLayout();
//                ((HomeActivity2) mContext).finish();
//            }
//        });


        //set the time it was posted
//        String timestampDifference = getTimestampDifference(getItem(position));
//        if(!timestampDifference.equals("0")){
//            holder.timeDetla.setText(timestampDifference + " DAYS AGO");
//        }else{
//            holder.timeDetla.setText("TODAY");
//        }
            holder.timeDetla.setText(getDate(holder.photo.getDate_createdLong(), "MMM dd, yyyy E hh:mm aa"));
//        if(holder.photo.getUser_id().equals(currentUser.getUid())){
//            holder.dm.setVisibility(View.GONE);
//            holder.dm.setEnabled(false);
//        }
        //set the profile image
//        final ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.displayImage(getItem(position).getImage_path(), holder.image);
        Picasso.with(getContext()).load(getItem(position).getImage_path())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.no_image)
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getContext())
                                .load(getItem(position).getImage_path())
                                .placeholder(R.drawable.no_image)
                                .into(holder.image);
                    }
                });

        //get the profile image and username
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("Users")
               .orderByChild("userID")
                .equalTo(getItem(position).getUser_id());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(final DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                    Log.d(TAG, "onDataChange: found user: "
                            + singleSnapshot.getValue(User.class).getName());

                    String name = singleSnapshot.getValue(User.class).getName();
                    String[] arname = name.split(" ") ;

                            holder.username.setText(arname[0]);
                    holder.username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "onClick: navigating to profile of: " +
                                    holder.user.getName());

                            Intent intent = new Intent(mContext, BusMyProfileActivity.class);
                            intent.putExtra(mContext.getString(R.string.calling_activity),
                                    mContext.getString(R.string.home_activity));
                            intent.putExtra(mContext.getString(R.string.intent_user), holder.user);
                            mContext.startActivity(intent);
                        }
                    });
                    holder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            
                            ((BusinessHome)mContext).onImageSelected(getItem(position),0, holder.photo.getUser_id());
                           //another thing?
                            ((BusinessHome)mContext).hideLayout();
                        }
                    });
//                    holder.bookmark.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            mReference.child("Bookmarks")
//                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .child(holder.photo.getPhoto_id())
//                                    .child(holder.photo.getUser_id())
//                                    .child("photo_post")
//                                    .setValue(holder.photo.getImage_path());
//
//                        }
//                    });
                    final ImageLoader imageLoader = ImageLoader.getInstance();
                    imageLoader.displayImage(singleSnapshot.getValue(User.class).getImage(),
                            holder.mprofileImage);

                    holder.mprofileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG,  "onClick: navigating to profile of: " +
                                    holder.user.getName());

                            Intent intent = new Intent(mContext, BusMyProfileActivity.class);
                            intent.putExtra(mContext.getString(R.string.calling_activity),
                                    mContext.getString(R.string.home_activity));
                            intent.putExtra(mContext.getString(R.string.intent_user), holder.user);
                            mContext.startActivity(intent);
                        }
                    });


                    holder.user = singleSnapshot.getValue(User.class);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //get the user object
        Query userQuery = mReference
                .child("Users")
               .orderByChild("userID")
                .equalTo(getItem(position).getUser_id());
        userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Log.d(TAG, "onDataChange: found user:  THISSSS" +
                    singleSnapshot.getValue(User.class).getName());

                    holder.user = singleSnapshot.getValue(User.class);
                    holder.dm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(mContext, BusMessageActivity.class);
                            i.putExtra("user_id", holder.photo.getUser_id());
                            i.putExtra("user_name", holder.user.getName());
                            mContext.startActivity(i);
                        }
                    });
                    holder.messageLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(mContext, BusMessageActivity.class);
                            i.putExtra("user_id", holder.photo.getUser_id());
                            i.putExtra("user_name", holder.user.getName());
                            mContext.startActivity(i);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        if(reachedEndOfList(position)){
            loadMoreData();
        }
        holder.ellipsis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayAlertDialog(holder);
            }
        });

        return convertView;
    }
    private void displayAlertDialog(final ViewHolder holder) {

        if(holder.photo.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

            builder.setItems(new CharSequence[]
                            {"Update"},
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // The 'which' argument contains the index position
                            // of the selected item
                            switch (which) {
                                case 0:
                                    Intent i = new Intent(getContext(), EditPostItem.class);
                                    i.putExtra("user", holder.user);
                                    i.putExtra("photo", holder.photo);
                                    getContext().startActivity(i);
                                    break;
                            }
                        }
                    });
            builder.create().show();
        }else{
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
                                    Toast.makeText(mContext, "CLICK!", Toast.LENGTH_SHORT).show();
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
                                            mReference.child("Reports")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .child(holder.photo.getUser_id())
                                                    .child("report_message")
                                                    .setValue(userInput.getText().toString());

                                            mReference.child("Reports")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .child(holder.photo.getUser_id())
                                                    .child("postID")
                                                    .setValue(holder.photo.getPhoto_id());
                                            Toast.makeText(mContext, "Reported", Toast.LENGTH_SHORT).show();
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

    }
    private boolean reachedEndOfList(int position){
        return position == getCount() - 1;
    }

    private void loadMoreData(){

        try{
            mOnLoadMoreItemsListener = (OnLoadMoreItemsListener) getContext();
        }catch (ClassCastException e){
            Log.e(TAG, "loadMoreData: ClassCastException: " +e.getMessage() );
        }

        try{
            mOnLoadMoreItemsListener.onLoadMoreItems();
        }catch (NullPointerException e){
            Log.e(TAG, "loadMoreData: ClassCastException: " +e.getMessage() );
        }
    }

//    private void addNewLike(final ViewHolder holder){
//        Log.d(TAG, "addNewLike: adding new like");
//
//        String newLikeID = mReference.push().getKey();
//        Like like = new Like();
//        like.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//        mReference.child("Photos")
//                .child(holder.photo.getPhoto_id())
//                .child(mContext.getString(R.string.field_likes))
//                .child(newLikeID)
//                .setValue(like);
//
//        mReference.child("Users_Photos")
//                .child(holder.photo.getUser_id())
//                .child(holder.photo.getPhoto_id())
//                .child(mContext.getString(R.string.field_likes))
//                .child(newLikeID)
//                .setValue(like);
//        mReference.child("Likes")
//                .child(holder.photo.getUser_id())
//                .child(newLikeID)
//                .child(holder.photo.getPhoto_id())
//                .child("user_id")
//                .setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
//
//
//        holder.liker.toggleLike();
////       `
//    }

    private void getCurrentUsername(){
        Log.d(TAG, "getCurrentUsername: retrieving user account settings");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query = reference
                .child("Users")
                .orderByChild("userID")
//                .orderByKey()
                .equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    currentUsername = singleSnapshot.getValue(User.class).getName();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

//    private void getLikesString(final ViewHolder holder){
//        Log.d(TAG, "getLikesString: getting likes string");
//
//        Log.d(TAG, "getLikesString: photo id: " + holder.photo.getPhoto_id());
//        try{
//        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//        Query query = reference
//                .child("Photos")
//                .child(holder.photo.getPhoto_id())
//                .child(mContext.getString(R.string.field_likes));
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                holder.users = new StringBuilder();
//                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//
//                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//                    Query query = reference
//                            .child("Users")
//                            .orderByKey()
//                            .equalTo(singleSnapshot.getValue(Like.class).getUser_id());
//                    query.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//                                Log.d(TAG, "onDataChange: found like: " +
//                                        singleSnapshot.getValue(User.class).getUserName());
//
//                                holder.users.append(singleSnapshot.getValue(User.class).getUserName());
//                                holder.users.append(",");
//                            }
//
//                            String[] splitUsers = holder.users.toString().split(",");
//
//                            //ed, edward
//                            holder.likeByCurrentUser = holder.users.toString().contains(currentUsername + ",");
//
//                            int length = splitUsers.length;
//
//                            holder.likesString = length + " interested!";

//                                if(length == 1){
//                                    holder.likesString = "Liked by " + splitUsers[0];
//                                }
//                                else if(length == 2){
//                                    holder.likesString = "Liked by " + splitUsers[0]
//                                            + " and " + splitUsers[1];
//                                }
//                                else if(length == 3){
//                                    holder.likesString = "Liked by " + splitUsers[0]
//                                            + ", " + splitUsers[1]
//                                            + " and " + splitUsers[2];
//
//                                }
//                                else if(length == 4){
//                                    holder.likesString = "Liked by " + splitUsers[0]
//                                            + ", " + splitUsers[1]
//                                            + ", " + splitUsers[2]
//                                            + " and " + splitUsers[3];
//                                }
//                                else if(length > 4){
//                                    holder.likesString = "Liked by " + splitUsers[0]
//                                            + ", " + splitUsers[1]
//                                            + ", " + splitUsers[2]
//                                            + " and " + (splitUsers.length - 3) + " others";
//                                }
//                            Log.d(TAG, "onDataChange: likes string: " + holder.likesString);
//                            //setup likes string
//                            setupLikesString(holder, holder.likesString);
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//                }
//                if(!dataSnapshot.exists()){
//                    holder.likesString = "";
//                    holder.likeByCurrentUser = false;
//                    //setup likes string
//                    setupLikesString(holder, holder.likesString);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        }catch (NullPointerException e){
//            Log.e(TAG, "getLikesString: NullPointerException: " + e.getMessage() );
//            holder.likesString = "";
//            holder.likeByCurrentUser = false;
//            //setup likes string
//            setupLikesString(holder, holder.likesString);
//
//        }
//    }


//    private void setupLikesString(final ViewHolder holder, String likesString){
//        Log.d(TAG, "setupLikesString: likes string:" + holder.likesString);
//
//        Log.d(TAG, "setupLikesString: photo id: " + holder.photo.getPhoto_id());
//        if(holder.likeByCurrentUser){
//            Log.d(TAG, "setupLikesString: photo is liked by current user");
//            holder.likeblack.setVisibility(View.GONE);
//            holder.likegreen.setVisibility(View.VISIBLE);
//            holder.likegreen.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return holder.detector.onTouchEvent(event);
//                }
//            });
//        }else{
//            Log.d(TAG, "setupLikesString: photo is not liked by current user");
//            holder.likeblack.setVisibility(View.VISIBLE);
//            holder.likegreen.setVisibility(View.GONE);
//            holder.likeblack.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return holder.detector.onTouchEvent(event);
//                }
//            });
//        }
//        holder.likes.setText(likesString);
//    }

    /**
     * Returns a string representing the number of days ago the post was made
     * @return
     */
    private String getTimestampDifference(Photo photo){
        Log.d(TAG, "getTimestampDifference: getting timestamp difference.");

        String difference = "";
//        Calendar c = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.CANADA);
//        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));//google 'android list of timezones'
//        Date today = c.getTime();
//        sdf.format(today);
//        Date timestamp;

        final Long photoTimestamp = photo.getDate_createdLong();
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sfd.format(new Date(photoTimestamp));
//        try{
//            timestamp = sdf.parse(photoTimestamp);
//            difference = String.valueOf(Math.round(((today.getTime() - timestamp.getTime()) / 1000 / 60 / 60 / 24 )));
//        }catch (ParseException e){
//            Log.e(TAG, "getTimestampDifference: ParseException: " + e.getMessage() );
//            difference = "0";
//        }
//        Long tsLong = System.currentTimeMillis()/1000;
//        difference = tsLong.toString();
        return difference;
    }

    public interface OnLoadMoreItemsListener{
        void onLoadMoreItems();
    }


     class ViewHolder{
        CircleImageView mprofileImage;
        String likesString;
        TextView username, timeDetla, caption, likes;
        SquareImageView image;
        ImageView likegreen, likeblack, comment, dm, ellipsis, bookmark;
        User user  = new User();
        StringBuilder users;
        String mLikesString;
        boolean likeByCurrentUser;
        Likes liker;
        GestureDetector detector;
        Photo photo;
        Boolean mProceslike = false;
        RelativeLayout messageLayout;


//        public void setPhotoLike(final String postKey, Photo photo, final ImageView likeblack){
//            mReference.child("Photos")
//                    .child(photo.getPhoto_id())
//                    .child(mContext.getString(R.string.field_likes))
//                    .addValueEventListener(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                if(dataSnapshot.child(postKey).hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())){
//                                        likeblack.setImageResource(R.drawable.likegreen);
//                                }else{
//                                    likeblack.setImageResource(R.drawable.likewhite);
//                                }
//
//                        }
//
//                        @Override
//                        public void onCancelled(DatabaseError databaseError) {
//
//                        }
//                    });
//        }
    }

//    public class GestureListener extends GestureDetector.SimpleOnGestureListener{
//
//        ViewHolder mHolder;
//        public GestureListener(ViewHolder holder) {
//            mHolder = holder;
//        }
//
//        @Override
//        public boolean onDown(MotionEvent e) {
//            return true;
//        }
//
//        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//
//            Log.d(TAG, "SingleTapDetected: clicked on photo: " + mHolder.photo.getPhoto_id());
//
//            DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
//            Query query = reference
//                    .child("Photos")
//                    .child(mHolder.photo.getPhoto_id())
//                    .child(mContext.getString(R.string.field_likes));
//            query.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
//
//                        String keyID = singleSnapshot.getKey();
//
//                        //case1: Then user already liked the photo
//                        if(mHolder.likeByCurrentUser
////                                && singleSnapshot.getValue(Like.class).getUser_id()
////                                        .equals(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                ){
//
//                            mReference.child("Photos")
//                                    .child(mHolder.photo.getPhoto_id())
//                                    .child(mContext.getString(R.string.field_likes))
//                                    .child(keyID)
//                                    .removeValue();
//                            mReference.child("Users_Photos")
////                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .child(mHolder.photo.getUser_id())
//                                    .child(mHolder.photo.getPhoto_id())
//                                    .child(mContext.getString(R.string.field_likes))
//                                    .child(keyID)
//                                    .removeValue();
//                            mReference.child("Likes")
//                                    .child(mHolder.photo.getUser_id())
//                                    .child(keyID)
//                                    .removeValue();
//                            mReference.child("Bookmarks")
//                                    .child(mHolder.photo.getUser_id())
//                                    .child(mHolder.photo.getPhoto_id())
//                                    .child("photo_post")
//                                    .removeValue();
//
//                            mHolder.liker.toggleLike();
//                            getLikesString(mHolder);
//                        }
//                        //case2: The user has not liked the photo
//                        else if(!mHolder.likeByCurrentUser){
//                            //add new like
//                            addNewLike(mHolder);
//                            break;
//                        }
//                    }
//                    if(!dataSnapshot.exists()){
//                        //add new like
//                        addNewLike(mHolder);
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//
//                }
//            });
//
//            return true;
//        }
//    }

}





























