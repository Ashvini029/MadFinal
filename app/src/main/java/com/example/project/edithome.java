package com.example.project;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class edithome extends AppCompatActivity {

    DatabaseReference dbref,db;
    EditText editTextcap;
    userhome uh;
    Button buttonsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edithome);

        editTextcap = findViewById(R.id.editTextcaption);
        buttonsave = findViewById(R.id.buttonsave);
        uh = new userhome();

        dbref = FirebaseDatabase.getInstance().getReference().child("userhome").child("userhome1");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    editTextcap.setText(dataSnapshot.child("caption").getValue().toString());
                }
                else
                    Toast.makeText(getApplicationContext(),"No profile or caption to show",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance().getReference().child("userhome");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("userhome1")){
                            try{
                                uh.setCaption(editTextcap.getText().toString().trim());

                                dbref = FirebaseDatabase.getInstance().getReference().child("userhome").child("userhome1");
                                dbref.setValue(uh);

                                Toast.makeText(getApplicationContext(),"Data updated successfully",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(edithome.this, myprofile.class);
                                startActivity(intent);
                            }
                            catch(NumberFormatException e){
                                Toast.makeText(getApplicationContext(),"Invalid updation",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                            Toast.makeText(getApplicationContext(),"No data to be updated",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


    }


    public void GoToProfile(View view) {
        Intent intent = new Intent(edithome.this, myprofile.class);
        startActivity(intent);
    }
}

