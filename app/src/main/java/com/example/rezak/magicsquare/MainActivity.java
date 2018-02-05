package com.example.rezak.magicsquare;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Map;
import java.util.Random;
import java.util.HashMap;



public class MainActivity extends Activity  {
    TextView rc1,rc2,rc3,rl1,rl2,rl3;
    Chronometer chrono;
    int COUNT_HELP;
    boolean Vresume = false;
    long baseCrono ;
    Button help,submit,back,Bresume;
    EditText l0c0,l0c1,l0c2,l1c0,l1c1,l1c2,l2c0,l2c1,l2c2;
    HashMap<String, EditText> list = new HashMap<>();
    int[][] _MagicSquare ;//= generate_Square();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent in = getIntent();
        int level = in.getIntExtra("level",0);
        super.onCreate(savedInstanceState);
        this.COUNT_HELP = 10;
        setContentView(R.layout.activity_main);
        // GENERATE A  MAGIC_SQUARE
        if(savedInstanceState ==null){
            Log.d("#####################", "instancestate is null **** ");
            this._MagicSquare = generate_Square();
        }

        // GET ALL WIDGET
        this.rc1 = (TextView)findViewById(R.id.rc1);
        this.rc2 = (TextView)findViewById(R.id.rc2);
        this.rc3 = (TextView)findViewById(R.id.rc3);
        this.rl1 = (TextView)findViewById(R.id.rl1);
        this.rl2 = (TextView)findViewById(R.id.rl2);
        this.rl3 = (TextView)findViewById(R.id.rl3);

        this.l0c0 = findViewById(R.id.l0c0);
        this.l0c1 = findViewById(R.id.l0c1);
        this.l0c2 = findViewById(R.id.l0c2);
        this.l1c0 = findViewById(R.id.l1c0);
        this.l1c1 = findViewById(R.id.l1c1);
        this.l1c2 = findViewById(R.id.l1c2);
        this.l2c0 = findViewById(R.id.l2c0);
        this.l2c1 = findViewById(R.id.l2c1);
        this.l2c2 = findViewById(R.id.l2c2);

        this.chrono = findViewById(R.id.chrno);


        help = findViewById(R.id.help);
        submit = findViewById(R.id.submit);
        Bresume = findViewById(R.id.cont);
        Bresume.setEnabled(this.Vresume);

        back = findViewById(R.id.back);
        submit.setEnabled(false);
        //put all editText in the map, then after we can use a foreach to get them
        this.list.put("l0c0",l0c0);
        this.list.put("l0c1",l0c1);
        this.list.put("l0c2",l0c2);
        this.list.put("l1c0",l1c0);
        this.list.put("l1c1",l1c1);
        this.list.put("l1c2",l1c2);
        this.list.put("l2c0",l2c0);
        this.list.put("l2c1",l2c1);
        this.list.put("l2c2",l2c2);
        for (final Map.Entry<String, EditText> element : this.list.entrySet()) {

            final EditText champ = (EditText) element.getValue();
            champ.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    int line = Character.getNumericValue(element.getKey().charAt(1));
                    int column = Character.getNumericValue(element.getKey().charAt(3));
                    if(!champ.getText().toString().equals("")){
                        int value = getDigit(champ,null);
                        if (thereIsDuplic(element.getKey(),value)){
                            //champ.setBackgroundColor(Color.RED);
                            champ.setTextColor(Color.RED);
                        }


                    }

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(GridIsFull()){
                        submit.setEnabled(true);
                    }else{
                        submit.setEnabled(false);
                    }
                }
            });

        }



        //COMPUTE RESULT OF THE GENERATED MAGIC SQUARE
        if(savedInstanceState == null){
            rc1.setText(SumLigneOrColumn(this._MagicSquare,0,-1));
            rc2.setText(SumLigneOrColumn(this._MagicSquare,1,-1));
            rc3.setText(SumLigneOrColumn(this._MagicSquare,2,-1));
            rl1.setText(SumLigneOrColumn(this._MagicSquare,-1,0));
            rl2.setText(SumLigneOrColumn(this._MagicSquare,-1,1));
            rl3.setText(SumLigneOrColumn(this._MagicSquare,-1,2));
        }

        ShowLevel(level);
    }

    protected void ShowLevel(int level){
        for (Map.Entry<String, EditText> element : this.list.entrySet()) {
            if (level==0){
                Log.d("************magic",level+"");
                return;
            }else{

                EditText champ = (EditText) element.getValue();
                int line = Character.getNumericValue(element.getKey().charAt(1));
                int column = Character.getNumericValue(element.getKey().charAt(3));
                if ( champ.getText().toString().equals("")|| champ.getCurrentTextColor() == Color.RED ) {
                    champ.setText(Integer.toString(this._MagicSquare[line][column]));
                    Log.d("************level",level+"");
                    //break;
                }
                level--;
            }
        }

    }
    @Override
    protected void onStart(){
        super.onStart();

        Log.d("onStart", "***on est dans onStart***");

    }
    @Override
    protected void onRestart(){
        super.onRestart();

        Log.d("onResume", "***on est dans onResume***");

    }
    @Override
    public void onStop(){
        super.onStop();
        this.Vresume= true;
        Log.d("onStop", "***on est dans onStop***");

    }
    @Override
    protected void onPause(){
        super.onPause();
        this.Vresume = true;
        this.baseCrono = this.chrono.getBase() - SystemClock.elapsedRealtime();
        this.chrono.stop();
        Log.d("onPause", "***on est dans onPause***");

    }

    @Override
    protected void onResume(){


        super.onResume();
        if(this.baseCrono == 0) {
            this.chrono.setBase(SystemClock.elapsedRealtime());
            this.chrono.start();
        }else{
            this.Bresume.setEnabled(true);
        }

        Log.d("onResume", "***on est dans onResume***");


    }
    @Override
    protected void onUserLeaveHint (){
        super.onUserLeaveHint();
        Log.d("onUserLeaveHint", "***on est dans onUserLeaveHint***");

    }

    @Override
    protected void onSaveInstanceState(Bundle appState){
        super.onSaveInstanceState(appState);
        appState.putSerializable("_MagicSquare",_MagicSquare);
        //appState.putSerializable("hashmap", list);
        appState.putInt("help",this.COUNT_HELP);
        this.baseCrono = this.chrono.getBase();
        appState.putLong("chrono",this.baseCrono);
        Log.d("**********************", "On save instance state ");


    }
    @Override
    public  void onRestoreInstanceState(Bundle appState){
        if(appState != null){
            Log.d("#####################", "On restor instance state ");
            super.onRestoreInstanceState(appState);
            this._MagicSquare = (int[][]) appState.getSerializable("_MagicSquare");
            //this.list = (HashMap<String, EditText>) appState.getSerializable("hashmap");
            this.COUNT_HELP = appState.getInt("help");
            if (this.COUNT_HELP == 0){
                this.help.setEnabled(false);
            }
            this.baseCrono = appState.getLong("chrono");
            this.chrono.setBase(this.baseCrono );
            rc1.setText(SumLigneOrColumn(this._MagicSquare,0,-1));
            rc2.setText(SumLigneOrColumn(this._MagicSquare,1,-1));
            rc3.setText(SumLigneOrColumn(this._MagicSquare,2,-1));
            rl1.setText(SumLigneOrColumn(this._MagicSquare,-1,0));
            rl2.setText(SumLigneOrColumn(this._MagicSquare,-1,1));
            rl3.setText(SumLigneOrColumn(this._MagicSquare,-1,2));
        }


    }
    //##################### ALL UTILS FUNCTIONS
    public int[][] generate_Square(){
        Integer[] digit = {1,2,3,4,5,6,7,8,9};
        Random rand = new Random();
        ArrayList<Integer> Alldigit = new ArrayList<>(digit.length);
        Alldigit.addAll(Arrays.asList(digit));
        int[][] Magic_square = new int[3][3];
        Log.d("generate_square","in generate square function");
        while(!Alldigit.isEmpty()){
          for(int i = 0; i < Magic_square.length ; i++)
              for(int j = 0; j< Magic_square[i].length; j++ ){
                  int index = rand.nextInt(Alldigit.size()) + 0;
                  Magic_square[i][j] = Alldigit.get(index);
                  Alldigit.remove(index);
            }
        }
        String s = "";
        for(int i = 0; i < Magic_square.length ; i++){
            for(int j = 0; j< Magic_square[i].length; j++ ){
                s+= Magic_square[i][j]+"|";

            }
            s+="\n";
        }
        Log.d("**********",s);

        return Magic_square;
    }

    public String SumLigneOrColumn(int[][] __Magic,int ligne, int column){
        int sum = 0;
        if(ligne == -1){
            for(int i = 0; i <__Magic[column].length;i++){
                sum += __Magic[i][column];
            }
        }
        if(column == -1){
            for(int i = 0; i <__Magic.length;i++){
                sum += __Magic[ligne][i];
            }
        }
        return sum+"";
    }

    public int getDigit(EditText _eT,TextView _tV){
        if (_tV == null){
            return Integer.parseInt(_eT.getText().toString());
        }else{
            return Integer.parseInt(_tV.getText().toString());
        }

    }
    public Boolean AllIsOk(int[][] __Magic){
        int c1 = getDigit(l0c0, null) + getDigit(l0c1, null) + getDigit(l0c2, null
        );
        int c2 = getDigit(l1c0, null) + getDigit(l1c1, null) + getDigit(l1c2, null);
        int c3 = getDigit(l2c0, null) + getDigit(l2c1, null) + getDigit(l2c2, null);
        int l1 = getDigit(l0c0, null) + getDigit(l1c0, null) + getDigit(l2c0, null);
        int l2 = getDigit(l0c1, null) + getDigit(l1c1, null) + getDigit(l2c1, null);
        int l3 = getDigit(l0c2, null) + getDigit(l1c2, null) + getDigit(l2c2, null);
        if (c1 != getDigit(null,rc1)){
            return false;
        }
        if (c2 != getDigit(null,rc2)){
            return false;
        }
        if (c3 != getDigit(null,rc3)){
            return false;
        }
        if (l1 != getDigit(null,rl1)){
            return false;
        }
        if (l2 != getDigit(null,rl2)){
            return false;
        }
        if (l3 != getDigit(null,rl3)){
            return false;
        }


        return true;
    }
    public boolean GridIsFull(){
        for (Map.Entry<String, EditText> element : this.list.entrySet()) {

            EditText champ = (EditText) element.getValue();
            if (champ.getText().toString().equals("")){
                return false;
            }
        }
        return true;
        }

    public boolean thereIsDuplic(String KeyOfEditText,Integer value){
        for (Map.Entry<String, EditText> element : this.list.entrySet()) {

            EditText champ = (EditText) element.getValue();
            String key = (String) element.getKey();
            Boolean ToContinue = champ.getText().toString().equals("") || key.equals(KeyOfEditText);
            if(ToContinue)  {
                Log.d("**"+element.getKey()+"","**champs is empty ");
                continue;
            }
            if (getDigit(champ,null) == value){
                Log.d(element.getKey()+"**","**there is a duplicate");
                return true;
            }
        }
        return false;
    }
//########################### onClick Function

    public void finishGame(View v){
        finish();
    }
    public void submit(View v){
        if(AllIsOk(this._MagicSquare)){
            SharedPreferences sharedPreferences = getSharedPreferences("BestScore", MODE_PRIVATE);
            long Second = (SystemClock.elapsedRealtime() - this.chrono.getBase()) / 1000;
            Log.d("Time =  ",""+Second);
            this.chrono.stop();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            for (int i=5; 0 <= i; i--){
                Log.d("Time =  "+i,""+Second);
                if(sharedPreferences.getLong(i+"",100)>Second){
                    editor.putLong(i+"",Second);
                    editor.commit();
                    return;
                }
            }
            this.submit.setEnabled(false);

        }

    }
    public void help(View v) {
        if (this.COUNT_HELP > 0){
            for (Map.Entry<String, EditText> element : this.list.entrySet()) {

                EditText champ = (EditText) element.getValue();
                int line = Character.getNumericValue(element.getKey().charAt(1));


                int column = Character.getNumericValue(element.getKey().charAt(3));

                //Log.d("************magic",champ.getText().toString()+"");

                if ( champ.getText().toString().equals("")|| champ.getCurrentTextColor() == Color.RED ) {
                    champ.setText(Integer.toString(this._MagicSquare[line][column]));
                    Log.d("************magic",this._MagicSquare[line][column]+"");
                    break;
                }
            }
            --this.COUNT_HELP;
            return;
        }else{
            this.help.setEnabled(false);
        }


    }
    public void resume(View view){
        if(this.baseCrono == 0){
            this.chrono.setBase(SystemClock.elapsedRealtime());
            this.chrono.start();
        }else{
            this.chrono.setBase(this.baseCrono);
            this.chrono.start();
        }
        this.Bresume.setEnabled(false);
    }
}
