package com.example.rezak.magicsquare;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

public class BestscoreActivity extends AppCompatActivity {
    TextView tv1,tv2,tv3,tv4,tv5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //*********** instantiate sharedPreferences
        setContentView(R.layout.activity_bestscore);
        SharedPreferences sharedPreferences = getSharedPreferences("BestScore", MODE_PRIVATE);

        //*********** get widget
        this.tv1 = findViewById(R.id.tv1);
        this.tv2 = findViewById(R.id.tv2);
        this.tv3 = findViewById(R.id.tv3);
        this.tv4 = findViewById(R.id.tv4);
        this.tv5 = findViewById(R.id.tv5);

        //*********** get all best scores from sharedprefernces
        long _1 = sharedPreferences.getLong("1",100);
        long _2 = sharedPreferences.getLong("2",100);
        long _3 = sharedPreferences.getLong("3",100);
        long _4 = sharedPreferences.getLong("4",100);
        long _5 = sharedPreferences.getLong("5",100);

        long[] anum = {_1,_2,_3,_4,_5};
        //*********** set all TextView text

        Arrays.sort(anum);
        tv1.setText("1. "+anum[0]);
        tv2.setText("2. "+anum[1]);
        tv3.setText("3. "+anum[2]);
        tv4.setText("4. "+anum[3]);
        tv5.setText("5. "+anum[4]);



    }
    public void backToHome(View v){
        finish();
    }
}
