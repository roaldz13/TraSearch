package com.example.taquio.trasearch.Samok;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.taquio.trasearch.BusinessProfile.Material;
import com.example.taquio.trasearch.R;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Del Mar on 3/15/2018.
 */

public class BusinessAdd extends AppCompatActivity implements View.OnClickListener{

    private EditText etAdd;
    private Button btAdd;
    private String key, name;
    private static final FirebaseDatabase DATABASE = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_add_material);

        referenceId();
        firebaseSetup();

    }

    private void referenceId(){
        etAdd = (EditText) findViewById(R.id.etAddMaterial);
        btAdd = (Button) findViewById(R.id.btnAddMaterial);
        btAdd.setOnClickListener(this);
    }

    private void firebaseSetup() {
        key = DATABASE.getReference().push().getKey();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddMaterial:
                name = etAdd.getText().toString();
                if(!TextUtils.isEmpty(name)) {
                    Material material = new Material(name);
                    DATABASE.getReference().child("Materials").child(key).setValue(material);
                    finish();
                }else {
                    etAdd.setError("Text field must not be empty");
                }
                break;
        }
    }

}
