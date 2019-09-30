package com.example.hangman;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    public ImageView hangImg;

    //arrays of image IDs for when the user guesses wrong
    private int[] imageArray = {
            R.drawable.hangman0,
            R.drawable.hangman1,
            R.drawable.hangman2,
            R.drawable.hangman3,
            R.drawable.hangman4,
            R.drawable.hangman5,
            R.drawable.hangman6
    };

    LinearLayout alphabetLayout;
    TextView wordTxtView;

    Button btn;
    Button newGameBtn;
    private int errCount = 0;
    private ArrayList<List<String>> wordToHint = new ArrayList<>();
    private String currAnswer = "";
    private String currHint = "";
    private String currWord = "____";
    private String currLet = "";
    private int hintClkCnt = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hangImg = (ImageView) findViewById(R.id.hangImg);

        //set up new game btn
        newGameBtn = (Button) findViewById(R.id.newGameBtn);
        newGameBtn.setOnClickListener(newGameListener);

        // Initialize list of words and their hints
        // For simplicity we only do 4 letters at the moment
        wordToHint.add(Arrays.asList(new String[]{"joke", "funny"}));
        wordToHint.add(Arrays.asList(new String[]{"blue", "color"}));
        wordToHint.add(Arrays.asList(new String[]{"tree", "green"}));
        wordToHint.add(Arrays.asList(new String[]{"bank", "money"}));
        wordToHint.add(Arrays.asList(new String[]{"five", "number"}));
        wordToHint.add(Arrays.asList(new String[]{"hair", "fur"}));
        wordToHint.add(Arrays.asList(new String[]{"baby", "child"}));
        wordToHint.add(Arrays.asList(new String[]{"beer", "drink"}));
        wordToHint.add(Arrays.asList(new String[]{"cook", "make food"}));
        wordToHint.add(Arrays.asList(new String[]{"bear", "animal"}));

        //generate all buttons for alphabet
        String[] btn_name={"a","b","c","d","e","f","g","h","i","j","k",
                "l","m","n","o","p","q","r","s","t","u","v",
                "w","x","y","z"};

        alphabetLayout = (LinearLayout)findViewById(R.id.alphabet);
        LinearLayout tempLayout = new LinearLayout(this);

        // Each row has 5 buttons
        for(int i=0; i<26; i++)
        {
            if (i % 5 == 0){
                tempLayout = new LinearLayout(this);
                tempLayout.setWeightSum(5);
                tempLayout.setOrientation(LinearLayout.HORIZONTAL);
            }
            btn = new Button(MainActivity.this);
            btn.setId(i+1);
            btn.setText(btn_name[i]);
            btn.setTag(i);
            btn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    (i == 25) ? 0.2f : 1f));

            tempLayout.addView(btn);
            btn.setOnClickListener(MainActivity.this);
            if (i % 5 == 4 || i == 25) {
                alphabetLayout.addView(tempLayout);
            }
        }
        //Randomly choose a word, get the word and its hint
        Random rand = new Random();
        int currIdx = rand.nextInt(10);
        currAnswer = wordToHint.get(currIdx).get(0);
        currHint = wordToHint.get(currIdx).get(1);

        //initialize dashes under
        wordTxtView = findViewById(R.id.wordTxtView);
        wordTxtView.setText(currWord);
        //draw the underscores
        String dashes = "";
        for (int i = 0; i< currAnswer.length(); i++){
            dashes += "-";
        }

        //handling hint button in landscape orientation
        int orientation = this.getResources().getConfiguration().orientation;
        // if it is, display the hint
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayout wordAndHintLayout = findViewById(R.id.wordAndHint);
            Button hintBtn = new Button(MainActivity.this);
            hintBtn.setText("HINT");
            hintBtn.setOnClickListener(hintListener);
            hintBtn.setId((int)100);
            wordAndHintLayout.addView(hintBtn);
        }
    }

    @Override
    protected void onRestoreInstanceState (Bundle savedInstanceState) {

        super.onRestoreInstanceState (savedInstanceState);
        currAnswer= savedInstanceState.getString("currAnswer");
        currWord= savedInstanceState.getString("currWord");
        currHint= savedInstanceState.getString("currHint");
        currLet= savedInstanceState.getString("currLet");
        errCount= savedInstanceState.getInt("errCount");
        wordTxtView.setText(currWord);
        hintClkCnt= savedInstanceState.getInt("hintClkCnt");
        //restore button states
        boolean[] alphabetState=savedInstanceState.getBooleanArray("alphabetState");
        restoreAlphabet(alphabetState);
        changeImage(errCount);

    }

    @Override
    protected void onSaveInstanceState (Bundle savedInstanceState) {
        savedInstanceState.putString("currAnswer",currAnswer);
        savedInstanceState.putString("currWord", currWord);
        savedInstanceState.putString("currHint", currHint);
        savedInstanceState.putString("currLet", currLet);
        savedInstanceState.putInt("hintClkCnt", hintClkCnt);
        savedInstanceState.putInt("errCount", errCount);
        boolean[] alphabetState= new boolean[26];
        for (int i = 0; i< 26; i++){
            Button tempBtn = (Button) findViewById(i+1);
            alphabetState[i] = tempBtn.isEnabled();
        }
        savedInstanceState.putBooleanArray("alphabetState", alphabetState);
        super.onSaveInstanceState (savedInstanceState);
    }

    //onClick for hint button
    OnClickListener hintListener = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            // on the last life, cannot play any further
            if (errCount == 6){
                Toast.makeText(MainActivity.this, "Out of moves. Can't do this",
                        Toast.LENGTH_LONG).show();
            }
            else {
                //fetch button and perform actions
                Button hintBtn = (Button) findViewById((int)100);
                hintClkCnt++;
                setHintButton(hintBtn);
                Log.v("Current err is ", Integer.toString(errCount));
            }


        }
    };

    OnClickListener newGameListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(MainActivity.this, MainActivity.class); //change it to your main class
            //the following 2 tags are for clearing the backStack and start fresh
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            finish();
            startActivity(i);
        }
    };

    public void setHintButton(Button hintBtn) {
        if (hintClkCnt == 1) {
            LinearLayout wordAndHintLayout = findViewById(R.id.wordAndHint);
            TextView hintView = new TextView(MainActivity.this);
            hintView.setText("Hint: " + currHint);
            hintView.setTextSize(20f);
            wordAndHintLayout.addView(hintView);
            errCount++;
            changeImage(errCount);
        }
        // second click, disable unnecessary characters
        if (hintClkCnt == 2) {
            errCount++;
            changeImage(errCount);
            hintBtn.setEnabled(false);
            disableHalfAlphabet();
        }
    }

    public void disableHalfAlphabet() {
        // if the game is over
            int half = 13;
            Random random = new Random();
            while (half > 0) {
                int id = random.nextInt(26) + 1;
                Button tempBtn = (Button) findViewById(id);
                Log.v("this is half id ", Integer.toString(id));

                if (tempBtn.isEnabled() && !currAnswer.contains(tempBtn.getText().toString())) {
                    tempBtn.setEnabled(false);
                    half--;
                }
                else continue;
            }
    }
    public void restoreAlphabet(boolean[] alphabetState) {
        for (int i = 1; i< 27; i++){
            Button tempBtn = (Button) findViewById(i);
            tempBtn.setEnabled(alphabetState[i-1]);
        }
    }

    public void disableAllAlphabet() {
        for (int i = 1; i< 27; i++){
            Button tempBtn = (Button) findViewById(i);
            if (tempBtn.isEnabled()){
                tempBtn.setEnabled(false);
            }
        }
    }
    @Override
    public void onClick(View v) {
        if (errCount >= 6){
            Toast.makeText(this, "You have lost :(",
                    Toast.LENGTH_LONG).show();
            disableAllAlphabet();
        }
        else{
            Button currBtn = (Button) findViewById(v.getId());
            currLet = currBtn.getText().toString();
            Log.v("Current word is ", currAnswer);

            Log.v("current Letter is ", currLet);

            //if the letter clicked is not in the word, increment error Count
            if (!currAnswer.contains(currLet)) {
                errCount++;
                changeImage(errCount);
                Log.v("Current err is ", Integer.toString(errCount));
            }
            //if the letter clicked and the letter is in the word
            // we need to perform 3 things:
            // 1. update currWord and replace all the blank space with the current character
            // 2. if won, display toast
            else {
                // do a linear search
                char[] charArr = currWord.toCharArray();
                for (int i = 0; i < charArr.length; i++) {
                    // if correct letter, append current letter to CurrWord
                    if (currAnswer.charAt(i) == currLet.charAt(0)) {
                        charArr[i] = currLet.charAt(0);
                    }
                    currWord = new String(charArr);
                }
                wordTxtView.setText(currWord);
                currBtn.setEnabled(false);
                if (currWord.compareTo(currAnswer) == 0) {
                    Toast.makeText(this, "You have won",
                            Toast.LENGTH_LONG).show();
                    disableAllAlphabet();
                }
            }
        }

    }

    //update hangman picture
    public void changeImage(int errCount) {
        if (errCount > 7){
            Toast.makeText(this, "Game over! No action",
                    Toast.LENGTH_LONG).show();
        }
        else{
            Drawable d = getDrawable(imageArray[errCount-1]);
            hangImg.setImageDrawable(d);
            if (errCount == 7){
                Toast.makeText(this, "You have lost :(",
                        Toast.LENGTH_LONG).show();
                disableAllAlphabet();
            }
        }
    }
}

