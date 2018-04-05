package com.example.taquio.trasearch.Samok;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.BusinessHome.BusinessHome;
import com.example.taquio.trasearch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Del Mar on 2/20/2018.
 */

public class BusinessRegActivity2 extends AppCompatActivity {

    String bsnMail,
            bsnPass,
            bsnConPass,
            bsnBusinessName,
            bsnLocation,
            bsnPhone,
            bsnMobile,
            deviceToken;
    Button busRegister, busUpload;
    TextView busSkip;
    ImageView imagePermit;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private static final String STORAGE_PATH = "images/";
    Boolean flag = false;
    DatabaseReference current_user_db;
    Map userDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_register2);

        bsnMail = getIntent().getExtras().getString("EMAIL");
        bsnPass = getIntent().getExtras().getString("PASS");
        bsnConPass = getIntent().getExtras().getString("CONPASS");
        bsnBusinessName = getIntent().getExtras().getString("BUSINESSNAME");
        bsnLocation = getIntent().getExtras().getString("LOCATION");
//        bsnPhone = getIntent().getExtras().getString("PHONE");
        bsnMobile = getIntent().getExtras().getString("MOBILE");
        deviceToken = FirebaseInstanceId.getInstance().getToken();

        busRegister = (Button) findViewById(R.id.registerBtn1);
        busUpload = (Button) findViewById(R.id.busUpload);
//        busSkip = (TextView) findViewById(R.id.regSkip);
        imagePermit = (ImageView) findViewById(R.id.imagePermit);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        busUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i,"Select Image"),0);
            }
        });
        busRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    uploadData();
            }
        });
//        busSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!flag){
//                    skipUpLoad();
//                }
//            }
//        });

    }
    private void skipUpLoad(){
            mAuth.createUserWithEmailAndPassword(bsnMail, bsnPass).addOnCompleteListener(BusinessRegActivity2.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    if (task.isSuccessful()) {

                        final ProgressDialog progressDialog = new ProgressDialog(BusinessRegActivity2.this);
                        progressDialog.setTitle("Creating account...");
                        progressDialog.show();
                        final String user_id = mAuth.getCurrentUser().getUid();
                        current_user_db = databaseReference.child(user_id);
                        userDetails = new HashMap();
                        userDetails.put("bsnEmail", bsnMail);
                        userDetails.put("bsnBusinessName", bsnBusinessName);
                        userDetails.put("bsnLocation", bsnLocation);
                        userDetails.put("bsnMobile", bsnMobile);
//                        userDetails.put("bsnPhone", bsnPhone);
//                        userDetails.put("imagePermit", "default");
                        userDetails.put("Image", "none");
                        userDetails.put("Image_thumb", "none");
                        userDetails.put("device_token", deviceToken);
                        userDetails.put("userID", user_id);
                        userDetails.put("userType", "business");
                        userDetails.put("isVerified", false);



                        FirebaseDatabase.getInstance().getReference().child("ForVerification")
                                .child(user_id).child("Business_Permit").setValue(imagePermit);

                        current_user_db.setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(getApplicationContext(), "Welcome to TraSearch!", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(BusinessRegActivity2.this, BusinessHome.class);
                                startActivity(i);
                                BusinessRegActivity2.this.finish();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Please select data first", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    }
    private void uploadData() {

        if (imageUri != null) {
            mAuth.createUserWithEmailAndPassword(bsnMail, bsnPass).addOnCompleteListener(BusinessRegActivity2.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        final ProgressDialog progressDialog = new ProgressDialog(BusinessRegActivity2.this);
                        progressDialog.setTitle("Creating account...");
                        progressDialog.show();
                        StorageReference reference = storageReference.child(STORAGE_PATH + System.currentTimeMillis() + "." + imageUri);
                        reference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                final String user_id = mAuth.getCurrentUser().getUid();
                                current_user_db = databaseReference.child(user_id);
                                userDetails = new HashMap();
                                userDetails.put("bsnEmail", bsnMail);
                                userDetails.put("bsnBusinessName", bsnBusinessName);
                                userDetails.put("bsnLocation", bsnLocation);
                                userDetails.put("bsnMobile", bsnMobile);
//                                    userDetails.put("bsnPhone", bsnPhone);
//                                    userDetails.put("imagePermit", taskSnapshot.getDownloadUrl().toString());
                                userDetails.put("image", "none");
                                userDetails.put("image_thumb", "none");
                                userDetails.put("deviceToken", deviceToken);
                                userDetails.put("userID", user_id);
                                userDetails.put("userType", "business");
                                userDetails.put("isVerified", false);

                                current_user_db.setValue(userDetails);
                                FirebaseDatabase.getInstance().getReference().child("ForVerification")
                                        .child(user_id).child("Business_Permit").setValue(taskSnapshot.getDownloadUrl().toString());

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Welcome to TraSearch!", Toast.LENGTH_LONG).show();

                                Intent i = new Intent(BusinessRegActivity2.this, BusinessHome.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                BusinessRegActivity2.this.finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double totalProgress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage((int) totalProgress + " % ");
                            }
                        });
                    }

//                    if (!task.isSuccessful()) {
//                        Toast.makeText(getApplicationContext(), "Select image first", Toast.LENGTH_SHORT).show();
//                    }
                }
            });

        }else {
            Toast.makeText(getApplicationContext(), "Please upload an image for security", Toast.LENGTH_SHORT).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 0 && resultCode == RESULT_OK) {
            imageUri = data.getData();

            flag = true;
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageUri);
                imagePermit.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getActualImage(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}

