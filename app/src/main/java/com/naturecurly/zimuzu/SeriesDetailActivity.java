package com.naturecurly.zimuzu;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.naturecurly.zimuzu.Bean.DetailResponse;
import com.naturecurly.zimuzu.Bean.Series;
import com.naturecurly.zimuzu.NetworkServices.SeriesDetailService;
import com.naturecurly.zimuzu.Utils.AccessUtils;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_detail);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getString("id");
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
}
