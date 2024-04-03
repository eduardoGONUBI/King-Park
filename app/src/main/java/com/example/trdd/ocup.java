package com.example.trdd;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ocup extends AppCompatActivity implements LocationListener {

    // Declare and initialize DatabaseReference and StorageReference objects for Firebase
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://projpdm-8fe85-default-rtdb.firebaseio.com");
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

    // Declare constants for the request code and the image view and buttons in the layout
    private final int CAMERA_REQ_CODE =  100;
    ImageView imgCamera1;
    Button btnLocation1;
    TextView addressloc1;
    LocationManager locationManager1;
    Button uploadBtn;
    EditText feedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocup);

        // Get a reference to the views in the layout
        addressloc1= findViewById(R.id.address1);
        imgCamera1 = findViewById(R.id.imgCamera1);
        Button btnCamera1 = findViewById(R.id.btnCamera1);
        btnLocation1 = findViewById(R.id.btnLocation1);
        uploadBtn= findViewById(R.id.uploadBtn1);
        feedback = findViewById(R.id.feedback1);

        // This onClickListener is called when the "Open Camera" button is clicked.
        // It starts the camera app and allows the user to take a picture.
        btnCamera1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(iCamera,CAMERA_REQ_CODE);

            }
        });
        // This block of code checks if the app has permission to access the device's location and request permission if it doesn't.
        if(ContextCompat.checkSelfPermission(ocup.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ocup.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},100);
        }
        // This onClickListener is called when the "Get location" button is clicked. It calls the getLocation() method.
        btnLocation1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation1();
            }
        });
        // This onClickListener is called when the "Upload" button is clicked.
        // It checks if the user has taken a photo and obtained their location, and if they have,
        // it calls the uploadImage() method and stores the user's feedback and location in the Firebase database.
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(addressloc1.getText().toString().equals("")||imgCamera1.getDrawable()==null||feedback.getText().toString().equals("")) {
                    Toast.makeText(ocup.this, "Please take a photo, get location and give some feedback!", Toast.LENGTH_SHORT).show();
                }
                else {
                    String addressloc3 = addressloc1.getText().toString();

                    databaseReference.child("occupied").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(addressloc3)) {
                                Toast.makeText(ocup.this, "Someone already registred this spot as occupied!", Toast.LENGTH_SHORT).show();
                            } else {
                                String lol = feedback.getText().toString();
                                Date currentTime = Calendar.getInstance().getTime();
                                int xx = currentTime.getHours();
                                int zz = currentTime.getMinutes();
                                int yy = currentTime.getDay();
                                int aa = currentTime.getMonth();
                                uploadImage();
                                databaseReference.child("ocuppied").child(addressloc3).child("feedback").setValue(lol);
                                databaseReference.child("ocuppied").child(addressloc3).child("address").setValue(addressloc3);
                                databaseReference.child("ocuppied").child(addressloc3).child("hour").setValue(xx + ":" + zz);
                                databaseReference.child("ocuppied").child(addressloc3).child("date").setValue(yy + "/" + aa);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
                }

        });


    }
    // This method is called to upload the user's photo and location
    private void uploadImage(){

        String addressloc2 = addressloc1.getText().toString();
        StorageReference ocu = storageReference.child("Occupied Parking");
        StorageReference ref = ocu.child(addressloc2);

        imgCamera1.setDrawingCacheEnabled(true);
        imgCamera1.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgCamera1.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = ref.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(ocup.this, "Unsuccess", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ocup.this, "Success", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @SuppressLint("MissingPermission")
    private void getLocation1(){
        // It initializes the 'locationManager' object using the getSystemService() method and the LOCATION_SERVICE constant.
        // It requests location updates from the GPS provider using the 'requestLocationUpdates()' method of the 'locationManager' object.
        // The updates will be sent every 5 seconds with a minimum distance change of 5 meters.
        try{
            locationManager1=(LocationManager)  getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager1.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,5,ocup.this);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,@Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        // Check if the result is okay and the request code is the camera request code
        if(resultCode==RESULT_OK){
            if(requestCode==CAMERA_REQ_CODE){
                // Get the image from the intent's extras and set it to the ImageView
                Bitmap img1 = (Bitmap) data.getExtras().get("data");
                imgCamera1.setImageBitmap(img1);
            }
        }


    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Display the latitude and longitude in a toast message
        Toast.makeText(this, ""+location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();

        // Try to get the address from the latitude and longitude
        try{
            Geocoder geocoder= new Geocoder(ocup.this, Locale.getDefault());
            List<Address> addresses2 = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            String address2 = addresses2.get(0).getAddressLine(0);
            // Set the address to the TextView
            addressloc1.setText(address2);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }
/*
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }
*/
    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}