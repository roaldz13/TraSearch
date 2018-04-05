package com.example.taquio.trasearch.Samok;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.eschao.android.widget.elasticlistview.ElasticListView;
import com.example.taquio.trasearch.Models.Bookmark;
import com.example.taquio.trasearch.Models.Like;
import com.example.taquio.trasearch.Models.Photo;
import com.example.taquio.trasearch.R;
import com.example.taquio.trasearch.Utils.BottomNavigationViewHelper;
import com.example.taquio.trasearch.Utils.ViewPostFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SaveItemActivity extends AppCompatActivity{
    private static final String TAG = "SaveItemActivity";
    private static final int NUM_GRID_COLUMNS = 3;

//    OnSelect mOnSelectedListener;

    private ElasticListView mListView;
    private ArrayList<String> msaveUserId;
    private ArrayList<String> thephotos;
    private ArrayList<Photo> mPaginatedPhotos;
    private int resultsCount = 0;
    private GridView gridView;
    FrameLayout mFrameLayout;
    RelativeLayout mRelativeLayout;
    Toolbar savedToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_item);
         mFrameLayout = findViewById(R.id.frame_container);
         mRelativeLayout = findViewById(R.id.relativeSave);
        savedToolbar = findViewById(R.id.profileToolBar);

        mListView = findViewById(R.id.listView);
        gridView = findViewById(R.id.grid);


        ImageView backArrow = findViewById(R.id.ivBackArrow);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing the activity");
                finish();
            }
        });
//        initListViewRefresh();
        setupBottomNavigationView();
        getSaveItems();
//        getPhotos();

    }
    private void getSaveItems() {
        Log.d(TAG, "getKeys: searching for following");
        clearAll();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child("Bookmarks");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {


                    if(singleSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        for (DataSnapshot ds : singleSnapshot.getChildren()){
                            Log.d(TAG, "getKeys: found user: " + singleSnapshot
                                    .getKey());
                            thephotos.add(ds.getKey());
                        }

                    }

                }
                getPhotos(thephotos);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }
    private void clearAll(){
        thephotos = new ArrayList<>();
        mPaginatedPhotos = new ArrayList<>();
        msaveUserId = new ArrayList<>();
    }
    public  void getPhotos(final ArrayList<String> thephotos) {
        Log.d(TAG, "getPhotos: getting list of photos");
        final ArrayList<Photo> photos = new ArrayList<>();
        for (int i = 0; i < thephotos.size(); i++) {
            final int index = i;
            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("Users_Photos");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "Save Item Values "+ dataSnapshot.getValue());

                    for(DataSnapshot ds: dataSnapshot.getChildren()){
                        for(DataSnapshot sp: ds.getChildren()){
                            if(sp.getKey().equals(thephotos.get(index))){
                                Photo newPhoto = new Photo();
                                Log.d(TAG, "Getting: " +  ds.getValue());
                                Map<String, Object> objectMap = (HashMap<String, Object>) sp.getValue();
                                try {
                                    newPhoto.setPhoto_description(objectMap.get(getString(R.string.field_caption)).toString());
                                    newPhoto.setQuantity(objectMap.get(getString(R.string.field_tags)).toString());
                                    newPhoto.setPhoto_id(objectMap.get(getString(R.string.field_photo_id)).toString());
                                    newPhoto.setUser_id(objectMap.get(getString(R.string.field_user_id)).toString());
                                    newPhoto.setDate_created(Long.parseLong(objectMap.get(getString(R.string.field_date_created)).toString()));
                                    newPhoto.setImage_path(objectMap.get(getString(R.string.field_image_path)).toString());

                                    photos.add(newPhoto);
                                    Log.d(TAG, "photo checking : "+ photos.size());
                                } catch (NullPointerException e) {
                                    Log.e(TAG, "onDataChange: NullPointerException: " + e.getMessage());
                                }
                            }
                        }
                    }
                    //setup our image grid
                    int gridWidth = getResources().getDisplayMetrics().widthPixels;
                    int imageWidth = gridWidth/NUM_GRID_COLUMNS;
                    gridView.setColumnWidth(imageWidth);

                    ArrayList<String> imgUrls = new ArrayList<String>();
                    for(int i = 0; i < photos.size(); i++){
                        imgUrls.add(photos.get(i).getImage_path());
                    }

                    SaveGridImageAdapter adapter = new SaveGridImageAdapter(SaveItemActivity.this,R.layout.layout_grid_imageview,
                            "", imgUrls);
                    gridView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d(TAG, "image sa url : " + photos.get(position));
                           onImageSelected(photos.get(position),4);

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: query cancelled.");
                }
            });
        }
    }

    private void onImageSelected(Photo photo, int i) {

        hideLayoutSave();


        ViewPostFragment fragment = new ViewPostFragment();
        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.photo), photo);
        args.putInt(getString(R.string.activity_number), i);
        args.putString("theCall", "fromBookmark");

        fragment.setArguments(args);

        FragmentTransaction transaction  = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    public  void hideLayoutSave(){
        Log.d(TAG, "hideLayout: hiding layout");
        mRelativeLayout.setVisibility(View.GONE);
        mFrameLayout.setVisibility(View.VISIBLE);
    }


    public  void showLayoutSave(){
        Log.d(TAG, "hideLayout: showing layout");
        mRelativeLayout.setVisibility(View.VISIBLE);
        mFrameLayout.setVisibility(View.GONE);
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(this, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);
    }
}
