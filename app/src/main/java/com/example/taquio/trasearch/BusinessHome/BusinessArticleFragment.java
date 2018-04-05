package com.example.taquio.trasearch.BusinessHome;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Samok.BusinessAdd;

/**
 * Created by Del Mar on 2/16/2018.
 */

public class BusinessArticleFragment extends Fragment {
    private static final String TAG = "BusinessArticleFragment";
    Button btn;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.business_home_articles_fragment, container, false);
        btn = (Button) view.findViewById(R.id.addBtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), BusinessAdd.class);
                startActivity(i);
            }
        });
        return view;
    }
}
