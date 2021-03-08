package com.example.kidneyproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kidneyproject.Class.talent;
import com.example.kidneyproject.Class.user;
import com.example.kidneyproject.Class.visit;
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

public class AddTalentActivity extends AppCompatActivity {
      EditText Title ;
      ImageView imageTalent;
    private DatabaseReference mDatabase;
    SharedPreferences prefs;
    talent Addtalent;
    public static final String MyPREFERENCES = "MyPrefs" ;
    StorageReference storageReference;
    private static final int IMAGE_REQUEST=1;
    private  Uri imageuri;
    private StorageTask uploadTask;
    private String NameActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_talent);
        final Intent intent = getIntent();

        NameActivity= intent.getStringExtra("Activity");
        Title = (EditText) findViewById(R.id.Titleitle);
        imageTalent = (ImageView) findViewById(R.id.ImageTalent);
        initialfirebase();

    }


    public void AddTalent(View view) {
        if (TextUtils.isEmpty(Title.getText().toString()))
        {

            return;
        }
        else
        {

            Addtalent.setTitle(Title.getText().toString());
            Addtalent.setVolunteerId("0");


            String RequestVisit = mDatabase.push().getKey();
            mDatabase.child(RequestVisit).setValue(Addtalent);
            Toast.makeText(getApplicationContext(), "Add Successfully Talent", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(), ListTalentActivityForPatient.class);
            startActivity(intent);
            finish();


        }
    }
    private void initialfirebase() {
        Addtalent = new talent();
        storageReference= FirebaseStorage.getInstance().getReference("users");


        mDatabase = FirebaseDatabase.getInstance().getReference("Talent");
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Addtalent.setPatientId( prefs.getString("userid","2" ));

        Addtalent.setNumberlike(0);
        Addtalent.setLikemy("No");
        DatabaseReference reference;
        reference = FirebaseDatabase.getInstance().getReference("users");

        reference.child(prefs.getString("userid","2" )).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Addtalent.setImagePatient(dataSnapshot.getValue(user.class).getImageUrl());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }

    public void back(View view) {
        Intent intent = new Intent(getApplicationContext(), ListTalentActivityForPatient.class);
        startActivity(intent);
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
        final ProgressDialog progressBar= new ProgressDialog(AddTalentActivity.this
        );
        progressBar.setMessage("Uploading");
        progressBar.show();
        if(imageuri!=null)
        {
            final StorageReference storageReference1=storageReference.child(System.currentTimeMillis()+"."+
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
                        Addtalent.setImageTalent(mUri);

                        progressBar.dismiss();


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
}
