package com.example.rezak.magicsquare;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.net.Uri;
import android.widget.Spinner;
import android.widget.Toast;

public class Acceuil extends AppCompatActivity {
    Bundle VonSaveInstanceState;
    Spinner LevelSelector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceuil);
        LevelSelector = findViewById(R.id.Spin);
        VonSaveInstanceState = savedInstanceState;
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            this.onSaveInstanceState(this.VonSaveInstanceState);
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            this.onRestoreInstanceState(this.VonSaveInstanceState);
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }

    public void nouv(View v){
        Intent game = new Intent(Acceuil.this, MainActivity.class);
        int level = 0;
        int SelectedLevel = this.LevelSelector.getSelectedItemPosition();
        if(SelectedLevel == 0){
             level = 6;
        }else if (SelectedLevel == 1 ){
             level = 3;
        }
        game.putExtra("level",level);
        startActivity(game);
    }
    public void about(View v){
        String url = "https://en.wikipedia.org/wiki/Magic_square";
        Intent NaviAbout = new Intent( Intent.ACTION_VIEW, Uri.parse( url ) );
        startActivity(NaviAbout);
    }
    public void best(View v){
        Intent BestScore = new Intent(Acceuil.this, BestscoreActivity.class);
        startActivity(BestScore);

    }

}
