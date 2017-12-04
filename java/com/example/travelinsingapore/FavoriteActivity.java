package com.example.travelinsingapore;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    ArrayList<MapsActivity.AddressInfo> data = new ArrayList<>();
    SharedPreferences sharedPref;
    private RecyclerView favoriteList;
    private FavoriteAdapter mAdapter;
    private mfDbHelper DbHelper;
    public SQLiteDatabase mfDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        boolean checked = sharedPref.getBoolean(MainActivity.key[1],false);
        changeSomeAttribute(MainActivity.key[1], checked);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        DbHelper = new mfDbHelper(this);
        mfDb = DbHelper.getWritableDatabase();
        getData();
        //Toast.makeText(this,data.get(0).name,Toast.LENGTH_LONG).show();
        favoriteList = (RecyclerView) findViewById(R.id.recyclerViewAnime);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        favoriteList.setLayoutManager(linearLayoutManager);
        mAdapter = new FavoriteAdapter(this, data, this);
        favoriteList.setAdapter(mAdapter);
    }

    private void changeSomeAttribute(String key, boolean checked) {
        if (key.equals(MainActivity.key[1])) {
            if (checked) {
                setTheme(R.style.NightTheme);
            }
        }
    }

    public  void getData() {
        Cursor cursor = mfDb.rawQuery("SELECT * FROM " + MyFavorites.Entry.TABLE_NAME, null);
        int indexName = cursor.getColumnIndex(MyFavorites.Entry.COL_NAME);
        int indexAddress = cursor.getColumnIndex(MyFavorites.Entry.COL_ADRESS);
        while(cursor.moveToNext()){
            String myName = cursor.getString(indexName);
            String myAddress = cursor.getString(indexAddress);
            data.add(new MapsActivity.AddressInfo(myName,myAddress));
        }
    }

    public void onPause() {
        super.onPause();
        overridePendingTransition(0, 0);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}
