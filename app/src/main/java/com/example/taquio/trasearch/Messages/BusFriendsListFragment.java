package com.example.taquio.trasearch.Messages;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.taquio.trasearch.R;

/**
 * Created by Del Mar on 2/15/2018.
 */

public class BusFriendsListFragment extends Fragment {
    private static final String TAG = "FriendsListFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.business_fragment_friendslist, container, false);

        return view;
    }
}
