package com.naturecurly.zimuzu;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
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
import com.naturecurly.zimuzu.Services.UpdateInfoService;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    private BottomBar bottomBar;
    private FragmentManager fragmentManager;
    private JobScheduler updateScheduler;
    private Fragment rankFragment;
    private Fragment newsFragment;
    private Fragment favFragment;
    private Fragment updateFragment;
    private Fragment searchFragment;
    private Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        JobInfo.Builder builder = new JobInfo.Builder(1, new ComponentName(getPackageName(), UpdateInfoService.class.getName()));
        builder.setPeriodic(900000);
        JobInfo jobInfo = builder.build();
        updateScheduler.cancelAll();
        updateScheduler.schedule(jobInfo);

        Fabric.with(this, new Crashlytics());
        new getDatabaseTask().execute(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);

        initFragments();
        fragmentManager = getSupportFragmentManager();
        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_rank:
                        changeFragment(rankFragment, fragmentManager);
                        break;
                    case R.id.tab_home:
                        changeFragment(newsFragment, fragmentManager);
                        break;
                    case R.id.tab_search:
                        changeFragment(searchFragment, fragmentManager);
                        break;
                    case R.id.tab_fav:
                        changeFragment(new FavFragment(), fragmentManager);

                        break;
                    case R.id.tab_update:
                        changeFragment(updateFragment, fragmentManager);
                        break;

                }
            }
        });

    }

    private void initFragments() {
        rankFragment = new RankFragment();
        newsFragment = new HomeFragment();
//        favFragment = new FavFragment();
        updateFragment = new UpdateFragment();
        searchFragment = new SearchFragment();
    }

    private void changeFragment(Fragment fragment, FragmentManager fragmentManager) {
        if (currentFragment != null) {
            if (!fragment.isAdded()) {
                fragmentManager.beginTransaction().hide(currentFragment).add(R.id.content_container, fragment).commit();
            } else {
                fragmentManager.beginTransaction().hide(currentFragment).show(fragment).commit();
            }
            currentFragment = fragment;
        } else {
            fragmentManager.beginTransaction().add(R.id.content_container, fragment).commit();
            currentFragment = fragment;
        }
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
