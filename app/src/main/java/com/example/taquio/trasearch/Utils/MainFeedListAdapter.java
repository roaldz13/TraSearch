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

import com.example.taquio.trasearch.Samok.EditPostItem;
import com.example.taquio.trasearch.Samok.HomeActivity2;
import com.example.taquio.trasearch.Messages.MessageActivity;
import com.example.taquio.trasearch.Models.Photo;
import com.example.taquio.trasearch.Models.User;
import com.example.taquio.trasearch.Samok.MyProfileActivity;
import com.example.taquio.trasearch.R;
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

public class MainFeedListAdapter extends ArrayAdapter<Photo> {


    private static final String TAG = "MainFeedListAdapter";
    OnLoadMoreItemsListener mOnLoadMoreItemsListener;
    private LayoutInflater mInflater;
    private int mLayoutResource;
    private Context mContext;
    private DatabaseReference mReference;
    private String currentUsername = "";
    private boolean isBookmark = false, saveLogic = false;

    //firebase
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser;

    public MainFeedListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Photo> objects) {
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
//            holder.red = convertView.findViewById(R.id.bookmarkfill);
//            holder.nored = convertView.findViewById(R.id.bookmarknofill);
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
        holder.caption.setText(getItem(position).getPhoto_description());
        holder.timeDetla.setText(getDate(holder.photo.getDate_createdLong(), "MMM dd, yyyy E hh:mm aa"));



        //set the profile image
//        final ImageLoader imageLoader = ImageLoader.getInstance();
//        imageLoader.displayImage(getItem(position).getImage_path(), holder.image);

        Picasso.with(getContext()).load(getItem(position).getImage_path())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.man)
                .into(holder.image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(getContext())
                                .load(getItem(position).getImage_path())
                                .placeholder(R.drawable.man)
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

                    holder.user = singleSnapshot.getValue(User.class);
                    String name = singleSnapshot.getValue(User.class).getName();
                    String[] arname = name.split(" ") ;
                    holder.username.setText(arname[0]);
                    holder.username.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG, "onClick: navigating to profile of: " +
                                    holder.user.getName());

                            Intent intent = new Intent(mContext, MyProfileActivity.class);
                            intent.putExtra("calling_your_own",
                                    mContext.getString(R.string.home_activity));
                            Log.d(TAG, "onClick: Calling your OWN");
                            intent.putExtra(mContext.getString(R.string.intent_user), holder.user);
                            mContext.startActivity(intent);
                        }
                    });
                    holder.image.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            ((HomeActivity2)mContext).onImageSelected(getItem(position),0, holder.photo.getUser_id());
                           //another thing?
                            ((HomeActivity2)mContext).hideLayout();


                        }
                    });

//                    imageLoader.displayImage(singleSnapshot.getValue(User.class).getImage(),
//                            holder.mprofileImage);
                    Picasso.with(getContext()).load(singleSnapshot.getValue(User.class).getImage())
                            .networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.man)
                            .into(holder.mprofileImage, new Callback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError() {
                                    Picasso.with(getContext())
                                            .load(singleSnapshot.getValue(User.class).getImage())
                                            .placeholder(R.drawable.man)
                                            .into(holder.mprofileImage);
                                }
                            });

                    holder.mprofileImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.d(TAG,  "onClick: navigating to profile of: " +
                                    holder.user.getName());

                            Intent intent = new Intent(mContext, MyProfileActivity.class);
                            intent.putExtra("calling_your_own",
                                    mContext.getString(R.string.home_activity));
                            Log.d(TAG, "onClick: Calling your OWN");
                            intent.putExtra(mContext.getString(R.string.intent_user), holder.user);
                            mContext.startActivity(intent);
                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        FirebaseDatabase.getInstance().getReference().child("Bookmarks")
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(final DataSnapshot dataSnapshot) {
////                                    Toast.makeText(getContext(), "Datasnapshot " + dataSnapshot.getValue(), Toast.LENGTH_LONG).show();
//                    holder.bookmark.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//
//                         Log.d(TAG,    "Bookmark: ");
//                            if (!dataSnapshot.exists()) {
//                                Log.d(TAG,    "Bookmark1: ");
//                                mReference.child("Bookmarks")
//                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                        .child(holder.photo.getPhoto_id())
//                                        .setValue("photoID");
//                                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
////                              saveLogic   = false;
//                            }
//                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                                    Log.d(TAG, "snap: " + snapshot.getKey());
//                                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(snapshot.getKey())) {
////                                    Log.d(TAG, "comparator: " + snapshot.child(snapshot.getKey()).hasChild(holder.photo.getPhoto_id()));
////                                    Log.d(TAG, "kompara: " + snapshot.child(snapshot.getKey()));
////                                    Log.d(TAG, "tocompare: " + snapshot.child(snapshot.getKey()).child(holder.photo.getPhoto_id()).getKey().equals(holder.photo.getPhoto_id()));
//                                        for (DataSnapshot sp : snapshot.getChildren()) {
//                                            Log.d(TAG, "child: " + sp.getKey());
//                                            Log.d(TAG, "child2: " + holder.photo.getPhoto_id());
//                                            if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                                .hasChild(holder.photo.getPhoto_id())) {
//                                                Log.d(TAG, "removemark: " + holder.photo.getPhoto_id());
//                                                Log.d(TAG,    "Bookmark2: ");
//                                                isBookmark = true;
//                                                mReference.child("Bookmarks")
//                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                                        .child(holder.photo.getPhoto_id())
//                                                        .removeValue();
//                                                Toast.makeText(getContext(), "Unsave", Toast.LENGTH_SHORT).show();
////                                            saveLogic =false;
//                                            }
//                                        }
//
//                                        if (!isBookmark) {
//                                            Log.d(TAG, "addmark: " + holder.photo.getPhoto_id());
//                                            Log.d(TAG,    "Bookmark3: ");
//                                            mReference.child("Bookmarks")
//                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                                    .child(holder.photo.getPhoto_id())
//                                                    .setValue("photoID");
//                                            Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
////                                        saveLogic = false;
//                                        }
//                                    }
//                                }
//
//
//                        }
//                     });
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
        holder.bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLogic = true;
                Log.d(TAG, "onClick: BookMark Clicked");
//                Toast.makeText(mContext, "Bookmarked", Toast.LENGTH_SHORT).show();
//                if (saveLogic){
//                    Toast.makeText(mContext, "Bookmarked 2", Toast.LENGTH_SHORT).show();
                    mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
//                                    Toast.makeText(getContext(), "Datasnapshot " + dataSnapshot.getValue(), Toast.LENGTH_LONG).show();

                            if (!dataSnapshot.child("Bookmarks").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                                Log.d(TAG, "onDataChange: Bookmarked no exist");
                                mReference.child("Bookmarks")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(holder.photo.getPhoto_id())
                                        .setValue("photoID");
                                Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
                                saveLogic = true;
                            }
                            else if(!dataSnapshot.child("Bookmarks").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).hasChild(holder.photo.getPhoto_id()))
                            {
                                Log.d(TAG, "onDataChange: Bookmarked no exist");
                                mReference.child("Bookmarks")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child(holder.photo.getPhoto_id())
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
                                        Log.d(TAG, "comparator: " + snapshot.child(snapshot.getKey()).hasChild(holder.photo.getPhoto_id()));
                                        Log.d(TAG, "kompara: " + snapshot.child(snapshot.getKey()));
                                        Log.d(TAG, "tocompare: " + snapshot.child(snapshot.getKey()).child(holder.photo.getPhoto_id()).getKey().equals(holder.photo.getPhoto_id()));

                                        for (DataSnapshot sp : snapshot.getChildren()) {
                                            Log.d(TAG, "child: " + sp.getKey());
                                            if (sp.getKey().equals(holder.photo.getPhoto_id())) {
                                                Log.d(TAG, "removemark: " + holder.photo.getPhoto_id());

                                                isBookmark = true;
                                                mReference.child("Bookmarks")
                                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                        .child(holder.photo.getPhoto_id())

                                                        .removeValue();
                                                Toast.makeText(getContext(), "Unsave", Toast.LENGTH_SHORT).show();
                                                saveLogic =false;
                                            }

                                        }
                                        if (!isBookmark) {
                                            Log.d(TAG, "addmark: " + holder.photo.getPhoto_id());
                                            mReference.child("Bookmarks")
                                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                    .child(holder.photo.getPhoto_id())
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
                            Intent i = new Intent(mContext, MessageActivity.class);
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
            Log.d(TAG, "kuhaon ang para edit: " + holder.user);
            Log.d(TAG, "kuhaon ang para edit: " + holder.photo);
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
//                                    Toast.makeText(mContext, "CLICK!", Toast.LENGTH_SHORT).show();
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
        ImageView red, nored,comment, dm, ellipsis, bookmark;
        User user  = new User();
        StringBuilder users;
        String mLikesString;
        boolean likeByCurrentUser;
        Likes liker;
        GestureDetector detector;
        Photo photo;
        Boolean mProceslike = false;
        RelativeLayout messageLayout;

    }


}





























