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

public class editdetails extends AppCompatActivity {

    DatabaseReference dbref,db;
    EditText editTextname, editTextdob, editTextage, editTexthome, editTextstudy, editTextprof, editTextint, editTextfav, editTextph, editTextgen;
    userdetails ud;
    Button buttonsave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdetails);

        editTextname = findViewById(R.id.editTextname);
        editTextdob = findViewById(R.id.editTextdob);
        editTextage = findViewById(R.id.editTextage);
        editTextprof = findViewById(R.id.editTextprof);
        editTextfav= findViewById(R.id.editTextfav);
        editTexthome = findViewById(R.id.editTexthome);
        editTextint= findViewById(R.id.editTextint);
        editTextph = findViewById(R.id.editTextphone);
        editTextstudy = findViewById(R.id.editTextstudy);
        editTextgen = findViewById(R.id.editTextgen);

        buttonsave = findViewById(R.id.buttonsave);
        ud = new userdetails();

        dbref = FirebaseDatabase.getInstance().getReference().child("userdetails").child("userdetails1");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    editTextname.setText(dataSnapshot.child("name").getValue().toString());
                    editTextage.setText(dataSnapshot.child("age").getValue().toString());
                    editTextdob.setText(dataSnapshot.child("dob").getValue().toString());
                    editTextstudy.setText(dataSnapshot.child("studiedat").getValue().toString());
                    editTexthome.setText(dataSnapshot.child("hometown").getValue().toString());
                    editTextprof.setText(dataSnapshot.child("profession").getValue().toString());
                    editTextint.setText(dataSnapshot.child("interestedTo").getValue().toString());
                    editTextfav.setText(dataSnapshot.child("favourites").getValue().toString());
                    editTextgen.setText(dataSnapshot.child("gender").getValue().toString());
                    editTextph.setText(dataSnapshot.child("phone").getValue().toString());
                }
                else
                    Toast.makeText(getApplicationContext(),"No details to show",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance().getReference().child("userdetails");
                db.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("userdetails1")){
                            try{
                                ud.setName(editTextname.getText().toString().trim());
                                ud.setAge(Integer.parseInt(editTextage.getText().toString().trim()));
                                ud.setDob(editTextdob.getText().toString().trim());
                                ud.setGender(editTextgen.getText().toString().trim());
                                ud.setHometown(editTexthome.getText().toString().trim());
                                ud.setFavourites(editTextfav.getText().toString().trim());
                                ud.setPhone(Integer.parseInt(editTextph.getText().toString().trim()));
                                ud.setProfession(editTextprof.getText().toString().trim());
                                ud.setStudiedat(editTextstudy.getText().toString().trim());
                                ud.setInterestedTo(editTextint.getText().toString().trim());

                                dbref = FirebaseDatabase.getInstance().getReference().child("userdetails").child("userdetails1");
                                dbref.setValue(ud);

                                Toast.makeText(getApplicationContext(),"Data updated successfully",Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(editdetails.this, myprofile.class);
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

    public void GoToProfile(View view){
        Intent intent = new Intent(editdetails.this,myprofile.class);
        startActivity(intent);
    }
}
