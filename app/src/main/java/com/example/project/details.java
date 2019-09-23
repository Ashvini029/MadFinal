package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class details extends AppCompatActivity {

    EditText editTextname, editTextdob,editTextage,editTextprofession,editTexthome,editTextstudies,editTextinterest,editTextfavourite,editTextphone;
    Button buttonsave, buttonclear;
    RadioButton radioButtonmale, radioButtonfemale;
    DatabaseReference db;
    userdetails ud;
    String gen;

    public void clearcontrols(){
        editTextname.setText("");
        editTextdob.setText("");
        editTextage.setText("");
        editTextprofession.setText("");
        editTexthome.setText("");
        editTextstudies.setText("");
        editTextinterest.setText("");
        editTextfavourite.setText("");
        editTextphone.setText("");
        radioButtonmale.setChecked(false);
        radioButtonfemale.setChecked(false);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        editTextname = findViewById(R.id.editTextname);
        editTextdob = findViewById(R.id.editTextdob);
        editTextage = findViewById(R.id.editTextage);
        editTextprofession = findViewById(R.id.editTextprofession);
        editTextfavourite = findViewById(R.id.editTextfavourite);
        editTexthome = findViewById(R.id.editTexthome);
        editTextinterest = findViewById(R.id.editTextinterest);
        editTextphone = findViewById(R.id.editTextphone);
        editTextstudies = findViewById(R.id.editTextstudies);

        buttonsave = findViewById(R.id.buttonsave);
        buttonclear = findViewById(R.id.buttonclear);

        radioButtonmale = findViewById(R.id.radioButtonmale);
        radioButtonfemale = findViewById(R.id.radioButtonfemale);

        ud = new userdetails();

        buttonclear.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                clearcontrols();
            }
        });

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = FirebaseDatabase.getInstance().getReference().child("userdetails");

                try{
                    if(!editTextname.getText().toString().matches("[a-zA-Z]+")){
                        editTextname.requestFocus();
                        editTextname.setError("Enter only alphabetical characters.");
                        //Toast.makeText(getApplicationContext(),"Enter only alpahbets for name",Toast.LENGTH_SHORT).show();
                    }
                    else if(radioButtonfemale.isChecked() && radioButtonmale.isChecked()){
                        Toast.makeText(getApplicationContext(),"Select only one gender type",Toast.LENGTH_LONG).show();
                        radioButtonmale.setChecked(false);
                        radioButtonfemale.setChecked(false);
                    }
                    else {
                        if(radioButtonmale.isChecked()){
                            gen = "MALE";
                        }
                        else if(radioButtonfemale.isChecked()){
                            gen = "FEMALE";
                        }
                        ud.setName(editTextname.getText().toString().trim());
                        ud.setAge(Integer.parseInt(editTextage.getText().toString().trim()));
                        ud.setDob(editTextdob.getText().toString().trim());
                        ud.setGender(gen);
                        ud.setHometown(editTexthome.getText().toString().trim());
                        ud.setFavourites(editTextfavourite.getText().toString().trim());
                        ud.setPhone(Integer.parseInt(editTextphone.getText().toString().trim()));
                        ud.setProfession(editTextprofession.getText().toString().trim());
                        ud.setStudiedat(editTextstudies.getText().toString().trim());
                        ud.setInterestedTo(editTextinterest.getText().toString().trim());

                        db.child("userdetails1").setValue(ud);

                        Toast.makeText(getApplicationContext(), "Data added successfully", Toast.LENGTH_LONG).show();
                        clearcontrols();
                        Intent intent = new Intent(details.this,Dashboard.class);
                        startActivity(intent);
                    }

                }catch(NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"Error occurred",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

}

