package com.radusalagean.quickdotsviewsample;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.radusalagean.quickdotsview.QuickDotsView;

public class MainActivity extends AppCompatActivity {

    private static final int PAGES = 4;

    private ViewPager viewPager;
    private QuickDotsView quickDotsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = findViewById(R.id.view_pager);
        quickDotsView = findViewById(R.id.quick_dots_view);
        viewPager.setAdapter(new PagerAdapter(PAGES));
    }

    @Override
    protected void onStart() {
        super.onStart();
        quickDotsView.linkViewPager(viewPager);
    }

    @Override
    protected void onStop() {
        quickDotsView.unlinkViewPager(viewPager);
        super.onStop();
    }
}
