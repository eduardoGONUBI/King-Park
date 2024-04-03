package com.example.trdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// This class represents the login activity of the app. It handles the login process
// by checking the entered phone number and password against the values stored in the Firebase Realtime Database.
public class Login extends AppCompatActivity {
    // A reference to the Firebase Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projpdm-8fe85-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
// Initialize EditTexts and Buttons from the layout
        final EditText phone = findViewById(R.id.phone);
        final EditText password = findViewById(R.id.password);
        final Button loginBtn = findViewById(R.id.loginBtn);
        final TextView registerNowBtn = findViewById(R.id.registerNowBtn);


        // Set an OnClickListener for the login button.
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final String phoneTxt = phone.getText().toString();
                final String passwordTxt = password.getText().toString();
// Create an intent to start the Edit activity and pass the value of the phoneTxt as an extra with the key "phoneTxt".
                Intent phoneid = new Intent(Login.this,Edit.class);
                phoneid.putExtra("phoneTxt",phoneTxt);
                //Check if the phone and password fields are empty.
                // If they are, display a Toast message indicating that all fields need to be filled in.
                if(phoneTxt.isEmpty()||passwordTxt.isEmpty()){
                    Toast.makeText(Login.this, "Please Enter All Fields", Toast.LENGTH_SHORT).show();
                }
                else{
                    // Add a ValueEventListener to the "users" child node in the Firebase Realtime Database.
                    // The onDataChange method will be called with a DataSnapshot of the data at this location.
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if phone exists in database
                            if(snapshot.hasChild(phoneTxt)){
                                //phone exists
                                //check if password introduced matches password in database
                                final String getPassword= snapshot.child(phoneTxt).child("password").getValue(String.class);
                                if(getPassword.equals(passwordTxt)){
                                    Toast.makeText(Login.this, "Success Logged In", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(Login.this,MainActivity.class);
                                    i.putExtra("phoneTxt",phoneTxt);
                                    Intent phoneid = new Intent(Login.this,Edit.class);
                                    phoneid.putExtra("phoneTxt",phoneTxt);
                                    startActivity(i);
                                    finish();
                                }
                                else{
                                    Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                                }

                            }else{
                                Toast.makeText(Login.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    }
                    );
                }

            }
        });
        // Set an OnClickListener for the registerNowBtn TextView. When clicked, it will start the Register activity.
        registerNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

    }
}