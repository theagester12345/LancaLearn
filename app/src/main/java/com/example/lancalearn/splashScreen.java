package com.example.lancalearn;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.material.textview.MaterialTextView;

public class splashScreen extends AppCompatActivity {
    //Variables
    private Animation topAmin, bottomAmin;
    private ImageView image;
    private MaterialTextView logo, groupname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalsh_screen);

        //Animation
        topAmin= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAmin= AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //hooks
        image= findViewById(R.id.imageView);
        logo= findViewById(R.id.textView);
        groupname= findViewById(R.id.textView2);

        image.setAnimation(topAmin);
//        logo.setAnimation(bottomAmin);
        groupname.setAnimation(bottomAmin);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(splashScreen.this, login.class);
                Pair[ ]pairs=new Pair[2];
                pairs[0]=new Pair<View, String>(image,"logo_image");
                pairs[1]=new Pair<View, String>(logo,"logo_text");

//
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(splashScreen.this,pairs);
                    startActivity(intent,options.toBundle());
                    finish();
                }

            }
        }, 5000);
    }
}