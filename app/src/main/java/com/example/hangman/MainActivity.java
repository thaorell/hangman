package com.example.hangman;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Layout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    LinearLayout parent;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] btn_name={"a","b","c","d","e","f","g","h","i","j","k",
                "l","m","n","o","p","q","r","s","t","u","v",
                "w","x","y","z"};


//        String[]btn_name={"a","b","c"};
        parent = (LinearLayout)findViewById(R.id.alphabet);
        LinearLayout tempLayout = new LinearLayout(this);;
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
                    ViewGroup.LayoutParams.MATCH_PARENT, 1f));

            tempLayout.addView(btn);
            btn.setOnClickListener(MainActivity.this);
            if (i % 5 == 4 || i == 25) {
                parent.addView(tempLayout);
            }

        }
    }

    @Override
    public void onClick(View v) {
        String str = v.getTag().toString();
        if(str.equals("0")){
            Toast.makeText(getApplicationContext(),"You pick a",Toast.LENGTH_LONG).show();
        }
        else if(str.equals("1")){
            Toast.makeText(getApplicationContext(),"You pick b",Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"You pick c",Toast.LENGTH_LONG).show();
        }

    }
}

