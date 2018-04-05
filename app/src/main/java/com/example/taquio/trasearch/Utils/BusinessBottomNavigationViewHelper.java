package com.example.taquio.trasearch.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.example.taquio.trasearch.BusinessHome.BusinessHome;
import com.example.taquio.trasearch.BusinessProfile.BusinessProfile;
import com.example.taquio.trasearch.Messages.BusMessagesActivity;
import com.example.taquio.trasearch.Messages.MessagesActivity;
import com.example.taquio.trasearch.R;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

/**
 * Created by Del Mar on 2/7/2018.
 */

public class BusinessBottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";

    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView (Main)");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableShiftingMode(false);
    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.bs_home:
                        Intent intent1 = new Intent(context, BusinessHome.class);
                        context.startActivity(intent1);
                        break;
                    case R.id.bs_messages:
                        Intent intent2 = new Intent(context, BusMessagesActivity.class );
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        ((Activity)context).finish();
                        break;
                    case R.id.bs_profile:
                        Intent intent3 = new Intent(context, BusinessProfile.class);
                        context.startActivity(intent3);
                        break;
                }

                return false;
            }
        });
    }
}
