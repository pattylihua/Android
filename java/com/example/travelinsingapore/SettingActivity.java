package com.example.travelinsingapore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class SettingActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        boolean checked = sharedPref.getBoolean(MainActivity.key[1],false);
        changeSomeAttribute(MainActivity.key[1], checked);
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        for(int i=0;i<MainActivity.key.length;i++) {
            boolean checked = sharedPref.getBoolean(MainActivity.key[i],false);
            changeSomeAttribute(MainActivity.key[i], checked);
        }
        recreate();
    }

    private void changeSomeAttribute(String key, boolean checked) {
        if(key.equals(MainActivity.key[1])) {
            if(checked) {
                setTheme(R.style.NightTheme);
            } else {
                setTheme(R.style.AppTheme);
            }
        }
    }
}
