package com.naturecurly.zimuzu.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.naturecurly.zimuzu.Bean.News;
import com.naturecurly.zimuzu.Bean.NewsResponse;
import com.naturecurly.zimuzu.NetworkServices.NewsService;
import com.naturecurly.zimuzu.NewsActivity;
import com.naturecurly.zimuzu.Presenters.NewsPresenter;
import com.naturecurly.zimuzu.Presenters.NewsPresenterImpl;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.AccessUtils;
import com.naturecurly.zimuzu.Utils.DateUtils;
import com.naturecurly.zimuzu.Views.NewsView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by leveyleonhardt on 11/26/16.
 */

public class HomeFragment extends Fragment implements NewsView {
    private List<News> dataSet = new ArrayList<>();
    private RecyclerView recyclerView;
    private LinkedHashMap<String, String> typeMap = new LinkedHashMap<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager linearLayoutManager;
    private int visibleItemCount;
    private int totalItemCount;
    private int pastItems;
    private boolean loading;
    private int page;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        final NewsPresenter newsPresenter = new NewsPresenterImpl(this);
        page = 1;
        recyclerView = (RecyclerView) view.findViewById(R.id.news_recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.news_swipe_refresh);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                dataSet = new ArrayList<News>();
                newsPresenter.getNews(getActivity(), page);
//                fetchNews(page);
            }
        });
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new NewsAdapter(dataSet));
        loading = false;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (!loading) {
                    if ((pastItems + visibleItemCount) >= totalItemCount) {
                        loading = true;
                        page = page + 1;
                        swipeRefreshLayout.setRefreshing(true);
                        newsPresenter.getNews(getActivity(), page);

//                        fetchNews(page);
                        loading = false;
                    }
                }
            }
        });

        String[] keys = getResources().getStringArray(R.array.news_type_key);
        String[] values = getResources().getStringArray(R.array.news_type_value);
        for (int i = 0; i < keys.length; i++) {
            typeMap.put(keys[i], values[i]);
        }
        newsPresenter.getNews(getActivity(), page);
//        fetchNews(page);
        return view;
    }

    @Override
    public void updateRecyclerView(List newsData) {
        if (page == 1) {
            dataSet = newsData;
            recyclerView.setAdapter(new NewsAdapter(dataSet));
        } else if (page > 1) {
            dataSet.addAll(newsData);
            recyclerView.getAdapter().notifyDataSetChanged();
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void failGetData() {
        Toast.makeText(getActivity(), "Timeout, please try again.", Toast.LENGTH_SHORT).show();
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
        private List<News> dataSet = new ArrayList<>();

        public NewsAdapter(List<News> dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_news_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final News news = dataSet.get(position);
            holder.title.setText(typeMap.containsKey(news.getType()) ? "[" + typeMap.get(news.getType()) + "] " + news.getTitle() :
                    "" + news.getTitle());
            holder.date.setText(DateUtils.timestamp2String(news.getDateline()));
            if (news.getPoster() != null) {
//                Log.i("image", news.getPoster_a());
                Glide.with(getActivity()).load(news.getPoster()).centerCrop().into(holder.poster);
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), NewsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("id", news.getId());
                    intent.putExtras(bundle);
                    getActivity().startActivity(intent);
                }
            });
            Log.i("news", news.getId());
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private View itemView;
            private ImageView poster;
            private TextView title;
            private TextView date;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                poster = (ImageView) itemView.findViewById(R.id.news_list_poster);
                title = (TextView) itemView.findViewById(R.id.news_list_title);
                date = (TextView) itemView.findViewById(R.id.news_list_date);
            }
        }
    }

//    private void fetchNews(final int page) {
//        Retrofit retrofit = new Retrofit.Builder().addCallAdapterFactory(RxJavaCallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).baseUrl(getString(R.string.baseUrl)).build();
//        NewsService newsService = retrofit.create(NewsService.class);
//        Observable fetchNews = newsService.fetchNews(AccessUtils.generateAccessKey(getActivity()), "15", page + "");
//        fetchNews.retry(3)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<NewsResponse>() {
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
//                    public void onNext(NewsResponse newsResponse) {
//                        dataSet.addAll(newsResponse.getData());
//                        if (page != 1) {
//                            recyclerView.getAdapter().notifyDataSetChanged();
//                        } else {
//                            recyclerView.setAdapter(new NewsAdapter(dataSet));
//                        }
//                        if (swipeRefreshLayout != null) {
//                            swipeRefreshLayout.setRefreshing(false);
//                        }
//                    }
//                });
//
////        Call call = newsService.fetchNews(AccessUtils.generateAccessKey(getActivity()), "15", page + "");
////        call.enqueue(new Callback() {
////            @Override
////            public void onResponse(Call call, Response response) {
////                if (response.isSuccessful()) {
////                    NewsResponse newsResponse = (NewsResponse) response.body();
////                    dataSet.addAll(newsResponse.getData());
////                    if (page != 1) {
////                        recyclerView.getAdapter().notifyDataSetChanged();
////                    } else {
////                        recyclerView.setAdapter(new NewsAdapter(dataSet));
////                    }
////                    if (swipeRefreshLayout != null) {
////                        swipeRefreshLayout.setRefreshing(false);
////                    }
////                }
////            }
////
////            @Override
////            public void onFailure(Call call, Throwable t) {
////
////            }
////        });
//    }

}
