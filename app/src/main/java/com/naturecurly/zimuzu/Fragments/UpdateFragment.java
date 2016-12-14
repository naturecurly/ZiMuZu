package com.naturecurly.zimuzu.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.naturecurly.zimuzu.Bean.Aria2Response;
import com.naturecurly.zimuzu.Bean.DatabaseInstance;
import com.naturecurly.zimuzu.Bean.Update;
import com.naturecurly.zimuzu.Bean.UpdateResponse;
import com.naturecurly.zimuzu.Databases.FavDataScheme.FavTable;
import com.naturecurly.zimuzu.Databases.UpdateDataScheme.UpdateTable;
import com.naturecurly.zimuzu.NetworkServices.DownloadService;
import com.naturecurly.zimuzu.NetworkServices.TodayUpdateService;
import com.naturecurly.zimuzu.Presenters.UpdatePresenter;
import com.naturecurly.zimuzu.Presenters.UpdatePresenterImpl;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.AccessUtils;
import com.naturecurly.zimuzu.Utils.Base64Utils;
import com.naturecurly.zimuzu.Utils.DatabaseUtils;
import com.naturecurly.zimuzu.Views.UpdateView;

import java.util.ArrayList;
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
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by leveyleonhardt on 11/29/16.
 */

public class UpdateFragment extends Fragment implements UpdateView {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Update> dataSet = new ArrayList<>();
    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        final UpdatePresenter updatePresenter = new UpdatePresenterImpl(this);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.update_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updatePresenter.updateDate(getActivity());
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.update_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new UpdateAdapter(dataSet));

        //read database
        updatePresenter.readData();
        return view;
    }

    @Override
    public void updateRecyclerView(List dataset) {
        recyclerView.setAdapter(new UpdateAdapter(dataset));
        swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void unchaged() {
        swipeRefreshLayout.setRefreshing(false);
    }

    public class UpdateAdapter extends RecyclerView.Adapter<UpdateAdapter.ViewHolder> {
        private List<Update> dataSet = new ArrayList<>();


        public UpdateAdapter(List<Update> dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_update, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Update update = dataSet.get(position);
            holder.cnname.setText(update.getCnname());
            holder.name.setText(update.getName());
            holder.season.setText("S" + update.getSeason() + "E" + update.getEpisode());

            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (update.getLink() == null || update.getLink().length() == 0) {
                        Snackbar.make(view, "No available link.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    } else {
                        remoteDownload(update.getLink());
                        Snackbar.make(view, "Download task has been pushed.", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                }

            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private View itemView;
            private TextView cnname;
            private TextView name;
            private TextView season;
            private Button download;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                cnname = (TextView) itemView.findViewById(R.id.update_cnname);
                name = (TextView) itemView.findViewById(R.id.update_name);
                season = (TextView) itemView.findViewById(R.id.update_season);
                download = (Button) itemView.findViewById(R.id.update_download);
            }
        }
    }


    private void remoteDownload(String url) {

        if (sharedPreferences.getString(getString(R.string.download_address_key), "").length() == 0 || sharedPreferences.getString(getString(R.string.download_address_key), "").length() == 0) {
            Toast.makeText(getActivity(), "Please configure your aria2 address.", Toast.LENGTH_SHORT).show();
        } else {
            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(sharedPreferences.getString(getString(R.string.download_address_key), "")).build();
            Log.i("download", sharedPreferences.getString(getString(R.string.download_address_key), ""));
            DownloadService downloadService = retrofit.create(DownloadService.class);
            String params = "[" + "\"" + "token:" + sharedPreferences.getString(getString(R.string.download_secret), "") + "\"" + "," + "[" + "\"" + url + "\"" + "]" + "]";
            Log.i("download", params);
            String codedParams = Base64Utils.encode(params);
            Log.i("download", codedParams);
            Call<Aria2Response> call = downloadService.download("aria2.addUri", "1", codedParams);
            Log.i("download", call.request().url().toString());
            call.enqueue(new Callback<Aria2Response>() {
                @Override
                public void onResponse(Call call, Response response) {
                    if (response.isSuccessful()) {
                        Log.i("download", "success");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.i("download", t.getMessage());
                }
            });
        }
    }
}
