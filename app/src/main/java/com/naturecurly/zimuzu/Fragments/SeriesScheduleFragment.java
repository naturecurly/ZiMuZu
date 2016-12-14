package com.naturecurly.zimuzu.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.naturecurly.zimuzu.Bean.Episode;
import com.naturecurly.zimuzu.Bean.ScheduleData;
import com.naturecurly.zimuzu.Listeners.OnSwitchWatchedListener;
import com.naturecurly.zimuzu.Presenters.SchedulePresenter;
import com.naturecurly.zimuzu.Presenters.SchedulePresenterImpl;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.Views.ScheduleItem;
import com.naturecurly.zimuzu.Views.ScheduleView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by leveyleonhardt on 12/6/16.
 */

public class SeriesScheduleFragment extends Fragment implements ScheduleView, OnSwitchWatchedListener {

    private RecyclerView recyclerView;
    private SchedulePresenter schedulePresenter;
    private String id;
    private List<Episode> dataSet = new ArrayList<>();
    private TextView noSeasonHint;
    private Map<String, Set<String>> watchedMap = new HashMap<>();

    public static SeriesScheduleFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString("id", id);
        SeriesScheduleFragment fragment = new SeriesScheduleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        id = bundle.getString("id");
        schedulePresenter = new SchedulePresenterImpl(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_series_schedule, container, false);
        noSeasonHint = (TextView) view.findViewById(R.id.no_season_hint);
        noSeasonHint.setVisibility(View.GONE);
        recyclerView = (RecyclerView) view.findViewById(R.id.schedule_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ScheduleAdapter(dataSet, this));
        schedulePresenter.getSchedules(getActivity(), id);
        return view;
    }

    public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {
        private List<Episode> dataSet;
        private OnSwitchWatchedListener onSwitchWatchedListener;

        public ScheduleAdapter(List<Episode> dataSet, OnSwitchWatchedListener onSwitchWatchedListener) {
            this.dataSet = dataSet;
            this.onSwitchWatchedListener = onSwitchWatchedListener;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.item_episode, parent, false);
            ((ScheduleItem) view).setOnSwitchWatchedListener(onSwitchWatchedListener);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ScheduleItem item = (ScheduleItem) holder.itemView;
            Episode episode = dataSet.get(position);
            String seasonName = episode.getSeason();

            item.setSeasonNumber(seasonName);
            item.setCellNumber(Integer.parseInt(episode.getEpisode()));
            if (watchedMap.containsKey(episode.getSeason())) {
                Set<String> set = watchedMap.get(episode.getSeason());
                item.setWatched(set);
            }
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private View itemView;

            public ViewHolder(View itemView) {
                super(itemView);
                this.itemView = itemView;
            }
        }

    }


    @Override
    public void watched(ScheduleItem.Cell cell) {
        if (watchedMap.containsKey(cell.getSeason())) {
            Set<String> watchedSet = watchedMap.get(cell.getSeason());
            watchedSet.add(cell.getEpisode());
        } else {
            Set<String> watchedSet = new HashSet<>();
            watchedSet.add(cell.getEpisode());
            watchedMap.put(cell.getSeason(), watchedSet);
        }
        schedulePresenter.writeData(id, watchedMap);

        Toast.makeText(getActivity(), "watched", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void unWatched(ScheduleItem.Cell cell) {
        Log.i("unwatched", cell.getSeason() + "," + cell.getEpisode());
        Set<String> watchedSet = watchedMap.get(cell.getSeason());
        for (String s : watchedSet) {
            if (s.equals(cell.getEpisode())) {
                watchedSet.remove(s);
                break;
            }
        }
        schedulePresenter.writeData(id, watchedMap);
        Toast.makeText(getActivity(), "unWatched", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateRecyclerView(ScheduleData data) {
        watchedMap = data.getLocalData();
        dataSet = data.getRemoteData().getData();
        Log.i("getSchedule", watchedMap.size() + "");

        if (dataSet.size() != 0) {
            noSeasonHint.setVisibility(View.GONE);
            List<Episode> newDataSet = new ArrayList<>();
            for (Episode e : (List<Episode>) dataSet) {
                if (Integer.parseInt(e.getSeason()) <= 100) {
                    newDataSet.add(0, e);
                } else {
                    newDataSet.add(newDataSet.size(), e);
                }
            }
            recyclerView.setAdapter(new ScheduleAdapter(newDataSet, this));
        } else {
            noSeasonHint.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void failGetData() {
        noSeasonHint.setVisibility(View.VISIBLE);

    }

    @Override
    public void fillWatchedData(Map map) {

    }

}
