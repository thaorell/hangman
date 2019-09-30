package com.example.hangman;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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

    LinearLayout parent;
    TextView dashTxtView;
    TextView wordTxtView;

    Button btn;
    private int errCount = 0;
    private ArrayList<List<String>> wordToHint = new ArrayList<>();
    private String currAnswer = "";
    private String currHint = "";
    private String currWord = "    ";
    private String currLet = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hangImg = (ImageView) findViewById(R.id.hangImg);

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


        // Initialize the alphabet buttons by inserting new LinearLayout into the parent container
       String[] btn_name={"a","b","c","d","e","f","g","h","i","j","k",
                "l","m","n","o","p","q","r","s","t","u","v",
                "w","x","y","z"};

        parent = (LinearLayout)findViewById(R.id.alphabet);
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
                parent.addView(tempLayout);
            }
        }

        //Randomly choose a word, get the word and its hint
        Random rand = new Random();
        int currIdx = rand.nextInt(10);
        currAnswer = wordToHint.get(currIdx).get(0);
        currHint = wordToHint.get(currIdx).get(1);

        //initialize dashes under
        dashTxtView = findViewById(R.id.dashTextView);
        wordTxtView = findViewById(R.id.wordTxtView);
        String dashes = "";
        for (int i = 0; i< currAnswer.length(); i++){
            dashes += "-";
        }
        dashTxtView.setText(dashes);
        dashTxtView.setTextSize(80f);
    }


    @Override
    public void onClick(View v) {
        Button currBtn = (Button) findViewById(v.getId());
        currLet = currBtn.getText().toString();
        Log.v("Current word is ", currAnswer);

        Log.v("current Letter is ", currLet);

        //if the letter clicked is not in the word, increment error Count
        if (!currAnswer.contains(currLet)) {
            errCount++;
            changeImage(errCount);
        }
        //if the letter clicked and the letter is in the word
        // we need to perform 3 things:
        // 1. update currWord and replace all the blank space with the current character
        // 2.
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
//            if (currWord.compareTo(currAnswer) == 0)

        }
    }

    //update hangman picture
    public void changeImage(int errCount) {
        Drawable d = getDrawable(imageArray[errCount]);
        hangImg.setImageDrawable(d);
        if (errCount == 7) {

        }
    }

    public void



}

