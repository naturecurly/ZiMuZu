package com.naturecurly.zimuzu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.naturecurly.zimuzu.Bean.NewsDetail;
import com.naturecurly.zimuzu.Bean.NewsDetailResponse;
import com.naturecurly.zimuzu.NetworkServices.NewsDetailService;
import com.naturecurly.zimuzu.Utils.AccessUtils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class NewsActivity extends AppCompatActivity {
    private String id;
    private TextView newsContent;
    private LinearLayout newsFrame;
    private TextView newsTitle;
    private ImageView newsTitleBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getString("id");
        setContentView(R.layout.activity_news);
        newsContent = (TextView) findViewById(R.id.news_content);
        newsFrame = (LinearLayout) findViewById(R.id.news_activity_frame);
        newsTitle = (TextView) findViewById(R.id.news_detail_title);
        newsTitleBackground = (ImageView) findViewById(R.id.news_title_background);
        newsContent.setTextIsSelectable(true);
        getContent();
    }

    private void getContent() {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(getString(R.string.baseUrl)).build();
        NewsDetailService newsDetailService = retrofit.create(NewsDetailService.class);
        Call call = newsDetailService.getInfo(AccessUtils.generateAccessKey(getApplicationContext()), id);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    NewsDetailResponse newsDetailResponse = (NewsDetailResponse) response.body();
                    NewsDetail newsDetail = newsDetailResponse.getData();
                    fillContent(newsDetail.getTitle(), newsDetail.getContent(), newsDetail.getPoster());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    private void fillContent(String title, String content, String poster) {

        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByTag("p");
        StringBuilder sb = new StringBuilder();
        for (Element e : elements) {
            sb.append(e.text() + "\n");
        }
        newsContent.setText(sb.toString());
        newsTitle.setText(title);
        Glide.with(getApplicationContext()).load(poster).centerCrop().animate(R.animator.load_image).into(newsTitleBackground);
//        Glide.with(getApplicationContext()).load(poster).asBitmap().animate(R.animator.load_image).into(new SimpleTarget<Bitmap>() {
//            @Override
//            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
//                newsFrame.setBackground(new BitmapDrawable(getResources(), resource));
//            }
//        });
    }
}
