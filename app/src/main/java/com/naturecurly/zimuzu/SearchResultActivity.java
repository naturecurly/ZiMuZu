package com.naturecurly.zimuzu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.naturecurly.zimuzu.Bean.SearchResponse;
import com.naturecurly.zimuzu.Bean.SearchResult;
import com.naturecurly.zimuzu.NetworkServices.SearchService;
import com.naturecurly.zimuzu.Utils.AccessUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class SearchResultActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private String keyword;
    private String option;
    private List<SearchResult> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        Bundle bundle = getIntent().getExtras();
        keyword = bundle.getString("k");
        option = bundle.getString("st");
        recyclerView = (RecyclerView) findViewById(R.id.search_result_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new SearchResultAdapter(dataSet));
        search(keyword, option);
    }

    private void search(String keyword, String option) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(getString(R.string.baseUrl)).build();
        SearchService searchService = retrofit.create(SearchService.class);
        Call call = searchService.search(AccessUtils.generateAccessKey(this), keyword, option);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    SearchResponse searchResponse = (SearchResponse) response.body();
                    dataSet = searchResponse.getData().getList();
                    recyclerView.setAdapter(new SearchResultAdapter(dataSet));
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

        private List<SearchResult> dataSet;

        public SearchResultAdapter(List<SearchResult> dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_search_result, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final SearchResult searchResult = dataSet.get(position);
            String channel = searchResult.getChannel().equals("") ? "sub" : searchResult.getChannel();

            holder.titleText.setText("[" + channel + "] " + searchResult.getTitle());
            holder.typeText.setText(searchResult.getType());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (searchResult.getType().equals(getString(R.string.resource))) {
                        openDetail(searchResult.getItemId());
                    } else if (searchResult.getType().equals(getString(R.string.subtitle))) {

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
            private TextView titleText;
            private TextView typeText;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                titleText = (TextView) itemView.findViewById(R.id.item_search_result_title);
                typeText = (TextView) itemView.findViewById(R.id.item_search_result_type);
            }
        }


    }

    private void openDetail(String id) {
        Intent intent = new Intent(this, SeriesDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
