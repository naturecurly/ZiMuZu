package com.naturecurly.zimuzu.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.naturecurly.zimuzu.NetworkServices.SearchService;
import com.naturecurly.zimuzu.R;
import com.naturecurly.zimuzu.SearchResultActivity;
import com.naturecurly.zimuzu.Utils.AccessUtils;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by leveyleonhardt on 11/26/16.
 */

public class SearchFragment extends Fragment {
    private EditText searchText;
    private Button searchButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        searchText = (EditText) view.findViewById(R.id.search_edit_text);
        searchButton = (Button) view.findViewById(R.id.search_button);
        if (searchText.getText().length() == 0) {
            searchButton.setEnabled(false);
        }
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    searchButton.setEnabled(false);
                } else {
                    searchButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(searchText.getText().toString());
            }
        });
        return view;
    }

    private void search(String keyword) {
        Intent intent = new Intent(getActivity(), SearchResultActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("k", keyword);
        bundle.putString("st", "");
        intent.putExtras(bundle);
        getActivity().startActivity(intent);

    }
}
