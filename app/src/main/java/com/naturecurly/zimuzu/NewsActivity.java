package com.naturecurly.zimuzu;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.naturecurly.zimuzu.Bean.NewsDetail;
import com.naturecurly.zimuzu.Bean.NewsDetailResponse;
import com.naturecurly.zimuzu.NetworkServices.NewsDetailService;
import com.naturecurly.zimuzu.Presenters.NewsContentPresenter;
import com.naturecurly.zimuzu.Presenters.NewsContentPresenterImpl;
import com.naturecurly.zimuzu.Utils.AccessUtils;
import com.naturecurly.zimuzu.Utils.DensityUtil;
import com.naturecurly.zimuzu.Utils.StatusBarUtils;
import com.naturecurly.zimuzu.Views.NewsContentView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class NewsActivity extends AppCompatActivity implements NewsContentView {
    private String id;
    private LinearLayout newsContainer;
    private TextView newsTitle;
    private ImageView newsTitleBackground;
    private SharedPreferences sharedPreferences;
    private FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NewsContentPresenter newsContentPresenter = new NewsContentPresenterImpl(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            id = bundle.getString("id");
        } else {
            Uri data = intent.getData();
            String scheme = data.getScheme();
            String host = data.getHost();
            List<String> params = data.getPathSegments();
            String first = params.get(0);
            String second = params.get(1);
            Log.i("news", scheme + host + first + second);
            id = second;
        }
        setContentView(R.layout.activity_news);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        StatusBarUtils.transparentStatusBar(this);
        newsContainer = (LinearLayout) findViewById(R.id.news_activity_container);
        newsTitle = (TextView) findViewById(R.id.news_detail_title);
        newsTitleBackground = (ImageView) findViewById(R.id.news_title_background);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab_share);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, "http://www.zimuzu.tv/article/" + id + "\n分享一篇文章《" + newsTitle.getText() + "》" + "\n" + "(请使用自带浏览器打开此链接)");
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getResources().getText(R.string.send_to)));
            }
        });
        newsContentPresenter.getNewsContent(this, id);
//        getContent();
    }

//    private void getContent() {
//        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(getString(R.string.baseUrl)).build();
//        NewsDetailService newsDetailService = retrofit.create(NewsDetailService.class);
//        Call call = newsDetailService.getInfo(AccessUtils.generateAccessKey(getApplicationContext()), id);
//        call.enqueue(new Callback() {
//            @Override
//            public void onResponse(Call call, Response response) {
//                if (response.isSuccessful()) {
//                    NewsDetailResponse newsDetailResponse = (NewsDetailResponse) response.body();
//                    NewsDetail newsDetail = newsDetailResponse.getData();
//                    fillContent(newsDetail.getTitle(), newsDetail.getContent(), newsDetail.getPoster());
//                }
//            }
//
//            @Override
//            public void onFailure(Call call, Throwable t) {
//
//            }
//        });
//    }

    @Override
    public void fillContent(String title, String content, String poster) {

        Document document = Jsoup.parse(content);
        Elements elements = document.getElementsByTag("p");
        StringBuilder sb = new StringBuilder();
        for (Element e : elements) {
            if (e.getElementsByTag("img").hasAttr("src") && !(sharedPreferences.getBoolean(getString(R.string.data_saver_key), false))) {
                String url = e.getElementsByTag("img").attr("src");
                ImageView image = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(DensityUtil.dip2px(this, 16), DensityUtil.dip2px(this, 16), DensityUtil.dip2px(this, 16), DensityUtil.dip2px(this, 16));
                image.setLayoutParams(params);
                Glide.with(this).load(url).fitCenter().crossFade().into(image);
                newsContainer.addView(image);
            }
            TextView text = new TextView(this);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(DensityUtil.dip2px(this, 16), 0, DensityUtil.dip2px(this, 16), 0);
            text.setLayoutParams(textParams);
            text.setTextColor(Color.WHITE);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, Integer.parseInt(sharedPreferences.getString(getString(R.string.text_size_key), "15")));
            text.setText(e.text());
            text.setTextIsSelectable(true);
            newsContainer.addView(text);
        }
        newsTitle.setText(title);
        Glide.with(getApplicationContext()).load(poster).centerCrop().animate(R.animator.load_image).into(newsTitleBackground);
    }


    @Override
    public void failGetData() {
        if (this != null) {
            Toast.makeText(this, "Timeout, please try again.", Toast.LENGTH_SHORT).show();
        }
    }

}
