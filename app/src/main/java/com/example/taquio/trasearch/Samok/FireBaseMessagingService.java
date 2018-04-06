package com.example.taquio.trasearch.Samok;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.taquio.trasearch.Models.User;
import com.example.taquio.trasearch.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by taquio on 2/13/18.
 */

public class FireBaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    private static final String TAG = "FireBaseMessagingServic";
    private DatabaseReference mUserDatabase;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

//        String notification_title = remoteMessage.getNotification().getTitle();
//        String notification_message = remoteMessage.getNotification().getBody();
//        final String click_action = remoteMessage.getNotification().getClickAction();
//        String from_user_id = remoteMessage.getData().get("from_user_id");
//
//
//
//        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
//
//        final User user = new User();
//
//        final NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.trasearchicon)
//                        .setContentTitle(notification_title)
//                        .setContentText(notification_message);
//
//        mUserDatabase.child(from_user_id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Boolean verify = false;
//                String deviceToken =" ", Email =" ", Image = " ",
//                        Image_thumb = " ", Name = " ",  CPNumber = " ",  userID = " ";
//
//                if(dataSnapshot.child("userType").getValue().equals("non-business")){
//                    deviceToken = dataSnapshot.child("device_token").getValue().toString();
//                    Email = dataSnapshot.child("Email").getValue().toString();
//                    Image = dataSnapshot.child("Image").getValue().toString();
//                    Image_thumb = dataSnapshot.child("Image_thumb").getValue().toString();
//                    Name = dataSnapshot.child("Name").getValue().toString();
//                    CPNumber = dataSnapshot.child("PhoneNumber").getValue().toString();
//                    userID = dataSnapshot.child("userID").getValue().toString();
////                        UserName = dataSnapshot.child("UserName").getValue().toString();
//                    verify = dataSnapshot.child("isVerified").getValue(Boolean.class);
//                }
//                if(dataSnapshot.child("userType").getValue().equals("business")){
//                    deviceToken = dataSnapshot.child("device_token").getValue().toString();
//                    Email = dataSnapshot.child("bsnEmail").getValue().toString();
//                    Image = dataSnapshot.child("image").getValue().toString();
//                    Image_thumb = dataSnapshot.child("image_thumb").getValue().toString();
//                    Name = dataSnapshot.child("bsnBusinessName").getValue().toString();
//                    CPNumber = dataSnapshot.child("bsnMobile").getValue().toString();
//                    userID = dataSnapshot.child("userID").getValue().toString();
////                        UserName = dataSnapshot.child("UserName").getValue().toString();
//                    verify = dataSnapshot.child("isVerified").getValue(Boolean.class);
//                }
//
//                user.setDevice_token(deviceToken);
//                user.setEmail(Email);
//                user.setImage(Image);
//                user.setImage_thumb(Image_thumb);
//                user.setName(Name);
//                user.setPhoneNumber(CPNumber);
//                user.setUserID(userID);
////                user.setName(UserName);
//                user.setVerified(verify);
//
//
//                Intent resultIntent = new Intent(click_action);
//
//                Log.d(TAG, "onMessageReceived: For transfer: "+user);
//                resultIntent.putExtra("formessage","formessage");
//                resultIntent.putExtra("intent_user", user);
//
//                PendingIntent resultPendingIntent =
//                        PendingIntent.getActivity(
//                                FireBaseMessagingService.this,
//                                0,
//                                resultIntent,
//                                PendingIntent.FLAG_UPDATE_CURRENT
//                        );
//                mBuilder.setContentIntent(resultPendingIntent);
//
//                int mNotificationId = (int) System.currentTimeMillis();
//                NotificationManager mNotifyMgr =
//                        (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                mNotifyMgr.notify(mNotificationId, mBuilder.build());
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
        String notification_title = remoteMessage.getNotification().getTitle();
        String notification_message = remoteMessage.getNotification().getBody();
        final String click_action = remoteMessage.getNotification().getClickAction();
        String from_user_id = remoteMessage.getData().get("from_user_id");



        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        final User user = new User();

        final NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.trasearchicon)
                        .setContentTitle(notification_title)
                        .setContentText(notification_message);

        mUserDatabase.child(from_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String deviceToken = dataSnapshot.child("device_token").getValue().toString(),
                        Email = dataSnapshot.child("Email").getValue().toString(),
                        Image = dataSnapshot.child("Image").getValue().toString(),
                        Image_thumb = dataSnapshot.child("Image_thumb").getValue().toString(),
                        Name = dataSnapshot.child("Name").getValue().toString(),
                        CPNumber = dataSnapshot.child("PhoneNumber").getValue().toString(),
                        userID = dataSnapshot.child("userID").getValue().toString();
//                        UserName = dataSnapshot.child("UserName").getValue().toString();
                Boolean verify = dataSnapshot.child("isVerify").getValue(Boolean.class);

                user.setDevice_token(deviceToken);
                user.setEmail(Email);
                user.setImage(Image);
                user.setImage_thumb(Image_thumb);
                user.setName(Name);
                user.setPhoneNumber(CPNumber);
                user.setUserID(userID);
//                user.setUserName(UserName);
                user.setVerified(verify);


                Intent resultIntent = new Intent(click_action);

                Log.d(TAG, "onMessageReceived: For transfer: "+user);
                resultIntent.putExtra("formessage","formessage");
                resultIntent.putExtra("intent_user", user);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                FireBaseMessagingService.this,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                int mNotificationId = (int) System.currentTimeMillis();
                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mNotifyMgr.notify(mNotificationId, mBuilder.build());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
