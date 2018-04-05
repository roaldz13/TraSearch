package com.example.taquio.trasearch.Samok;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.taquio.trasearch.BusinessHome.BusinessHome;
import com.example.taquio.trasearch.Guest.GuestHome;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.SearchLogic.ArticleData;
import com.example.taquio.trasearch.SearchLogic.ArticleHTTPRequest;
import com.example.taquio.trasearch.SearchLogic.VideoData;
import com.example.taquio.trasearch.SearchLogic.VideoHTTPRequest;
import com.example.taquio.trasearch.Utils.Donate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import android.os.Handler;
import android.os.Message;

/**
 * Created by Del Mar on 2/12/2018.
 */

public class GuestSearch extends AppCompatActivity {
    private static final String TAG = "GuestSearch";

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    private Button signIn, reg,searchExec;
    private EditText searchText;
    private DatabaseReference searchItem;
    AdView mAdView;
    FloatingActionButton floatBtn;
    RadioGroup searchMethod;
    RadioButton webCrawl, databseSearch;
    private boolean search_method;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_search);
        refIDs();
        mAuth = FirebaseAuth.getInstance();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        searchMethod.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.webCrawl) {
                    search_method = true;
                }
                else if (checkedId == R.id.databaseSearch) {
                    search_method = false;
                }
            }
        });
        searchText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GuestSearch.this, GuestHome.class);
//                i.putExtra("searchMethod", search_method);
                i.putExtra("searchMethod", search_method);
                startActivity(i);
                Log.d(TAG, "onClick: " + search_method);
            }
        });

        floatBtn = findViewById(R.id.floatingButton);

        floatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuestSearch.this, Donate.class);
                startActivity(intent);
            }
        });
        MobileAds.initialize(GuestSearch.this, "ca-app-pub-3940256099942544~3347511713");
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuestSearch.this,ActivityLogin.class));

            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuestSearch.this,ChooseLayout.class));

            }
        });


//        searchExec.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final String search = searchText.getText().toString();
//                Log.d(TAG, "Hello World" + search);
////                progressDialog = new ProgressDialog(GuestSearch.this);
////                progressDialog.setTitle("Searching");
////                progressDialog.setMessage("Please wait while we CRAWL");
////                progressDialog.show();
////                progressDialog.setCanceledOnTouchOutside(false);
////                final String search = searchText.getText().toString();
////
////                DatabaseReference mTraSearch;
////                mTraSearch = FirebaseDatabase.getInstance().getReference().child("TraSearch");
////                searchItem = FirebaseDatabase.getInstance().getReference().child("TraSearch").child(search).child("Videos");
////
////                Log.d(TAG, "Before EventListener");
////                mTraSearch.addValueEventListener(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(DataSnapshot dataSnapshot) {
////                        Log.d(TAG, "Before checking");
////                        if(!dataSnapshot.child(search).exists())
////                        {
////                            Spider spider = new Spider();
////                            Map<Integer, CrawledData> videoLinks;
////                            Map tobeUpload = new HashMap();
////                            videoLinks = spider.searchEngine(search);
////                            for(Integer index: videoLinks.keySet()){
////                                Integer key = index;
////                                CrawledData value = videoLinks.get(key);
////                                String[] newLink = value.getUrl().split("v=");
////
////
////                                tobeUpload.put(newLink[1]+"/Title",value.getTitle());
////                            }
////
////                            searchItem.updateChildren(tobeUpload).addOnCompleteListener(new OnCompleteListener() {
////                                @Override
////                                public void onComplete(@NonNull Task task) {
////                                    if(task.isSuccessful())
////                                    {
////                                        Log.d(TAG, "onComplete: Search Completed");
////                                        progressDialog.dismiss();
////                                    }
////                                    else
////                                    {
////                                        Log.d(TAG, "onComplete: Search Failed");
////                                        progressDialog.dismiss();
////                                    }
////                                }
////                            });
////                        }
////                        else{
////                            Log.d(TAG, "onDataChange: "+search+" already in the DAtabase");
////                            progressDialog.dismiss();
////                        }
////                    }
////
////                    @Override
////                    public void onCancelled(DatabaseError databaseError) {
////
////                    }
////                });
//            }
//        });
    }


//    public void getArticles(View view) {
//        final String sea = searchText.getText().toString();
//        ExecutorService executor = Executors.newCachedThreadPool();
//
//        Future<Map<Integer, ArticleData>> future = executor.submit(new Callable<Map<Integer, ArticleData>>() {
//            @Override
//            public Map<Integer, ArticleData> call() throws Exception {
//                ArticleHTTPRequest articleHTTPRequest = new ArticleHTTPRequest();
//                return articleHTTPRequest.sendGet(sea);
//            }
//        });
//        executor.shutdown();
//
//        try {
//            Map<Integer, ArticleData> articleData;
//            StringBuilder sb = new StringBuilder();
//            articleData = future.get();
//            for (Integer index: articleData.keySet()) {
//                ArticleData value = articleData.get(index);
//                String name = value.getName() + "\n";
//                sb.append(name);
//            }
//            Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void getVideos(View view){
//        final String sea = searchText.getText().toString();
////        Toast.makeText(this, "YAWA" + sea, Toast.LENGTH_SHORT).show();
//        ExecutorService executor = Executors.newCachedThreadPool();
//
//        Future<Map<Integer, VideoData>>  future = executor.submit(new Callable<Map<Integer, VideoData>>() {
//            @Override
//            public Map<Integer, VideoData> call() throws Exception {
////                StringBuilder sb = new StringBuilder();
//                VideoHTTPRequest request = new VideoHTTPRequest();
////                Map<Integer, VideoData> videoDatas;
////                videoDatas = request.sendGet(sea);
////                for (Integer index: videoDatas.keySet()) {
////                    VideoData value = videoDatas.get(index);
////                    String title = value.getTitle();
////                    sb.append(title);
////                }
//                return request.sendGet(sea);
//            }
//        });
//        executor.shutdown();
//        try {
//            Map<Integer, VideoData> videoDatas;
//            StringBuilder sb = new StringBuilder();
//            videoDatas = future.get();
//            for (Integer index: videoDatas.keySet()) {
//                VideoData value = videoDatas.get(index);
//                String title = value.getTitle() + "\n";
//                sb.append(title);
//            }
//            Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Started");
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    private void updateUI(FirebaseUser user){
        if(user !=null)
        {
            DatabaseReference userType = FirebaseDatabase.getInstance().getReference().child("Users")
                    .child(mAuth.getCurrentUser().getUid());

            userType.child("userType").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
//                    if (dataSnapshot.hasChild("userType"))
//                    {
                        String userType = dataSnapshot.getValue().toString();
                        if(userType.equals("non-business"))
                        {
                            startActivity(new Intent(GuestSearch.this,HomeActivity2.class));
                            finish();
                        }
                        else if(userType.equals("admin"))
                        {
                            startActivity(new Intent(GuestSearch.this,AdminActivity.class));
                            finish();
                        }
                        else if(userType.equals("business"))
                        {
                            startActivity(new Intent(GuestSearch.this,BusinessHome.class));
                            finish();
                        }else{
                            Toast.makeText(GuestSearch.this,"UserType is null",Toast.LENGTH_LONG).show();
                        }
//                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void refIDs()
    {
        signIn = findViewById(R.id.guest_SignIn);
        reg = findViewById(R.id.guest_Reg);
//        searchExec = findViewById(R.id.searchExec);
        searchText = findViewById(R.id.searchText);
        searchMethod = (RadioGroup) findViewById(R.id.searchMethod);
        webCrawl = (RadioButton) findViewById(R.id.webCrawl);
        databseSearch = (RadioButton) findViewById(R.id.databaseSearch);
    }
}