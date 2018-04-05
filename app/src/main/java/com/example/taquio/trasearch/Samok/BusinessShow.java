package com.example.taquio.trasearch.Samok;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.taquio.trasearch.R;

/**
 * Created by Del Mar on 3/16/2018.
 */

public class BusinessShow extends AppCompatActivity {

    TextView textView;
    String matName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_material_show);

        textView = (TextView) findViewById(R.id.tvMaterial);
        Bundle bundle = getIntent().getExtras();
        matName = bundle.getString("materialName");

        textView.setText(matName);

    }
}
