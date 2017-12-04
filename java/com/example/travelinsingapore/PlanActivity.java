package com.example.travelinsingapore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class PlanActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener{

    CheckBox MBS;
    CheckBox SF;
    CheckBox VC;
    CheckBox RWS;
    CheckBox BTRC;
    CheckBox Z;
    Button clicked;
    TextView text;
    EditText budget;
    private BottomNavigationView navigation;
    SharedPreferences sharedPref;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent;
            switch (item.getItemId()) {
                case R.id.navigation_search:
                    intent = new Intent(PlanActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    return true;
                case R.id.navigation_plan:
                    return true;

                case R.id.navigation_more:
                    intent = new Intent(PlanActivity.this, MoreActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
        navigation.setSelectedItemId(R.id.navigation_plan);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPref.registerOnSharedPreferenceChangeListener(this);
        boolean checked = sharedPref.getBoolean(MainActivity.key[1],false);
        changeSomeAttribute(MainActivity.key[1], checked);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);

        navigation = (BottomNavigationView)findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_plan);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        text = (TextView)findViewById(R.id.text);
        MBS = (CheckBox)findViewById(R.id.MBS);
        SF = (CheckBox)findViewById(R.id.SF);
        VC = (CheckBox)findViewById(R.id.VC);
        RWS = (CheckBox)findViewById(R.id.RWS);
        BTRC = (CheckBox)findViewById(R.id.BTRC);
        Z = (CheckBox)findViewById(R.id.Z);
        clicked = (Button)findViewById(R.id.clicked);
        budget = (EditText) findViewById(R.id.budget);

        clicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = "";
                double money = Double.parseDouble(budget.getText().toString());
                boolean checked = sharedPref.getBoolean(MainActivity.key[2],false);
                if(!checked){
                    bruteForce solver = new bruteForce(MBS.isChecked(),SF.isChecked(),VC.isChecked(),
                            RWS.isChecked(),BTRC.isChecked(),Z.isChecked(),money,v.getContext());
                    message = solver.brutesolve();
                } else {
                    SimulatedAnnealing fsolver = new SimulatedAnnealing(money, MBS.isChecked(),SF.isChecked(),VC.isChecked(),
                            RWS.isChecked(),BTRC.isChecked(),Z.isChecked());
                    try{
                    message = fsolver.solve();
                    } catch (Exception ex){
                        Log.i("Yuanzhi",ex.getMessage());
                    }
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());;
                builder.setMessage(message);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                clicked.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                MBS.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                SF.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                VC.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                RWS.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                BTRC.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                Z.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                TextView ybudget = (TextView)findViewById(R.id.ybudget);
                ybudget.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
                budget.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
            } else {
                text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                clicked.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                MBS.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                SF.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                VC.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                RWS.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                BTRC.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                Z.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                TextView ybudget = (TextView)findViewById(R.id.ybudget);
                ybudget.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
                budget.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
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
