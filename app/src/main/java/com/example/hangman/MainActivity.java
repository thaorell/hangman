package com.example.hangman;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public ImageView hangImg;

    //arrays of image IDs for when the user guesses wrong
    private int[] imageArray = {
            R.drawable.Hangman_0,
            R.drawable.Hangman_1,
            R.drawable.Hangman_2,
            R.drawable.Hangman_3,
            R.drawable.Hangman_4,
            R.drawable.Hangman_5,
            R.drawable.Hangman_6
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hangImg = findViewById(R.id.hangImg);
        setContentView(R.layout.activity_main);
    }

    public void changeImage(int errCount) {
        Drawable d = getDrawable(imageArray[errCount]);
        hangImg.setImageDrawable(d);

        if (errCount == 7) {

        }
    }


}
