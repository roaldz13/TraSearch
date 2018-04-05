package com.example.taquio.trasearch.Samok;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.taquio.trasearch.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by Del Mar on 2/7/2018.
 */

public class VideosFragment extends Fragment {
    private static final String TAG = "VideosFragment";
    FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_videos, container, false);
        mAuth = FirebaseAuth.getInstance();

        return view;
    }
}
