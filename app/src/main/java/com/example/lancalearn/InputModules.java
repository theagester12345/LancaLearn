package com.example.lancalearn;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApiNotAvailableException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InputModules extends AppCompatActivity {
    private TextInputEditText name,code,course;
    private MaterialButton add;
    private DatabaseReference modules,post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_modules);

        //Get Views
        name = findViewById(R.id.name);
        code =findViewById(R.id.code);
        course = findViewById(R.id.course);
        add = findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                modules = FirebaseDatabase.getInstance().getReference().child("module");
                post = modules.push();
                post.child("name").setValue(name.getText().toString());
                post.child("code").setValue(code.getText().toString());
                post.child("course").setValue(course.getText().toString());




            }
        });



    }
}