package com.naturecurly.zimuzu;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.naturecurly.zimuzu.Fragments.SettingFragment;

/**
 * Created by leveyleonhardt on 11/29/16.
 */

public class SettingActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_container);
        FragmentManager fragmentManager = getFragmentManager();

        Fragment fragment = fragmentManager.findFragmentById(R.id.setting_container);
        if (fragment == null) {
            fragment = new SettingFragment();
            fragmentManager.beginTransaction().add(R.id.setting_container, fragment).commit();
        }
    }
}
