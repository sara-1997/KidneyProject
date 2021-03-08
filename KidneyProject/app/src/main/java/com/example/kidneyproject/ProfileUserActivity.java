package com.example.kidneyproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kidneyproject.Class.user;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ProfileUserActivity extends AppCompatActivity {
    EditText Email, Phone, Password ;
    Button UpdateProfile;
    EditText Name;
    TextView Passwor ,username;
    ImageView Edit;
    String UserId;
    DatabaseReference reference;
    Intent intent;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST=1;
    private  Uri imageuri;
    private StorageTask uploadTask;
    ImageView imageView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_user);
        //Initial Value
        storageReference= FirebaseStorage.getInstance().getReference("users");

        Email = (EditText) findViewById(R.id.email);
        Phone = (EditText) findViewById(R.id.Phone);
        Password = (EditText) findViewById(R.id.password);
        imageView=(ImageView) findViewById(R.id.image) ;

        UpdateProfile = (Button) findViewById(R.id.UpdateProfile);
        Name = (EditText) findViewById(R.id.name);
        Passwor = (TextView) findViewById(R.id.passtext);
        username = (TextView) findViewById(R.id.username);


        reference = FirebaseDatabase.getInstance().getReference("users");
         intent = getIntent();






            UserId=intent.getStringExtra("UserId");
            getInformationUser(UserId);












    }

    private void getInformationUser(String userId) {
        reference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
            username.setText(dataSnapshot.getValue(user.class).getName());
                Name.setText(dataSnapshot.getValue(user.class).getName());
                Email.setText(dataSnapshot.getValue(user.class).getEmailAddress());
                Phone.setText(dataSnapshot.getValue(user.class).getPhone());
                Password.setText(dataSnapshot.getValue(user.class).getPassword());
                if (dataSnapshot.getValue(user.class).getTypeuser().equals("Patient") &&
                        dataSnapshot.getValue(user.class).getImageUrl().equals("Default"))

                Glide.with(getApplicationContext())
                        .load("https://firebasestorage.googleapis.com/v0/b/kidneyproject-82cf0.appspot.com/o/users%2Fblood-pressure.png?alt=media&token=13ca2a64-1a89-4efb-95b5-c378d82201a8")
                        .into(imageView);
                else
                if (dataSnapshot.getValue(user.class).getTypeuser().equals("Volunteer") &&
                        dataSnapshot.getValue(user.class).getImageUrl().equals("Default"))

                    Glide.with(getApplicationContext())
                            .load("https://firebasestorage.googleapis.com/v0/b/kidneyproject-82cf0.appspot.com/o/users%2Fvolunteer.png?alt=media&token=2788c0ce-245c-4e82-95ae-6bff47f90b18")
                            .into(imageView);
                else

                    Glide.with(getApplicationContext())
                            .load(   dataSnapshot.getValue(user.class).getImageUrl())
                            .into(imageView);


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void Update(View view) {
        UserId=intent.getStringExtra("UserId");

        DatabaseReference updateData = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(UserId);
        updateData.child("emailAddress").setValue(Email.getText().toString().trim());

        updateData.child("name").setValue(Name.getText().toString().trim());
        updateData.child("password").setValue(Password.getText().toString());
        updateData.child("phone").setValue(Phone.getText().toString());
        Toast.makeText(getApplicationContext(),"Update User Information Successfully", Toast.LENGTH_LONG).show();



    }

    public void SaveImage(View view) {
        openImage();

    }

    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST);

    }
    private String getFileExtention(Uri uri)
    {
        String extension;
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        extension= mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
        return extension;
    }
    private void UploadImage()
    {
        final ProgressDialog progressBar= new ProgressDialog(ProfileUserActivity.this
        );
        progressBar.setMessage("Uploading");
        progressBar.show();
        if(imageuri!=null)
        {
            final StorageReference storageReference1=storageReference.child(Name.getText().toString().trim()+System.currentTimeMillis()+"."+
                    getFileExtention(imageuri));
            uploadTask=storageReference1.putFile(imageuri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful())
                        Toast.makeText(getApplicationContext(),  task.getException().toString(), Toast.LENGTH_LONG).show();

                   return storageReference1.getDownloadUrl();

                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task <Uri>task) {
                    if (task.isSuccessful())
                    {
                        Uri Download =task.getResult();
                        String mUri=Download.toString();
                        HashMap<String,Object> map=new HashMap<>();
                        map.put("imageUrl",mUri);
                        FirebaseDatabase.getInstance()
                                .getReference("users")
                                .child(UserId).updateChildren(map);

                        progressBar.dismiss();
                        reference.child(UserId).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Uploading Failed", Toast.LENGTH_LONG).show();
                        progressBar.dismiss();

                    }


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    progressBar.dismiss();


                }
            });

        }
        else
            Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_LONG).show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            imageuri=data.getData();


        }
        if (uploadTask!=null && uploadTask.isInProgress())
        {
            Toast.makeText(getApplicationContext(), "Upload Progress", Toast.LENGTH_LONG).show();

        }
        else {
            UploadImage();

        }

    }

    public void back(View view) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);


    }
}
