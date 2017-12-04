package com.example.travelinsingapore;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    private EditText place;
    private Button search;
    public static final String KEY = "";
    static final String[] key = {"LargeFontKey","NightModeKey","FastPlanKey"};
    SharedPreferences sharedPref;
    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    return true;
                case R.id.navigation_plan:
                    intent = new Intent(MainActivity.this, PlanActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                case R.id.navigation_more:
                    intent = new Intent(MainActivity.this,MoreActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        boolean checked = sharedPref.getBoolean(key[1],false);
        changeSomeAttribute(key[1], checked);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        place = (EditText)findViewById(R.id.searchPlace);
        search = (Button)findViewById(R.id.search);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        checked = sharedPref.getBoolean(key[0],false);
        changeSomeAttribute(key[0], checked);
    }

    private void changeSomeAttribute(String key, boolean checked) {
        if (key.equals(this.key[1])) {
            if (checked) {
                setTheme(R.style.NightTheme);
            }
        } else if (key.equals(this.key[0])) {
            if (checked) {
                place.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                search.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
            } else {
                place.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                search.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
            }
        }
    }

    public void onResume() {
        super.onResume();
        navigation.setSelectedItemId(R.id.navigation_search);
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    public void search(View v) {
        Context context = v.getContext();
        Intent intent = new Intent(context, MapsActivity.class);
        String message = place.getText().toString();
        intent.putExtra(KEY,message);
        startActivity(intent);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        for(int i=0;i<this.key.length;i++) {
            boolean checked = sharedPreferences.getBoolean(this.key[i],false);
            changeSomeAttribute(this.key[i], checked);
            recreate();
        }
    }
}
