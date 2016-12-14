package com.naturecurly.zimuzu;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.naturecurly.zimuzu.Bean.DatabaseInstance;
import com.naturecurly.zimuzu.Bean.DetailResponse;
import com.naturecurly.zimuzu.Bean.Series;
import com.naturecurly.zimuzu.Databases.FavDataScheme.FavTable;
import com.naturecurly.zimuzu.Fragments.SeriesDetailFragment;
import com.naturecurly.zimuzu.Fragments.SeriesScheduleFragment;
import com.naturecurly.zimuzu.NetworkServices.SeriesDetailService;
import com.naturecurly.zimuzu.Utils.AccessUtils;
import com.naturecurly.zimuzu.Utils.DatabaseUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class SeriesDetailActivity extends AppCompatActivity {
//    private Series series;
//    private String id;
//    private TextView chName;
//    private TextView enName;
//    private ImageView poster;
//    private FrameLayout frameLayout;
//    private TextView status;
//    private TextView premiere;
//    private TextView content;
//    private TextView score;
//    private TextView extraInfo;
//    private ContentValues contentValues;
//    private FloatingActionButton fab;


    private ViewPager viewPager;
    private Fragment detailFragment;
    private Fragment scheduleFragment;
    private String id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);
        transparentStatusBar();
        viewPager = (ViewPager) findViewById(R.id.series_detail_view_pager);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
//        detailFragment = SeriesDetailFragment.newInstance(id);
//        scheduleFragment = new SeriesDetailFragment();
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));

    }

    public class PagerAdapter extends FragmentPagerAdapter {
        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return SeriesDetailFragment.newInstance(id);
            } else {
                return SeriesScheduleFragment.newInstance(id);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

    private void transparentStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
