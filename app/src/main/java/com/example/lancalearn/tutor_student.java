package com.example.lancalearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;

public class tutor_student extends AppCompatActivity {
    private MaterialTextView heading ;
    private RecyclerView rv;
    private DatabaseReference user,student,tutor;
    private FirebaseRecyclerOptions<user> option;
    private FirebaseRecyclerAdapter<user,user_viewHolder> adapter;
    private KProgressHUD progressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_student);
        getSupportActionBar().setTitle("");

        //Progress HUD
        progressHUD = KProgressHUD.create(tutor_student.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Looking for Participants...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        //GET views
        heading = findViewById(R.id.heading);
        rv = findViewById(R.id.recyclerView);

        //Setup RecyclerView
        rv.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(tutor_student.this);
//        linearLayoutManager.setReverseLayout(true);
        rv.setLayoutManager(linearLayoutManager);

        heading.setText(getIntent().getStringExtra("heading"));

        //INT db
        student= FirebaseDatabase.getInstance().getReference().child("student");
        tutor = FirebaseDatabase.getInstance().getReference().child("tutor");

        try {
            if (isNetworkavailable()){

                if (getIntent().getStringExtra("profile").equals("Tutor")){
                    user = student;
                }else {
                    user=tutor;
                }

                Query query = user.orderByChild("course").equalTo(getIntent().getStringExtra("course"));
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Log.d("QueryCourse:",snapshot.toString());
                        Log.d("course",getIntent().getStringExtra("course"));
                        if (snapshot.exists()){

                            option = new FirebaseRecyclerOptions.Builder<user>().setQuery(query,user.class ).build();
                            adapter = new FirebaseRecyclerAdapter<user, user_viewHolder>(option) {
                                @Override
                                protected void onBindViewHolder(@NonNull user_viewHolder holder, int position, @NonNull user model) {

                                    rv.setVisibility(View.VISIBLE);
                                    holder.name.setText(model.getName());
                                    progressHUD.dismiss();
                                    holder.email.setText(model.getEmail());

                                    if (model.getProfile().equals("Student")){
                                        holder.icon.setImageResource(R.drawable.student);
                                    }else {
                                        holder.icon.setImageResource(R.drawable.teacher);
                                    }

                                    holder.call.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            callPhoneNumber(model.getPhone());
                                        }
                                    });

                                    holder.message.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent i = new Intent(tutor_student.this,chat_app.class);
                                            startActivity(i);
                                        }
                                    });



//                                    String profile = getIntent().getStringExtra("profile");
//                                    Log.d("profileQ:",profile);
//                                    Log.d("profileM:",model.getProfile());
//                                    if (profile.equals(profile)){
//                                        progressHUD.dismiss();
////                                        Toast.makeText(tutor_student.this,"No Participant available",Toast.LENGTH_LONG).show();
//
//                                    }else {
//
//                                    }

                                }

                                @NonNull
                                @Override
                                public user_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view =   LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_tutor_student_card,parent,false);


                                    return new user_viewHolder(view);
                                }
                            };

                            adapter.startListening();
                            rv.setAdapter(adapter);


//                            Query profileQuery = snapshot.getRef().orderByChild("profile").equalTo(getIntent().getStringExtra("profile"));
//                            profileQuery.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                    Log.d("QueryStatus:",snapshot.toString());
//                                    Log.d("profile:",getIntent().getStringExtra("profile"));
//                                   if (snapshot.exists()){
//
//
//                                   }else {
//                                       Toast.makeText(tutor_student.this,"No Participant Available",Toast.LENGTH_LONG).show();
//                                   }
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });



                        }else {

                            progressHUD.dismiss();
                            Toast.makeText(tutor_student.this,"No Participant available",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else {
                progressHUD.dismiss();
                Toast.makeText(tutor_student.this,"Please Check your Internet Connection...",Toast.LENGTH_LONG).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isNetworkavailable() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

    public void callPhoneNumber(String number)
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling

                    ActivityCompat.requestPermissions(tutor_student.this, new String[]{Manifest.permission.CALL_PHONE}, 101);

                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + number));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if(requestCode == 101)
//        {
//            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
//            {
//                callPhoneNumber();
//            }
//            else
//            {
//                Log.e(TAG, "Permission not Granted");
//            }
//        }
    }
}