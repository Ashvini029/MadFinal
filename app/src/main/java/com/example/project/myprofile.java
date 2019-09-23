package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class myprofile extends AppCompatActivity {

    DatabaseReference dbref,db,db1,db2;
    TextView textViewcap,textViewname,textViewgender,textViewdob,textViewage,textViewhome,textViewstudy,textViewprof,textViewint,textViewfav;
    Button buttondel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myprofile);

        textViewcap = findViewById(R.id.textViewcap);
        textViewname = findViewById(R.id.textViewname);
        textViewage = findViewById(R.id.textViewage);
        textViewhome = findViewById(R.id.textViewhome);
        textViewstudy = findViewById(R.id.textViewstudy);
        textViewprof = findViewById(R.id.textViewprof);
        textViewint = findViewById(R.id.textViewint);
        textViewfav = findViewById(R.id.textViewfav);
        textViewgender =findViewById(R.id.textViewgen);
        textViewdob = findViewById(R.id.textViewdob);
        buttondel = findViewById(R.id.buttondelete);

        buttondel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                db1 = FirebaseDatabase.getInstance().getReference().child("userhome");
                db1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("userhome1")){
                            dbref = FirebaseDatabase.getInstance().getReference().child("userhome").child("userhome1");
                            dbref.removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


                db2 = FirebaseDatabase.getInstance().getReference().child("userdetails");
                db2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("userdetails1")){
                            db = FirebaseDatabase.getInstance().getReference().child("userdetails").child("userdetails1");
                            db.removeValue();
                            Toast.makeText(getApplicationContext(),"Your account has been removed.",Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Account details incorrect to be deleted.",Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        dbref = FirebaseDatabase.getInstance().getReference().child("userhome").child("userhome1");
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    textViewcap.setText(dataSnapshot.child("caption").getValue().toString());
                }
                else
                    Toast.makeText(getApplicationContext(),"No profile or caption to show",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        db = FirebaseDatabase.getInstance().getReference().child("userdetails").child("userdetails1");
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()){
                    textViewname.setText(dataSnapshot.child("name").getValue().toString());
                    textViewage.setText(dataSnapshot.child("age").getValue().toString());
                    textViewdob.setText(dataSnapshot.child("dob").getValue().toString());
                    textViewgender.setText(dataSnapshot.child("gender").getValue().toString());
                    textViewstudy.setText(dataSnapshot.child("studiedat").getValue().toString());
                    textViewhome.setText(dataSnapshot.child("hometown").getValue().toString());
                    textViewprof.setText(dataSnapshot.child("profession").getValue().toString());
                    textViewint.setText(dataSnapshot.child("interestedTo").getValue().toString());
                    textViewfav.setText(dataSnapshot.child("favourites").getValue().toString());
                }
                else
                    Toast.makeText(getApplicationContext(),"No any details show",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void EditHome(View view) {
        Intent intent = new Intent(myprofile.this, edithome.class);
        startActivity(intent);

    }
    public void EditDetails(View view){
        Intent intent = new Intent(myprofile.this,editdetails.class);
        startActivity(intent);

    }
}
