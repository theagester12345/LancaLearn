package com.example.lancalearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;

public class login extends AppCompatActivity {
    private MaterialButton signUp,login;
    private TextInputEditText email,password;
    private KProgressHUD progressHUD;
    private DatabaseReference users,student,tutor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        //Progress HUD
        progressHUD = KProgressHUD.create(login.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Logging In...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        student= FirebaseDatabase.getInstance().getReference().child("student");
        tutor =  FirebaseDatabase.getInstance().getReference().child("tutor");

        signUp = findViewById(R.id.signup_screen);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this,signUp.class);
                startActivity(i);
            }
        });


        //Get Views
        login = findViewById(R.id.login_btn);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progressHUD.show();


                // Validation
                if (!TextUtils.isEmpty(email.getText().toString()) && !TextUtils.isEmpty(password.getText().toString())){

                    //3 seconds delay

                    Handler delay = new Handler();

                    delay.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            //check Internet Availability
                            try {
                                if (isNetworkavailable()){



                                    //Check if @ sign exist to know its an email used for login
                                    if (email.getText().toString().contains("@")){
                                        //Query Db for email
                                        Query queryStudent = student.orderByChild("email").equalTo(email.getText().toString().trim());
                                        queryStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                if (snapshot.exists()){
                                                    for (DataSnapshot credentials : snapshot.getChildren()){

                                                        if (TextUtils.equals(password.getText().toString().trim(),credentials.child("password").getValue(String.class))){


                                                            //Update is_online
                                                            student.child(credentials.getKey()).child("is_online").setValue("true");
                                                            Intent x = new Intent(getApplicationContext(), home.class);
                                                            x.putExtra("id",credentials.child("id").getValue(String.class));
                                                            x.putExtra("course",credentials.child("course").getValue(String.class));
                                                            x.putExtra("name",credentials.child("name").getValue(String.class));
//                                                            Log.d("status",credentials.child("profile").getValue(String.class));
                                                            x.putExtra("profile",credentials.child("profile").getValue(String.class));
                                                            x.putExtra("phone",credentials.child("phone").getValue(String.class));
                                                            x.putExtra("email",credentials.child("email").getValue(String.class));


                                                            startActivity(x);
                                                            progressHUD.dismiss();
                                                            finish();

                                                            Log.d("login", "using email");



                                                        }else{
                                                            //Button Load

                                                            progressHUD.dismiss();
                                                            Log.d("status", "email login error");


                                                        }


                                                    }


                                                }else {

                                                    Query queryStudent = tutor.orderByChild("email").equalTo(email.getText().toString().trim());
                                                    queryStudent.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            if (snapshot.exists()){
                                                                for (DataSnapshot credentials : snapshot.getChildren()){

                                                                    if (TextUtils.equals(password.getText().toString().trim(),credentials.child("password").getValue(String.class))){


                                                                        //Update is_online
                                                                        tutor.child(credentials.getKey()).child("is_online").setValue("true");
                                                                        Intent x = new Intent(getApplicationContext(), home.class);
                                                                        x.putExtra("id",credentials.child("id").getValue(String.class));
                                                                        x.putExtra("course",credentials.child("course").getValue(String.class));
                                                                        x.putExtra("name",credentials.child("name").getValue(String.class));
                                                                        Log.d("status",credentials.child("profile").getValue(String.class));
                                                                        x.putExtra("profile",credentials.child("profile").getValue(String.class));
                                                                        x.putExtra("phone",credentials.child("phone").getValue(String.class));
                                                                        x.putExtra("email",credentials.child("email").getValue(String.class));


                                                                        startActivity(x);
                                                                        progressHUD.dismiss();
                                                                        finish();

                                                                        Log.d("login", "using email");



                                                                    }else{
                                                                        //Button Load

                                                                        progressHUD.dismiss();
                                                                        Log.d("status", "email login error");


                                                                    }


                                                                }


                                                            }else {



                                                                //Button Load init
                                                                progressHUD.dismiss();
                                                                Log.d("status", "email logi error no snapshot");

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });


                                                    //Button Load init
//                                                    progressHUD.dismiss();
//                                                    Log.d("status", "email logi error no snapshot");

                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });

                                    }else{
//                                        //Query Db for username
//                                        Query queryon_customerDB_username = customer_db.orderByChild("username").equalTo(username_email.getText().toString().trim());
//                                        queryon_customerDB_username.addListenerForSingleValueEvent(new ValueEventListener() {
//                                            @Override
//                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                                if (snapshot.exists()){
//                                                    for (DataSnapshot credentials : snapshot.getChildren()){
//
//                                                        if (TextUtils.equals(password.getText().toString().trim(),credentials.child("password").getValue(String.class))){
//
//
//
//                                                            //Update is_online
//                                                            customer_db.child(credentials.getKey()).child("is_online").setValue("true");
//                                                            Intent x = new Intent(getApplicationContext(), customer_home.class);
//
//
//
//                                                            startActivity(x);
//                                                            progressHUD.dismiss();
//                                                            finish();
//
//                                                            Log.d("login", "using username");
//
//
//
//                                                        }else{
//                                                            //Button Load init
//                                                            progressHUD.dismiss();
//                                                            Log.d("status", "username error problem");
//                                                        }
//
//
//                                                    }
//
//
//                                                }else {
//                                                    //Button Load
//                                                    progressHUD.dismiss();
//                                                    Log.d("status", "username error problem no snapshot");
//
//                                                }
//
//
//                                            }
//
//                                            @Override
//                                            public void onCancelled(@NonNull DatabaseError error) {
//
//                                            }
//                                        });

                                    }


                                }else {
                                    //Button Load init
                                    progressHUD.dismiss();

                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            //checkLoging(username_email.getText().toString(),password.getText().toString());



                        }
                    }, 3000);

                }else {

                    progressHUD.dismiss();
                    Log.d("status", "field validation error");
                }
            }
        });


    }

    public boolean isNetworkavailable() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    @Override
    protected void onDestroy() {
        progressHUD.dismiss();
        super.onDestroy();

    }
}