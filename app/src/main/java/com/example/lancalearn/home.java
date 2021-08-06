package com.example.lancalearn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kaopiz.kprogresshud.KProgressHUD;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class home extends AppCompatActivity {
    private MaterialTextView name,course,portal;
    private RecyclerView rv;
    private DatabaseReference user,modules,decision,post,student,tutor;
    private FirebaseRecyclerOptions<module> option;
    private FirebaseRecyclerAdapter<module,module_viewHolder> adapter;
    private KProgressHUD progressHUD;
    private String course_short_hand;
    private MaterialButton logout,view_profile;
    private CircleImageView profile_pic;
    private ImagePicker imagePicker;
    private Uri profileImg_uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().setTitle("Home");

        //Progress HUD
        progressHUD = KProgressHUD.create(home.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Looking for Subjects...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        //GET Views
        name =findViewById(R.id.name);
        rv = findViewById(R.id.recyclerView);
        portal = findViewById(R.id.portal);
        course = findViewById(R.id.course);
        logout = findViewById(R.id.logout);
        view_profile =findViewById(R.id.view_profile);
        profile_pic = findViewById(R.id.profile_image);

        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                imagePicker.Companion.with(home.this)
                        .crop()
                        .compress(1024)
                        .maxResultSize(1080,1080)
                        .start();
            }
        });

        view_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(home.this,viewProfile.class);
                i.putExtra("name",getIntent().getStringExtra("name"));
                i.putExtra("email",getIntent().getStringExtra("email"));
                i.putExtra("course",getIntent().getStringExtra("course"));
                i.putExtra("status",getIntent().getStringExtra("profile"));
                i.putExtra("phone",getIntent().getStringExtra("phone"));
                startActivity(i);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(home.this,login.class);
                startActivity(i);
                finish();
            }
        });

        name.setText(getIntent().getStringExtra("name"));
        course.setText(getIntent().getStringExtra("course"));
        portal.setText(getIntent().getStringExtra("profile")+" Portal");

        //Setup RecyclerView
        rv.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(home.this);
        linearLayoutManager.setReverseLayout(true);
        rv.setLayoutManager(linearLayoutManager);


        //INIT Dbs
        user= FirebaseDatabase.getInstance().getReference().child("users");
        student= FirebaseDatabase.getInstance().getReference().child("student");
        tutor = FirebaseDatabase.getInstance().getReference().child("tutor");
        modules = FirebaseDatabase.getInstance().getReference().child("module");
        decision = FirebaseDatabase.getInstance().getReference().child("decision");

        //Convert Course Full hand to short hand
        if (getIntent().getStringExtra("course").equals("Accounting")){
            course_short_hand="ac";
        }else if (getIntent().getStringExtra("course").equals("Computer Science")){
            course_short_hand="cs";
        }else if (getIntent().getStringExtra("course").equals("Business Management")){
            course_short_hand="bm";
        }else if (getIntent().getStringExtra("course").equals("Marketing")){
            course_short_hand="mk";
        }else if (getIntent().getStringExtra("course").equals("Politics")){
            course_short_hand="ps";
        }else if (getIntent().getStringExtra("course").equals("Economics")){
            course_short_hand="es";
        }else if (getIntent().getStringExtra("course").equals("Law")){
            course_short_hand="lw";
        }



        try {
            if (isNetworkavailable()){

//                //GET user name & course
//                Query query = user.orderByChild("id").equalTo(getIntent().getStringExtra("id"));
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        if (snapshot.exists()){
//
//                            for (DataSnapshot users : snapshot.getChildren()){
//
//                                name.setText(users.child("name").getValue(String.class));
//                                course.setText(users.child("course").getValue(String.class));
//
//                                progressHUD.dismiss();
//
//
//                            }
//
//                        }else {
//                            progressHUD.dismiss();
//                            Toast.makeText(home.this,"Problem Connecting to Database",Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//                        Toast.makeText(home.this,error.toString(),Toast.LENGTH_LONG).show();
//                    }
//                });


                //GET ALL Modules
                Query allModules = modules.orderByChild("course").equalTo(course_short_hand);
                allModules.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){


                            option = new FirebaseRecyclerOptions.Builder<module>().setQuery(allModules,module.class ).build();
                            adapter = new FirebaseRecyclerAdapter<module, module_viewHolder>(option) {
                                @Override
                                protected void onBindViewHolder(@NonNull module_viewHolder holder, int position, @NonNull module model) {

                                    //Image
                                    if (model.getBorrowed()==null){
                                        //Not a borrowed course
                                        if (model.getCourse().equals("mk")){
                                            holder.img.setImageResource(R.drawable.marketing);

                                        }else if (model.getCourse().equals("es")){
                                            holder.img.setImageResource(R.drawable.economics);
                                        }else if (model.getCourse().equals("ac")){
                                            holder.img.setImageResource(R.drawable.accounting);
                                        }else if (model.getCourse().equals("ps")){
                                            holder.img.setImageResource(R.drawable.political_science);
                                        }else if (model.getCourse().equals("cs")){
                                            holder.img.setImageResource(R.drawable.computer_science);

                                        }else if (model.getCourse().equals("bm")){
                                            holder.img.setImageResource(R.drawable.businness_management);
                                        }else if (model.getCourse().equals("lw")){
                                            holder.img.setImageResource(R.drawable.law);
                                        }
                                    }else {
                                        if (model.getBorrowed().equals("mk")){
                                            holder.img.setImageResource(R.drawable.marketing);

                                        }else if (model.getBorrowed().equals("es")){
                                            holder.img.setImageResource(R.drawable.economics);
                                        }else if (model.getBorrowed().equals("ac")){
                                            holder.img.setImageResource(R.drawable.accounting);
                                        }else if (model.getBorrowed().equals("ps")){
                                            holder.img.setImageResource(R.drawable.political_science);
                                        }else if (model.getBorrowed().equals("cs")){
                                            holder.img.setImageResource(R.drawable.computer_science);

                                        }else if (model.getBorrowed().equals("bm")){
                                            holder.img.setImageResource(R.drawable.businness_management);
                                        }else if (model.getBorrowed().equals("lw")){
                                            holder.img.setImageResource(R.drawable.law);
                                        }
                                    }


                                    //Get name
                                    holder.name.setText(model.getName());
                                    progressHUD.dismiss();

                                    //Get code
                                    holder.code.setText(model.getCode());

                                    //Onclick
                                    holder.cv.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {

//                                            //Create Decision
//                                            post = decision.push();
//                                            post.child("id").setValue(getIntent().getStringExtra("id"));
//                                            post.child("code").setValue(model.getCode());

                                            Intent i = new Intent(home.this,tutor_student.class);
                                            if (getIntent().getStringExtra("profile").equals("Student")){
                                                i.putExtra("heading","List Of Tutors");
                                            }else {
                                                i.putExtra("heading","List Of Students");
                                            }
                                            i.putExtra("course",getIntent().getStringExtra("course"));
                                            i.putExtra("profile",getIntent().getStringExtra("profile"));

                                            startActivity(i);


                                        }
                                    });

                                }

                                @NonNull
                                @Override
                                public module_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                                    View view =   LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_module_card,parent,false);


                                    return new module_viewHolder(view);
                                }
                            };

                            adapter.startListening();
                            rv.setAdapter(adapter);

                        }else {
                            progressHUD.dismiss();
                            Toast.makeText(home.this,"Problem Connecting to Database",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressHUD.dismiss();
                        Toast.makeText(home.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                });


            }else {
                progressHUD.dismiss();
                Toast.makeText(home.this,"Please Check your Internet Connection",Toast.LENGTH_LONG).show();

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