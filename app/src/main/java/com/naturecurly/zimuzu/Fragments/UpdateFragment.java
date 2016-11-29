package com.naturecurly.zimuzu.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.naturecurly.zimuzu.Bean.DatabaseInstance;
import com.naturecurly.zimuzu.Bean.Update;
import com.naturecurly.zimuzu.Bean.UpdateResponse;
import com.naturecurly.zimuzu.Databases.FavDataScheme.FavTable;
import com.naturecurly.zimuzu.Databases.UpdateDataScheme.UpdateTable;
import com.naturecurly.zimuzu.NetworkServices.TodayUpdateService;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Utils.AccessUtils;
import com.naturecurly.zimuzu.Utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 11/29/16.
 */

public class UpdateFragment extends Fragment {
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private List<Update> dataSet = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        readFromDatabase();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.update_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getUpdate();
            }
        });
        recyclerView = (RecyclerView) view.findViewById(R.id.update_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new UpdateAdapter(dataSet));
        return view;
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
            Update update = dataSet.get(position);
            holder.cnname.setText(update.getCnname());
            holder.name.setText(update.getName());
            holder.season.setText("S" + update.getSeason() + "E" + update.getEpisode());
            holder.download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "to be developed", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
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


    private void getUpdate() {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(getString(R.string.baseUrl)).build();
        TodayUpdateService todayUpdateService = retrofit.create(TodayUpdateService.class);
        Call call = todayUpdateService.getUpdate(AccessUtils.generateAccessKey(getActivity()));
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    UpdateResponse updateResponse = (UpdateResponse) response.body();
                    List<Update> updateList = updateResponse.getData();
                    for (Update item : updateList) {
                        Cursor cursor = DatabaseInstance.database.query(FavTable.NAME, null, FavTable.Cols.ID + "= ?", new String[]{item.getResourceid()}, null, null, null);
                        if (cursor.getCount() != 0) {
                            DatabaseInstance.database.insert(UpdateTable.NAME, null, DatabaseUtils.generateUpdateContentValues(item));
                        }
                    }
                    readFromDatabase();
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }


    private void readFromDatabase() {
        dataSet = new ArrayList<>();
        Cursor cursor = DatabaseInstance.database.query(UpdateTable.NAME, null, null, null, null, null, "_id" + " DESC");
        int idIndex = cursor.getColumnIndex(UpdateTable.Cols.ID);
        int resourceIndex = cursor.getColumnIndex(UpdateTable.Cols.RESOURCE);
        int nameIndex = cursor.getColumnIndex(UpdateTable.Cols.NAME);
        int formatIndex = cursor.getColumnIndex(UpdateTable.Cols.FORMAT);
        int seasonIndex = cursor.getColumnIndex(UpdateTable.Cols.SEASON);
        int episodeIndex = cursor.getColumnIndex(UpdateTable.Cols.EPISODE);
        int sizeIndex = cursor.getColumnIndex(UpdateTable.Cols.SIZE);
        int cnnameIndex = cursor.getColumnIndex(UpdateTable.Cols.CNNAME);
        int channelIndex = cursor.getColumnIndex(UpdateTable.Cols.CHANNEL);
        int linkIndex = cursor.getColumnIndex(UpdateTable.Cols.WAY);

        while (cursor.moveToNext()) {
            Update update = new Update(cursor.getString(idIndex), cursor.getString(resourceIndex), cursor.getString(nameIndex), cursor.getString(formatIndex), cursor.getString(seasonIndex), cursor.getString(episodeIndex), cursor.getString(sizeIndex), cursor.getString(cnnameIndex), cursor.getString(channelIndex), cursor.getString(linkIndex));
            dataSet.add(update);
        }
    }
}
