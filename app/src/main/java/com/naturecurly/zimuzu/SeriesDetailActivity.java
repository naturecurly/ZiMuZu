package com.naturecurly.zimuzu;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
    private Series series;
    private String id;
    private TextView chName;
    private TextView enName;
    private ImageView poster;
    private FrameLayout frameLayout;
    private TextView status;
    private TextView premiere;
    private TextView content;
    private TextView score;
    private TextView extraInfo;
    private ContentValues contentValues;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);
        transparentStatusBar();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DatabaseInstance.database.insert(FavTable.NAME, null, contentValues) > 0) {
                    Snackbar.make(view, "Added to your Favorite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    Snackbar.make(view, "You have added this series", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        frameLayout = (FrameLayout) findViewById(R.id.detail_frame);
        chName = (TextView) findViewById(R.id.detail_cnname);
        enName = (TextView) findViewById(R.id.detail_enname);
        poster = (ImageView) findViewById(R.id.detail_poster);
        status = (TextView) findViewById(R.id.detail_status);
        premiere = (TextView) findViewById(R.id.detail_premiere);
        content = (TextView) findViewById(R.id.detail_content);
        score = (TextView) findViewById(R.id.detail_score);
        extraInfo = (TextView) findViewById(R.id.detail_extra);
        getSeriesDetail();
    }


    private void getSeriesDetail() {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(getString(R.string.baseUrl)).build();
        SeriesDetailService seriesDetailService = retrofit.create(SeriesDetailService.class);
        Call call = seriesDetailService.getDetail(AccessUtils.generateAccessKey(this), id);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    DetailResponse detailResponse = (DetailResponse) response.body();
                    series = detailResponse.getData();
                    contentValues = DatabaseUtils.generateFavContentValues(series);
                    fab.setVisibility(View.VISIBLE);
                    fillInfo(series);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void fillInfo(Series seriesInfo) {
        chName.setText(seriesInfo.getCnname());
        enName.setText(seriesInfo.getEnname());
        status.setText(seriesInfo.getPlay_status());
        premiere.setText(seriesInfo.getPremiere());
        content.setText(seriesInfo.getContent());
        extraInfo.setText(getString(R.string.area) + seriesInfo.getArea() + "\n" + getString(R.string.category) + seriesInfo.getCategory() + "\n" + getString(R.string.channel) + seriesInfo.getChannel());
        score.setText(getString(R.string.score) + seriesInfo.getScore() + "/" + "10");
        Glide.with(this).load(seriesInfo.getPoster()).animate(R.animator.load_image).into(poster);
        Glide.with(this).load(seriesInfo.getPoster()).asBitmap().animate(R.animator.load_image).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                frameLayout.setBackground(new BitmapDrawable(getResources(), resource));
            }
        });
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
