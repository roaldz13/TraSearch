package com.example.taquio.trasearch.Samok;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.Models.Photo;
import com.example.taquio.trasearch.Models.User;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Utils.SquareImageView;
import com.example.taquio.trasearch.Utils.UniversalImageLoader;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Edward on 26/02/2018.
 */

public class EditPostItem extends AppCompatActivity {
    private static final String TAG = "EditPostItem";
    private DatabaseReference mReference;
    private ImageView close;
    private TextView save, username;
    private EditText photo_desc, qty;
    private String captionHolder, qtyholder;
    private CircleImageView profphoto;
    private SquareImageView postedphoto;
    String time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit_post);

        final Context context = EditPostItem.this;

        mReference = FirebaseDatabase.getInstance().getReference();
        profphoto = findViewById(R.id.profile_photo);
        postedphoto = findViewById(R.id.post_image);
        username = findViewById(R.id.username);

        photo_desc = (EditText) findViewById(R.id.image_caption);
        qty = findViewById(R.id.item_qty);

        close = findViewById(R.id.ivCloseShare);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        save = findViewById(R.id.tvSave);

        Bundle b =  getIntent().getExtras();

        final User muser = (User) b.get("user");
       final Photo mphoto = (Photo) b.get("photo");

        Log.d(TAG, "Editing : " + muser);
        Log.d(TAG, "Editing Photo: " + mphoto);

        String name = muser.getName();
        String[] arname = name.split(" ") ;
                            UniversalImageLoader.setImage(muser.getImage(),profphoto, null,"");
                            UniversalImageLoader.setImage(mphoto.getImage_path(),postedphoto, null,"");
                            username.setText(arname[0]);
                            photo_desc.setText(mphoto.getPhoto_description());
                            qty.setText(mphoto.getQuantity());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map postDetails = new HashMap();
                postDetails.put("date_created",mphoto.getDate_createdLong());
                postDetails.put("image_path",mphoto.getImage_path());
                postDetails.put("photo_description",photo_desc.getText().toString());
                postDetails.put("photo_id", mphoto.getPhoto_id());
                postDetails.put("quantity",qty.getText().toString());
                postDetails.put("user_id",mphoto.getUser_id());


                    mReference.child("Users_Photos")
                            .child(mphoto.getUser_id())
                            .child(mphoto.getPhoto_id())
                            .setValue(postDetails);

                    mReference.child("Photos")
                            .child(mphoto.getPhoto_id())
                            .child(mphoto.getUser_id())
                            .setValue("userID");

                    Log.d(TAG, "Na change: " + postDetails);
                    Toast.makeText(EditPostItem.this,"Successfully Edited!", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(EditPostItem.this, HomeActivity2.class));

            }
        });


    }
}
