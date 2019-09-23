package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;

public class home extends AppCompatActivity {

    EditText edittextcaption;
    ImageView imgview;
    Button buttonsave, buttonclear;
    DatabaseReference dbref;
    userhome uh;
    Uri imguri;
    private static final int PICK_IMAGE = 1;

    private void clearControls(){
        edittextcaption.setText("");
        imgview.setImageBitmap(null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        edittextcaption = findViewById(R.id.editTextcaption);
        imgview = findViewById(R.id.imgview);
        buttonsave = findViewById(R.id.buttonsave);
        buttonclear = findViewById(R.id.buttonclear);
        uh = new userhome();

        imgview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(gallery,"Select picture"), PICK_IMAGE);
            }
        });
        buttonclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearControls();
            }
        });

        buttonsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbref = FirebaseDatabase.getInstance().getReference().child("userhome");

                try{
                    if(edittextcaption.getText().toString().length()==0){
                        Toast.makeText(getApplicationContext(),"Nothing to save. Add details to save.",Toast.LENGTH_SHORT).show();}
                    else {
                        uh.setCaption(edittextcaption.getText().toString().trim());

                        dbref.child("userhome1").setValue(uh);

                        Toast.makeText(getApplicationContext(), "Data added successfully", Toast.LENGTH_SHORT).show();
                        Intent int1 = new Intent(home.this, details.class);
                        startActivity(int1);
                    }

                }catch (NumberFormatException e){
                    Toast.makeText(getApplicationContext(),"Some error occurred",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void GoToDetails(View view) {
        Intent intent = new Intent(home.this, details.class);
        startActivity(intent);
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imguri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imguri);
                imgview.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
