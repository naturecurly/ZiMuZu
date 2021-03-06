package com.naturecurly.zimuzu.Fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.naturecurly.zimuzu.Bean.Series;
import com.naturecurly.zimuzu.Bean.TopResponse;
import com.naturecurly.zimuzu.NetworkServices.RankService;
import com.naturecurly.zimuzu.Presenters.RankPresenter;
import com.naturecurly.zimuzu.Presenters.RankPresenterImpl;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.SeriesDetailActivity;
import com.naturecurly.zimuzu.Utils.AccessUtils;
import com.naturecurly.zimuzu.Views.RankView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by leveyleonhardt on 11/26/16.
 */

public class RankFragment extends Fragment implements RankView {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Series> dataSet = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);
        final RankPresenter rankPresenter = new RankPresenterImpl(this);
        recyclerView = (RecyclerView) view.findViewById(R.id.rank_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new CardAdapter(dataSet));
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rankPresenter.getRank(getActivity());
//                getTopContent();
            }
        });
        rankPresenter.getRank(getActivity());
//        getTopContent();
        return view;
    }

//    private void getTopContent() {
//        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).baseUrl(getString(R.string.baseUrl)).build();
//        RankService rankService = retrofit.create(RankService.class);
//        Map<String, String> paramsMap = AccessUtils.generateAccessKey(getActivity());
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        Observable<TopResponse> rank = rankService.getTop(paramsMap, sharedPreferences.getString(getString(R.string.rank_number_key), getString(R.string.rank_limit)));
//        rank.retry(3)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<TopResponse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Toast.makeText(getActivity(), "Network timeout, please retry.", Toast.LENGTH_SHORT).show();
//                        swipeRefreshLayout.setRefreshing(false);
//                    }
//
//                    @Override
//                    public void onNext(TopResponse response) {
//                        List<Series> series = response.getData();
//                        recyclerView.setAdapter(new CardAdapter(series));
//                        if (swipeRefreshLayout != null) {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                });
//
//    }

    @Override
    public void updateRecyclerView(List dataSet) {
        recyclerView.setAdapter(new CardAdapter(dataSet));
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void failGetData() {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "Timeout, please try again.", Toast.LENGTH_SHORT).show();
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
        private List<Series> dataSet;

        public CardAdapter(List<Series> data) {
            dataSet = data;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.cardview, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TextView textView = holder.textView;
            View holderView = holder.holderView;
            final Series series = dataSet.get(position);
            textView.setText((position + 1) + ". " + series.getCnname());
            holderView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SeriesDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", series.getId());
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;
            private View holderView;

            public ViewHolder(View itemView) {
                super(itemView);
                holderView = itemView;
                textView = (TextView) itemView.findViewById(R.id.card_text_view);
            }

        }
    }

}
