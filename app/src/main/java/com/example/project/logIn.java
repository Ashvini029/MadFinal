package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class logIn extends AppCompatActivity {
    EditText nEmail, nPwd;
    Button LogIn;
    TextView notHvtAcc;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("Log In");
        mAuth = FirebaseAuth.getInstance();
        //back button

        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setDisplayShowHomeEnabled(true);

        nEmail = findViewById(R.id.emailE);
        nPwd = findViewById(R.id.pwdE);
        LogIn = findViewById(R.id.login);
        notHvtAcc = findViewById(R.id.hvNotAcc);

        LogIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String email = nEmail.getText().toString();
                String password = nPwd.getText().toString().trim();

                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    nEmail.setError("Invalid Email");
                    nEmail.setFocusable(true);
                }
                else{
                    logInUser(email,password);
                }

                //startActivity(new Intent(logIn.this, Register.class));

            }
        });


        notHvtAcc.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(logIn.this, Register.class));

            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in");


    }


    private void logInUser(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (task.getResult().getAdditionalUserInfo().isNewUser()){
                                //get user email and uid from auth
                                String email = user.getEmail();
                                String uid = user.getUid();
                                //when user is registered store user info in firebase realtime database too
                                //using HashMap
                                HashMap<Object, String> hashMap = new HashMap<>();
                                //put info in hashmap
                                hashMap.put("email",email);
                                hashMap.put("uid", uid);
                                hashMap.put("name", "");
                                hashMap.put("phone", "");
                                hashMap.put("image", "");
                                hashMap.put("cover", "");
                                //firebase database isntance
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                //path to store user data named 'Users'
                                DatabaseReference reference = database.getReference("Users");
                                //put data within hashmap in database
                                reference.child(uid).setValue(hashMap);
                            }



                            startActivity(new Intent(logIn.this, Dashboard.class));
                            finish();

                        } else {
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(logIn.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }

    public boolean onSupportNavigateup()
    {
        onBackPressed();//go prevois activity
        return super.onSupportNavigateUp();
    }

}