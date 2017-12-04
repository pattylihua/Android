package com.example.travelinsingapore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MoreActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private Button setting;
    private Button favorite;
    private BottomNavigationView navigation;
    SharedPreferences sharedPref;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    intent = new Intent(MoreActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_plan:
                    intent = new Intent(MoreActivity.this, PlanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    return true;
                case R.id.navigation_more:

                    return true;
            }
            return false;
        }

    };

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_more);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        boolean checked = sharedPref.getBoolean(MainActivity.key[1],false);
        changeSomeAttribute(MainActivity.key[1], checked);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_more);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        setting = (Button)findViewById(R.id.settings);
        favorite = (Button)findViewById(R.id.favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MoreActivity.this, FavoriteActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MoreActivity.this, SettingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });
        checked = sharedPref.getBoolean(MainActivity.key[0],false);
        changeSomeAttribute(MainActivity.key[0], checked);
    }

    private void changeSomeAttribute(String key, boolean checked) {
        if(key.equals(MainActivity.key[1])) {
            if(checked) {
                setTheme(R.style.NightTheme);
            } else {
                setTheme(R.style.AppTheme);
            }
        } else if (key.equals(MainActivity.key[0])) {
            if (checked) {
                setting.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                favorite.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
            } else {
                setting.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                favorite.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            }
        }
    }
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        for(int i=0;i<MainActivity.key.length;i++) {
            boolean checked = sharedPref.getBoolean(MainActivity.key[i],false);
            changeSomeAttribute(MainActivity.key[i], checked);
        }
        recreate();
    }
}
