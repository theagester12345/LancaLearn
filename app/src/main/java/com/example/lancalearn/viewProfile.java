package com.example.lancalearn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.dhaval2404.imagepicker.ImagePicker;

public class viewProfile extends AppCompatActivity {
    private LinearLayout img_layout;
    private ImageView profile_pic;
    private TextView name,course,status,email,phone,pname;
    private Uri profileImg_uri;
    private ImagePicker imagePicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        //GET views
        img_layout = findViewById(R.id.imglayout);
        name = findViewById(R.id.fname);
        course =findViewById(R.id.cname);
        status = findViewById(R.id.stats);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        pname = findViewById(R.id.pname);

        name.setText(getIntent().getStringExtra("name"));
        pname.setText(getIntent().getStringExtra("name"));
        email.setText(getIntent().getStringExtra("email"));
        course.setText(getIntent().getStringExtra("course"));
        status.setText(getIntent().getStringExtra("status"));
        phone.setText(getIntent().getStringExtra("phone"));

        img_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                imagePicker.Companion.with(viewProfile.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080,1080)
                        .start();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        profile_pic= findViewById(R.id.profile_image);

        if(resultCode == Activity.RESULT_OK){
            profileImg_uri= data.getData();
            profile_pic.setImageURI(profileImg_uri);
        }

    }
}