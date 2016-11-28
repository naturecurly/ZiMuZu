package com.naturecurly.zimuzu.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.naturecurly.zimuzu.Bean.DatabaseInstance;
import com.naturecurly.zimuzu.Bean.Favs;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.SeriesDetailActivity;
import com.naturecurly.zimuzu.Utils.DatabaseUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leveyleonhardt on 11/27/16.
 */

public class FavFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Favs> dataSet = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        dataSet = DatabaseUtils.fetchFavs(DatabaseInstance.database);
        recyclerView = (RecyclerView) view.findViewById(R.id.fav_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new FavAdapter(dataSet));
        return view;
    }

    public class FavAdapter extends RecyclerView.Adapter<FavAdapter.ViewHolder> {

        private List<Favs> dataSet = new ArrayList<>();

        public FavAdapter(List<Favs> dataSet) {
            this.dataSet = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_favs, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Favs favs = dataSet.get(position);
            holder.enname.setText(favs.getEnname());
            holder.cnname.setText(favs.getCnname());
            Glide.with(getActivity()).load(favs.getPoster()).into(holder.poster);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openDetail(favs.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private View itemView;
            private ImageView poster;
            private TextView cnname;
            private TextView enname;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
                cnname = (TextView) itemView.findViewById(R.id.fav_list_title);
                enname = (TextView) itemView.findViewById(R.id.fav_list_entitle);
                poster = (ImageView) itemView.findViewById(R.id.fav_list_poster);
            }
        }
    }

    private void openDetail(String id) {
        Intent intent = new Intent(getActivity(), SeriesDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
