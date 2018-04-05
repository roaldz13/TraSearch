package com.example.taquio.trasearch.Guest;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taquio.trasearch.BusinessProfile.SectionsPageAdapter;
import com.example.taquio.trasearch.R;

public class GuestHome extends AppCompatActivity {
  private String TAG = "Guest Home";
  private Context mContext = GuestHome.this;
  private ViewPager mViewPager;
  EditText searchView;
  Switch searchSwitch;
  private boolean search_method;
  private String searchQuery;
  private Bundle result;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_guest_home);

    mViewPager = (ViewPager) findViewById(R.id.guestContainer);
    searchView = (EditText) findViewById(R.id.guestSearch);
    searchSwitch = (Switch) findViewById(R.id.searchSwitch);
    TabLayout tabLayout = (TabLayout) findViewById(R.id.guestTab);
    tabLayout.setupWithViewPager(mViewPager);
    search_method = getIntent().getBooleanExtra("searchMethod", false);
    if (getIntent().getBooleanExtra("searchMethod", false)) {
//            Log.d(TAG, "onCreate: getIntent().getBooleanExtra(searchMethod, false)" + search_method);
      searchSwitch.setChecked(true);
    }
//        if (!searchView.getText().toString().isEmpty()) {
//            setupViewPager(mViewPager);
//        }
    final SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);


//        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
//        searchView.setIconifiedByDefault(false);
    searchView.setFocusable(true);
//        searchView.setIconified(false);
    searchView.requestFocusFromTouch();
    searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || !(searchView.getText().toString().isEmpty())) {
          Log.d(TAG, "onEditorAction: " + true);
          if (searchEnable(searchView)) {
            return true;
          }
          else {
            return false;
          }
        }
        return false;
      }
    });
  }

  private void setupViewPager(ViewPager viewPager) {
    SectPageAdapter adapter = new SectPageAdapter(getSupportFragmentManager());
    if (!search_method) {
      adapter.addFragment(new GuestVideosFragment(), "Videos");
      adapter.addFragment(new GuestArticlesFragment(), "Articles");
    }
    else {
      adapter.addFragment(new GuestVideosFragment(), "Videos");
      adapter.addFragment(new GuestArticlesFragment(), "Articles");
    }
    viewPager.setAdapter(adapter);
//        Log.d(TAG, "setupViewPager: Im also Here");
  }

  public boolean searchEnable(View view) {
    searchQuery = searchView.getText().toString();
    if (searchQuery.isEmpty()) {
      Toast.makeText(this, "Please Input an Item to Search", Toast.LENGTH_LONG).show();
      return false;
    }
    else {
      if (searchSwitch.isChecked()) {
        search_method = true;
      }
      else {
        search_method = false;
      }
//            Log.d(TAG, "searchEnable: " + searchQuery + " to be passed, Crawler method: " + search_method);
      setupViewPager(mViewPager);
      return true;
    }
  }

  public Bundle sendVideoData() {
    searchQuery = searchView.getText().toString();
    String data = searchQuery;
    Bundle videoBundle = new Bundle();
    videoBundle.putString("searchQuery", data);
    videoBundle.putBoolean("searchMethod", search_method);
    Log.d(TAG, "sendVideoData: I'm Here " + data + " " + searchQuery + " " + search_method);
    return videoBundle;
//        GuestVideosFragment videosFragment = new GuestVideosFragment();
//        videosFragment.setArguments(videoBundle);
//        Intent i = new Intent(this, GuestVideosFragment.class);
//        i.putExtra("searchQuery", data);
//        startActivity(i);
  }
}