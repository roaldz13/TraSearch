package com.example.taquio.trasearch.Samok;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

public class ForVerification extends AppCompatActivity {
    private static final String TAG = "ForVerification";
    FirebaseAuth mAuth;
    StorageReference mImageStorage;
    DatabaseReference mUserDatabase;
    private Button ver_IDbtn,ver_Selfiebtn,ver_Skipbtn,ver_UploadExec;
    private ImageView ver_ID,ver_Selfie;
    private Uri selfie,mID;
    private boolean isID=false,isSelfie=false, checker = false;
    private String mCurrentUser;
    String mail, pass, name, address, image;
    String imageThumb, token, number, id, type, verify;
    CheckBox box;
    TextView txv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_for_verification);
        refIDs();
        txv = findViewById(R.id.tvPolicy);
        box = findViewById(R.id.cbPolicy);
        if(box.isChecked()){
            box.setChecked(true);
        }
        txv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mImageStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
//        mCurrentUser = mAuth.getCurrentUser();
//        FirebaseUser user = mAuth.getCurrentUser();
//        mCurrentUser = user.getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("ForVerification");

//        ver_Skipbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(ForVerification.this,HomeActivity2.class));
//                finish();
//            }
//        });


        mail = getIntent().getExtras().getString("a");
        pass = getIntent().getExtras().getString("b");




        ver_IDbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = CropImage.activity(mID)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setBorderCornerColor(Color.GREEN)
                        .setBorderLineColor(Color.GREEN)
                        .setActivityMenuIconColor(Color.GREEN)
                        .setBorderCornerColor(Color.GREEN)
                        .setFixAspectRatio(true)
                        .getIntent(ForVerification.this);
                startActivityForResult(intent, 0);
                isID = true;
            }
        });

        ver_Selfiebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity(selfie)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setBorderCornerColor(Color.GREEN)
                        .setBorderLineColor(Color.GREEN)
                        .setActivityMenuIconColor(Color.GREEN)
                        .setBorderCornerColor(Color.GREEN)
                        .setFixAspectRatio(true)
                        .getIntent(ForVerification.this);
                startActivityForResult(intent, 1);
                isSelfie = true;
            }
        });
        if (isID||isSelfie)
        {
//            checker = true;
            ver_UploadExec.setEnabled(true);
        }
        ver_UploadExec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addUser(mail, pass);

            }
        });

    }

    private void upLoadExec(boolean IDD,boolean Selfiee)
    {
        final ProgressDialog progressDialog = new ProgressDialog(ForVerification.this);
        if(IDD&&Selfiee)
        {
            progressDialog.setTitle("Uploading...");
            progressDialog.setMessage("Please wait while we upload your Images");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        if (Selfiee && IDD)
        {
            StorageReference filePath = mImageStorage.
                    child("forVerification").
                    child(mCurrentUser).
                    child("Selfie");

            filePath.putFile(selfie).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ver_Selfiebtn.setEnabled(false);
                    String selfURL  = taskSnapshot.getDownloadUrl().toString();
                    mUserDatabase.child(mCurrentUser).child("selfieURL").setValue(selfURL );
                    Log.d(TAG, "onSuccess: SELFIE  ");
//                    ver_Skipbtn.setText("Finish");
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            StorageReference filePath1 = mImageStorage.
                    child("forVerification").
                    child(mCurrentUser).
                    child("ID");
            filePath1.putFile(mID).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ver_Selfiebtn.setEnabled(false);
                    String IDURL = taskSnapshot.getDownloadUrl().toString();
                    mUserDatabase.child(mCurrentUser).child("IDURL").setValue(IDURL);
                    Log.d(TAG, "onSuccess: ID  ");
//                    ver_Skipbtn.setText("Finish");
                    progressDialog.dismiss();
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

                }
            });
        }
    }

    private void refIDs()
    {
        ver_ID = findViewById(R.id.ver_ID);
        ver_Selfie = findViewById(R.id.ver_Selfie);
        ver_IDbtn = findViewById(R.id.ver_IDbtn);
        ver_Selfiebtn = findViewById(R.id.ver_Selfiebtn);
//        ver_Skipbtn = findViewById(R.id.ver_Skipbtn);
        ver_UploadExec = findViewById(R.id.ver_UploadExec);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mID = result.getUri();
                ver_ID.setImageURI(mID);
                isID=true;
                if(isID&&isSelfie)
                {
                    ver_UploadExec.setEnabled(true);
                }else{
                    Toast.makeText(ForVerification.this,"Please take a picture for security",Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
        else if (requestCode == 1) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                selfie = result.getUri();
                ver_Selfie.setImageURI(selfie);
                isSelfie=true;
                if(isID&&isSelfie)
                {
                    ver_UploadExec.setEnabled(true);
                }else{
                    Toast.makeText(ForVerification.this,"Please provide a valid ID picture",Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }


    }
    //----------------------------------------------------------

    public void addUser(String email,String password)
    {
        Log.d(TAG, "addUser: Started");
        if(mID!=null&&selfie!=null){
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(ForVerification.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "onComplete: Success Registration");
                                FirebaseUser user = mAuth.getCurrentUser();
                                mCurrentUser = user.getUid();
                                updateUI(user);
                                upLoadExec(isID,isSelfie);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(ForVerification.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
//                            regProgress.dismiss();
                                updateUI(null);
                            }
                        }
                    });
        }else{
            Toast.makeText(ForVerification.this, "Please upload a business permit.",
                    Toast.LENGTH_SHORT).show();
        }

    }
    private void updateUI(FirebaseUser user)
    {
        if(user !=null)
        {
            Log.d(TAG, "updateUI: Adding User Details to Database");

            mail = getIntent().getExtras().getString("a");
            name = getIntent().getExtras().getString("2");
            address = getIntent().getExtras().getString("3");
            image = getIntent().getExtras().getString("4");
            imageThumb = getIntent().getExtras().getString("5");
            token = getIntent().getExtras().getString("6");
            number = getIntent().getExtras().getString("7");
//            id = getIntent().getExtras().getString("8");
            type = getIntent().getExtras().getString("9");
            verify = getIntent().getExtras().getString("10");
             DatabaseReference current_user_db = FirebaseDatabase.getInstance()
                                                        .getReference().child("Users").child(user.getUid());
            Map userDetails = new HashMap();
            userDetails.put("Email",mail);
            userDetails.put("Name",name);
            userDetails.put("Address",address);
            userDetails.put("Image",image);
            userDetails.put("Image_thumb",imageThumb);
            userDetails.put("device_token",token);
            userDetails.put("PhoneNumber",number);
            userDetails.put("userID",user.getUid());
            userDetails.put("userType",type);
            userDetails.put("isVerified",false);

            current_user_db.setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
//                        regProgress.dismiss();
                        Toast.makeText(ForVerification.this,"Welcome "+ name ,Toast.LENGTH_SHORT).show();
                        Intent startActivityIntent = new Intent(ForVerification.this, HomeActivity2.class);
                        startActivity(startActivityIntent);
                        finish();
                    }
                }
            });

        }
        else
        {
            Toast.makeText(ForVerification.this,"Error",Toast.LENGTH_SHORT).show();
        }
    }
}
