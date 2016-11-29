package com.naturecurly.zimuzu;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.crashlytics.android.Crashlytics;
import com.naturecurly.zimuzu.Bean.DatabaseInstance;
import com.naturecurly.zimuzu.Databases.DatabaseOpenHelper;
import com.naturecurly.zimuzu.Fragments.FavFragment;
import com.naturecurly.zimuzu.Fragments.HomeFragment;
import com.naturecurly.zimuzu.Fragments.RankFragment;
import com.naturecurly.zimuzu.Fragments.SearchFragment;
import com.naturecurly.zimuzu.Fragments.UpdateFragment;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    private BottomBar bottomBar;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Fabric.with(this, new Crashlytics());
        new getDatabaseTask().execute(getApplicationContext());
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
                    case R.id.tab_fav:
                        fragmentManager.beginTransaction().replace(R.id.content_container, new FavFragment()).commit();
                        break;
                    case R.id.tab_update:
                        fragmentManager.beginTransaction().replace(R.id.content_container, new UpdateFragment()).commit();
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

    private class getDatabaseTask extends AsyncTask<Context, Void, Void> {

        @Override
        protected Void doInBackground(Context... params) {
            DatabaseInstance.database = new DatabaseOpenHelper(params[0]).getWritableDatabase();
            if (DatabaseInstance.database != null)
                Log.i("xxx", "database created");
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_setting:
                Intent intent = new Intent(this, SettingActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
