package com.example.trdd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Edit extends AppCompatActivity {

    // Declare a reference to the Firebase Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projpdm-8fe85-default-rtdb.firebaseio.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Get references to the EditText views and Button view
        final EditText fullname1 = findViewById(R.id.fullname1);
        final EditText email1 = findViewById(R.id.email1);
        final EditText password1 = findViewById(R.id.password1);
        final EditText conpassword1 = findViewById(R.id.conPassword1);
        final Button submitBtn = findViewById(R.id.submitBtn);

        // Set an OnClickListener on the submit button
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the text from the EditText views
                final String fullnameTxt = fullname1.getText().toString();
                final String emailTxt = email1.getText().toString();
                final String passwordTxt = password1.getText().toString();
                final String conPasswordTxt = conpassword1.getText().toString();
                // Get the value from the intent extras
                String teste2 = getIntent().getExtras().getString("phoneTxt");

                // Check if all the EditText views are empty
                if (fullnameTxt.isEmpty() && passwordTxt.isEmpty() && conPasswordTxt.isEmpty() && emailTxt.isEmpty()) {
                    // Display a toast message and finish the activity if all the EditText views are empty
                    Toast.makeText(Edit.this, "You didnt Change Anything LOL :)", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Check if the password and confirm password fields match
                    if (!passwordTxt.equals(conPasswordTxt)) {
                        // Display a toast message if the passwords do not match
                        Toast.makeText(Edit.this, "Passwords Must Match", Toast.LENGTH_SHORT).show();
                    }

                    // Check if all the fields are not empty
                    if (!emailTxt.isEmpty() && !passwordTxt.isEmpty() && !fullnameTxt.isEmpty() && !conPasswordTxt.isEmpty()) {
                        // Update the full name, email, and password fields in the Firebase Realtime Database with the
                        // new values if all the fields are not empty

                        databaseReference.child("users").child(teste2).child("fullname").removeValue();
                        databaseReference.child("users").child(teste2).child("fullname").setValue(fullnameTxt);
                        databaseReference.child("users").child(teste2).child("email").removeValue();
                        databaseReference.child("users").child(teste2).child("email").setValue(emailTxt);
                        databaseReference.child("users").child(teste2).child("password").removeValue();
                        databaseReference.child("users").child(teste2).child("password").setValue(passwordTxt);
                        finish();
                    }
                    // Check if the full name and password fields are not empty, and the email field is empty
                    if (fullnameTxt.isEmpty() && !emailTxt.isEmpty()&&!passwordTxt.isEmpty()) {
                        // Update the email and password fields in the Firebase Realtime Database with the new values
                        // if the full name and password fields are not empty, and the email field is empty
                        databaseReference.child("users").child(teste2).child("email").removeValue();
                        databaseReference.child("users").child(teste2).child("email").setValue(emailTxt);
                        databaseReference.child("users").child(teste2).child("password").removeValue();
                        databaseReference.child("users").child(teste2).child("password").setValue(passwordTxt);
                        finish();
                    }
                    // Check if the email and password fields are not empty, and the full name field is empty
                    if (emailTxt.isEmpty()&&!fullnameTxt.isEmpty()&&!passwordTxt.isEmpty()) {
                        // Update the full name and password fields in the Firebase Realtime Database with the new
                        // values if the email and password fields are not empty, and the full name field is empty
                        databaseReference.child("users").child(teste2).child("fullname").removeValue();
                        databaseReference.child("users").child(teste2).child("fullname").setValue(fullnameTxt);
                        databaseReference.child("users").child(teste2).child("password").removeValue();
                        databaseReference.child("users").child(teste2).child("password").setValue(passwordTxt);
                        finish();
                    }
                    // Check if the full name and email fields are not empty, and the password field is empty
                    if (passwordTxt.isEmpty()&&!fullnameTxt.isEmpty()&&!emailTxt.isEmpty()) {
                        // Update the full name and email fields in the Firebase Realtime Database with the new
                        // values if the full name and email fields are not empty, and the password field is empty
                        databaseReference.child("users").child(teste2).child("fullname").removeValue();
                        databaseReference.child("users").child(teste2).child("fullname").setValue(fullnameTxt);
                        databaseReference.child("users").child(teste2).child("email").removeValue();
                        databaseReference.child("users").child(teste2).child("email").setValue(emailTxt);
                        finish();
                    }
                    else {
                        // Check if the email field is not empty and the full name and password fields are empty
                        if (fullnameTxt.isEmpty() && passwordTxt.isEmpty()&&!emailTxt.isEmpty()) {
                            // Update the email field in the Firebase Realtime Database with the new value if the email field is not empty and the full name and password fields are empty
                            databaseReference.child("users").child(teste2).child("email").removeValue();
                            databaseReference.child("users").child(teste2).child("email").setValue(emailTxt);
                            finish();
                        }
                        // Check if the password field is not empty and the full name and email fields are empty
                        if (fullnameTxt.isEmpty() && emailTxt.isEmpty()&&!passwordTxt.isEmpty()) {
                            // Update the password field in the Firebase Realtime Database with the new value if the password field is not empty and the full name and email fields are empty
                            databaseReference.child("users").child(teste2).child("password").removeValue();
                            databaseReference.child("users").child(teste2).child("password").setValue(passwordTxt);
                            finish();
                        }
                        // Check if the full name field is not empty and the email and password fields are empty
                        if (emailTxt.isEmpty() && passwordTxt.isEmpty()&&!fullnameTxt.isEmpty()) {
                            // Update the full name field in the Firebase Realtime Database with the new value if the full name field is not empty and the email and password fields are empty
                            databaseReference.child("users").child(teste2).child("fullname").removeValue();
                            databaseReference.child("users").child(teste2).child("fullname").setValue(fullnameTxt);
                            finish();
                        }
                    }
                }
            }
        });
    }
}
