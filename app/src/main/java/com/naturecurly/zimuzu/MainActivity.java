package com.naturecurly.zimuzu;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.naturecurly.zimuzu.Fragments.HomeFragment;
import com.naturecurly.zimuzu.Fragments.RankFragment;
import com.naturecurly.zimuzu.Fragments.SearchFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {
    private BottomBar bottomBar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        fragmentManager = getSupportFragmentManager();
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_rank:
                        fragmentManager.beginTransaction().replace(R.id.content_container, new RankFragment()).commit();
                        break;
                    case R.id.tab_home:
                        fragmentManager.beginTransaction().replace(R.id.content_container, new HomeFragment()).commit();
                        break;
                    case R.id.tab_search:
                        fragmentManager.beginTransaction().replace(R.id.content_container, new SearchFragment()).commit();
                        break;
                }
            }
        });


//        Fragment fragment = fragmentManager.findFragmentById(R.id.content_container);
//        if (fragment == null) {
//            fragment = new RankFragment();
//            fragmentManager.beginTransaction().add(R.id.content_container, fragment).commit();
//        }
    }

}
