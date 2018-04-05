package com.example.taquio.trasearch.BusinessMessages;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taquio.trasearch.R;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class BusinessFriendsListFragment extends Fragment {
    private static final String TAG = "BusinessFriendsListFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.business_message_friendslist_fragment, container, false);

        return view;
    }
}