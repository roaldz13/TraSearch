package com.example.taquio.trasearch.Samok;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberUtils;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.HintRequest;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    String email;
    ProgressDialog regProgress;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private EditText field_email
            ,field_password
            ,field_username
            ,field_cPassword
            ,field_address
            ,field_phonenumber;
    private Button btn_submit;
    private TextView register_cancelBtn;
//    private ImageButton chooseImage;
    private ImageView userProfileImage;
    private Uri filePath;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private final String APPLICATION_KEY = "f210079c-626f-47fe-a1ce-8b82077daffb";
    private final int RESOLVE_HINT =19;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        refIDs();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        if(getIntent().hasExtra("emailPass"))
        {
            email = getIntent().getStringExtra("emailPass");
            field_email.setText(email);
        }

        register_cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

        startActivity(new Intent(RegisterActivity.this, GuestSearch.class));

            }
        });


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Submit button clicked");

                regProgress = new ProgressDialog(RegisterActivity.this);
                regProgress.setTitle("Registering");
                regProgress.setMessage("Please wait while we verify your data");
                regProgress.show();

                final String pass = field_password.getText().toString(),
                        cPass = field_cPassword.getText().toString();

                if(hasRegError())
                {
                    Log.d(TAG, "onClick: Has Error");
                    Toast.makeText(RegisterActivity.this,"Please check your Registration Details",Toast.LENGTH_SHORT).show();
                    regProgress.dismiss();
                }

                else if (!(pass.equals(cPass)))
                {
                    field_password.setError("Password didn't match");
                    field_cPassword.setError("Password didn't match");
                    regProgress.dismiss();
                }
                else
                {
                    email = field_email.getText().toString();
                    Log.d(TAG, "onClick: Registering User");
                    DatabaseReference checkUserName = FirebaseDatabase.getInstance().getReference();

                    checkUserName.child("Users").child("UserName").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(field_username
                                    .getText()
                                    .toString()))
                            {
                                field_username.setError("Username already exist");
                                regProgress.dismiss();
                            }
                            else
                            {
//                                 String user_id = mAuth.getCurrentUser().getUid();
                                Intent i = new Intent(RegisterActivity.this, ForVerification.class);
                                i.putExtra("a", field_email.getText().toString());
                                i.putExtra("b", field_password.getText().toString());
                                i.putExtra("1", field_email.getText().toString());
                                i.putExtra("2", field_username.getText().toString());
                                i.putExtra("3", field_address.getText().toString().toUpperCase());
                                i.putExtra("4", "none");
                                i.putExtra("5", "none");
                                i.putExtra("6", FirebaseInstanceId.getInstance().getToken());
                                i.putExtra("7", field_phonenumber.getText().toString());
//                                i.putExtra("8", user_id);
                                i.putExtra("9", "non-business");
                                i.putExtra("10", false);
                                startActivity(i);
//                                addUser(field_email
//                                .getText()
//                                .toString(),field_password
//                                .getText()
//                                .toString());
                                regProgress.dismiss();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
//                startActivity(new Intent(RegisterActivity.this,ForVerification.class));
//                finish();
           }
        });
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        filePath = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            userProfileImage.setImageBitmap(bitmap);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        if (requestCode == RESOLVE_HINT) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                // credential.getId();  <-- will need to process phone number string
            }
        }
    }

    private boolean hasRegError()
    {
        final String email = field_email.getText().toString(),
                password = field_password.getText().toString(),
//                username = field_username.getText().toString(),
                name = field_username.getText().toString();
        boolean flag = true;

        if(email.length()<=0)
        {
            field_email.setError("Please input a valid Email Address");
        }
        else if(name.length()<=0)
        {
            field_username.setError("Please input a valid Name");
        }
//        else if (username.length()<=0)
//        {
//            field_username.setError("Please input a valid Username");
//        }
        else if(password.length()<8)
        {
            field_password.setError("Password must be more than 8 characters");
        }
        else
        {
            flag=false;
        }
        return flag;
    }

    private void refIDs()
    {
        field_email = findViewById(R.id.field_email);
        field_password = findViewById(R.id.field_password);
        btn_submit = findViewById(R.id.btn_submit);
        field_username = findViewById(R.id.field_username);
//        field_name = findViewById(R.id.field_name);
//        chooseImage = findViewById(R.id.register_chooseImage);
//        userProfileImage = findViewById(R.id.register_image);
        field_cPassword = findViewById(R.id.field_cPassword);
        field_address = findViewById(R.id.address);
        field_phonenumber = findViewById(R.id.field_phonenumber);
        register_cancelBtn = findViewById(R.id.register_cancelBtn);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, GuestSearch.class));
    }
    //    private void requestHint() throws IntentSender.SendIntentException {
//        HintRequest hintRequest = new HintRequest.Builder()
//                .setPhoneNumberIdentifierSupported(true)
//                .build();
//
//        PendingIntent intent = Auth.CredentialsApi.getHintPickerIntent(
//                apiClient, hintRequest);
//        startIntentSenderForResult(intent.getIntentSender(),
//                RESOLVE_HINT, null, 0, 0, 0);
//    }
}
