package com.example.taquio.trasearch.Samok;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.taquio.trasearch.Messages.InboxFragment;
import com.example.taquio.trasearch.R;

public class ReportViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_view);

        setupViewPager();

    }

    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new ReportViewFragment());
        ViewPager viewPager = findViewById(R.id.messageContainer);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = findViewById(R.id.messageTabLayout);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setText("Reports");
    }
}
