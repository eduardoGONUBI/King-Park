package com.example.trdd;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;

// This class represents the main activity of the app. It handles the UI and
// user interactions with buttons in the layout.
public class MainActivity extends AppCompatActivity {


    // A reference to the Firebase Realtime Database
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projpdm-8fe85-default-rtdb.firebaseio.com");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    // References to create alert dialogs
    AlertDialog dialog;
    AlertDialog.Builder builder;
    String[] items={"Yes","No"};
    String result="";
    ArrayList<String> mfrees2=new ArrayList<>();
    ArrayList<String> mocups2=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons from the layout
        final Button editBtn = (Button) findViewById(R.id.editBtn);
        final Button freeBtn = (Button) findViewById(R.id.freeBtn);
        final Button ocupBtn = (Button) findViewById(R.id.ocupBtn);
        final Button deleteBtn = (Button) findViewById(R.id.deleteBtn);
        final Button logoutBtn = (Button) findViewById(R.id.logoutBtn);
        final Button showfBtn = (Button) findViewById(R.id.showfBtn);
        final Button showoBtn= (Button) findViewById(R.id.showoBtn);

        showoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref2,ref3,ref4;
                ref2 = ref1.child("ocuppied");

                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            mocups2.add(String.valueOf(dsp.getValue())); //add result into array list
                        }
                        builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Occupied Spots");
                        if(mocups2.isEmpty()){
                            builder.setMessage("No Occupied Parking Spots Registred");
                        }else {
                            String s = "";
                            for (int i = 0; i <= mocups2.size() - 1; i++) {
                                String lol = mocups2.get(i).replaceAll("hour", "");

                                String[] t = lol.split("=");

                                s += "Occupied Parking at " + t[2] + System.lineSeparator() + "Feeback about this says that:'"+t[4]+"'"+ System.lineSeparator()+ System.lineSeparator();
                            }
                            builder.setMessage(s);
                        }
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String value = getIntent().getExtras().getString("phoneTxt");
                                Intent teste = new Intent(MainActivity.this,MainActivity.class);
                                teste.putExtra("phoneTxt",value);
                                startActivity(teste);
                            }
                        });
                        dialog =builder.create();
                        dialog.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

        showfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                DatabaseReference ref1= FirebaseDatabase.getInstance().getReference();
                DatabaseReference ref2,ref3,ref4;
                ref2 = ref1.child("free");

                ref2.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //ArrayList<String> mfrees2=new ArrayList<String>();
                        for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                            mfrees2.add(String.valueOf(dsp.getValue())); //add result into array list
                        }
                        builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Free Spots");
                        if(mfrees2.isEmpty()){
                            builder.setMessage("No Free Parking Spots Available");
                        }else {
                            String s = "";
                            for (int i = 0; i <= mfrees2.size() - 1; i++) {
                                String lol = mfrees2.get(i).replaceAll("hour", "");

                                String[] t = lol.split("=");

                                s += "Free Parking at " + t[2] + System.lineSeparator() + "Feeback about this says that:'"+t[4]+"'"+ System.lineSeparator()+ System.lineSeparator();
                            }
                            builder.setMessage(s);
                        }
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String value = getIntent().getExtras().getString("phoneTxt");
                                Intent teste = new Intent(MainActivity.this,MainActivity.class);
                                teste.putExtra("phoneTxt",value);
                                startActivity(teste);
                            }
                        });
                        dialog =builder.create();
                        dialog.show();


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });


            }
        });



        // Set an OnClickListener for the logout button. When clicked, it will start the Login activity.
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure you wanna logout?");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        result = items[which];
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(result=="Yes"){
                            startActivity(new Intent(MainActivity.this,Login.class));
                        }
                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog =builder.create();
                dialog.show();
            }
        });

        // Set an OnClickListener for the edit button. When clicked, it will start the Edit activity
        // and pass the value of the phoneTxt extra from the intent as an extra with the key "value".
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = getIntent().getExtras().getString("phoneTxt");
                Intent teste = new Intent(MainActivity.this,Edit.class);
                teste.putExtra("phoneTxt",value);
                startActivity(teste);
            }
        });

        // Set an OnClickListener for the delete button. When clicked, it will retrieve the value of the
        // phoneTxt extra from the intent and remove the child node at "users/<value>" in the Firebase
        // Realtime Database. It will then start the Login activity and finish the current activity.
         deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value=getIntent().getExtras().getString("phoneTxt");
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Are you sure you wanna delete your account?");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        result = items[which];
                    }
                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(result=="Yes"){
                            databaseReference.child("users").child(value).removeValue();
                            startActivity(new Intent(MainActivity.this,Login.class));
                        }
                    }


                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog =builder.create();
                dialog.show();
            }
        });
        // Set an OnClickListener for the free button. When clicked, it will start the free activity.
        freeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,free.class));
            }
        });
        // Set an OnClickListener for the ocup button. When clicked, it will start the ocup activity.
        ocupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ocup.class));
            }
        });


    }
}