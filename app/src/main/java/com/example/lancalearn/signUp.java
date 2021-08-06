package com.example.lancalearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.angmarch.views.NiceSpinner;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class signUp extends AppCompatActivity {
    private MaterialButton backToLogin,signUp;
    private NiceSpinner year,course,profile;
    private TextInputEditText name,email,password,phone;
    private KProgressHUD progressHUD;
    private AwesomeValidation validation;
    private DatabaseReference post,student,tutor,user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Progress HUD
        progressHUD = KProgressHUD.create(signUp.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Setting you up...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f);

        //Init users db
        student = FirebaseDatabase.getInstance().getReference().child("student");
        tutor = FirebaseDatabase.getInstance().getReference().child("tutor");


        //GET Views
        year = findViewById(R.id.year);
        course = findViewById(R.id.course);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        profile =  findViewById(R.id.profile);
        phone = findViewById(R.id.phone);

        //GET string arrays for spinner
        List<String> dataset_year = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.year)));
        List<String> dataset_course = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.course)));
        List<String> dataset_profile = new LinkedList<>(Arrays.asList(getResources().getStringArray(R.array.profile)));
        year.attachDataSource(dataset_year);
        course.attachDataSource(dataset_course);
        profile.attachDataSource(dataset_profile);


        //Init Validation Style
        validation = new AwesomeValidation(ValidationStyle.BASIC);
        validation.addValidation(this, R.id.email,android.util.Patterns.EMAIL_ADDRESS,R.string.email_error );
        String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        validation.addValidation(this,R.id.password,regexPassword,R.string.password__error );



        signUp = findViewById(R.id.signup);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressHUD.show();


                //Check internet connection
                try {
                    if (isNetworkavailable()){

                        //Check Form Validation
                        validation.clear();

                        if (validation.validate()){
                            if (!TextUtils.equals(course.getSelectedItem().toString(), getString(R.string.course_heading)) && !TextUtils.equals(profile.getSelectedItem().toString(), getString(R.string.profile_heading))) {

                                //3 seconds delay
                                Handler delay = new Handler();

                                delay.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        postToDB();
                                        // Log.d("customerID", cust_id);

                                        finish();



                                    }
                                }, 3000);


                            }else {
                                //Button Load Spinner
                                progressHUD.dismiss();
                                Toast.makeText(signUp.this,"Fill All Fields",Toast.LENGTH_SHORT).show();
                            }

                        }else {

                            progressHUD.dismiss();

                        }




                    }else{
                        //Button Load spinner
                        progressHUD.dismiss();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        backToLogin = findViewById(R.id.reg_login_btn);
        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    public boolean isNetworkavailable() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    //Generate customer ID
    public void postToDB(){
        //Write to customer db

        if (profile.getSelectedItem().toString().equals("Student")){
            post = student.push();
           user = student;
        }else {
            post = tutor.push();
            user = tutor;
        }


        // It will generate 6 digit random Number.
        // from 0 to 999999
        Random rnd = new Random();
        int number = rnd.nextInt(999999);

        //Look into Db for same ID

        Query query_onCustomerDB = user.orderByChild("id").equalTo(String.format("%06d", number));
        query_onCustomerDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("snapshot",snapshot.toString());
                if (snapshot.exists()){
                    postToDB();
                    Log.d("generateCustomerID","id exist in db");
                }else{

                    // this will convert any number sequence into 6 character.


                    post.child("id").setValue( String.format("%06d", number));
                    post.child("name").setValue(name.getText().toString());
                    post.child("email").setValue(email.getText().toString().trim());
                    post.child("password").setValue(password.getText().toString().trim());
                    post.child("course").setValue(course.getSelectedItem().toString());
                    post.child("year").setValue(year.getSelectedItem().toString());
                    post.child("profile").setValue(profile.getSelectedItem().toString());
                    post.child("phone").setValue(phone.getText().toString());




                    //Log.d("generateCustomerID","id does not exist in db");
                    //Log.d("c_id",String.format("%06d", number));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}