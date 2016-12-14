package com.naturecurly.zimuzu.Fragments;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.naturecurly.zimuzu.Bean.DatabaseInstance;
import com.naturecurly.zimuzu.Bean.Series;
import com.naturecurly.zimuzu.Databases.FavDataScheme;
import com.naturecurly.zimuzu.Presenters.SeriesDetailPresenter;
import com.naturecurly.zimuzu.Presenters.SeriesDetailPresenterImpl;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.DatabaseUtils;
import com.naturecurly.zimuzu.Views.SeriesDetailView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by leveyleonhardt on 12/6/16.
 */

public class SeriesDetailFragment extends Fragment implements SeriesDetailView {
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
    private SeriesDetailPresenter seriesDetailPresenter;

    public static SeriesDetailFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString("id", id);
        SeriesDetailFragment fragment = new SeriesDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        id = bundle.getString("id", "");
        seriesDetailPresenter = new SeriesDetailPresenterImpl(this);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series_detail, container, false);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DatabaseInstance.database.insert(FavDataScheme.FavTable.NAME, null, contentValues) > 0) {
                    Snackbar.make(view, "Added to your Favorite", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    SharedPreferences preferences = getActivity().getSharedPreferences("zimuzu", MODE_PRIVATE);
                    preferences.edit().putInt("updateId", 0).commit();
                } else {
                    Snackbar.make(view, "You have added this series", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }

            }
        });
        frameLayout = (FrameLayout) view.findViewById(R.id.detail_frame);
        chName = (TextView) view.findViewById(R.id.detail_cnname);
        enName = (TextView) view.findViewById(R.id.detail_enname);
        poster = (ImageView) view.findViewById(R.id.detail_poster);
        status = (TextView) view.findViewById(R.id.detail_status);
        premiere = (TextView) view.findViewById(R.id.detail_premiere);
        content = (TextView) view.findViewById(R.id.detail_content);
        score = (TextView) view.findViewById(R.id.detail_score);
        extraInfo = (TextView) view.findViewById(R.id.detail_extra);
        seriesDetailPresenter.getSeriesData(getContext(), id);
        return view;
    }


    @Override
    public void updateView(Series seriesInfo) {
        contentValues = DatabaseUtils.generateFavContentValues(seriesInfo);
        fab.setVisibility(View.VISIBLE);

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

    @Override
    public void failToUpdate() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "Timeout, please try again.", Toast.LENGTH_SHORT).show();
        }
    }
}
