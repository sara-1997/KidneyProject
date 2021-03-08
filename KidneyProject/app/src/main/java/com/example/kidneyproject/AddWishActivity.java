package com.example.kidneyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kidneyproject.Class.Wish;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddWishActivity extends AppCompatActivity {
    EditText TitleWish , Description, Place  ;
    private DatabaseReference mDatabase;
    SharedPreferences prefs;
    Wish AddWish;
    android.widget.Button Button ;
    int flag=1 ;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private String NameActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_wish);
        final Intent intent = getIntent();
        NameActivity= intent.getStringExtra("Activity");

        Button=(Button) findViewById(R.id.button);

        TitleWish =(EditText) findViewById(R.id.Wishtitle);
        Description =(EditText) findViewById(R.id.Description);
        Place =(EditText) findViewById(R.id.Place);
        initialfirebase();
        if (NameActivity.equals("List"))
        {
            TitleWish.setText(intent.getStringExtra("Title"));
            Description.setText(intent.getStringExtra("VisitTime"));
            Place.setText(intent.getStringExtra("Place"));
            Description.setText(intent.getStringExtra("Description"));
            Button.setText("Update Request");
            Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Update Visit Information
                    DatabaseReference updateData = FirebaseDatabase.getInstance()
                            .getReference("RequestWish")
                            .child( intent.getStringExtra("Key"));
                    AddWish.setDescription(Description.getText().toString());
                    AddWish.setWishTitle(TitleWish.getText().toString());
                    AddWish.setPlace(Place.getText().toString());
                    if (flag==1)
                    updateData.child("place").setValue(AddWish.getPlace());

                    updateData.child("wishTitle").setValue(TitleWish.getText().toString());
                        updateData.child("description").setValue(Description.getText().toString());



                    Toast.makeText(getApplicationContext(),"Update Wish Request ",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),ListWishForPatientActivity.class));
                    finish();



                }
            });

        }
    }

    private void initialfirebase() {
        AddWish = new Wish();

        mDatabase = FirebaseDatabase.getInstance().getReference("RequestWish");
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        AddWish.setPatientId((prefs.getString("userid","2" )));
        AddWish.setPlace( prefs.getString("place","Tabuk" ));
        Place.setText(AddWish.getPlace());

        AddWish.setLongitude( Double.longBitsToDouble(prefs.getLong("Longitude",
                Double.doubleToLongBits(25.1))));
        AddWish.setLatitude(Double.longBitsToDouble(prefs.getLong("Latitude",
                Double.doubleToLongBits(25.1))));


    }


    public void back(View view) {
        Intent intent = new Intent(this,ListWishForPatientActivity.class);
        startActivity(intent);

    }
    public void RequestSave(View view) {
        if (CheckValueText()==false)
        {
            return;
        }
        else
        {

            flag = 0;

            AddWish.setDescription(Description.getText().toString());
            AddWish.setWishTitle(TitleWish.getText().toString());
            AddWish.setPlace(Place.getText().toString());

            AddWish.setStatus("Wait");
            AddWish.setVolunteerId("0");


            String RequestVisit = mDatabase.push().getKey();
            mDatabase.child(RequestVisit).setValue(AddWish);
            Toast.makeText(getApplicationContext(), "Add Successfully Wish", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), ListWishForPatientActivity.class);
            startActivity(intent);
            finish();


        }

    }


    boolean CheckEmpty(TextView text) {
        CharSequence String = text.getText().toString();
        return TextUtils.isEmpty(String);
    }

    private boolean CheckValueText() {

        boolean check = true;

        if (CheckEmpty(TitleWish)) {
            TitleWish.setError("Enter valid wish title!");
            check = false;
        }


        if (CheckEmpty(Place)) {
            Place.setError("You must enter your hospital location!");
            check = false;
        }

        if (TextUtils.isEmpty(Description.getText())) {

            Description.setError("You must enter a description to add");
            check = false;
        }
        return check;


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,ListWishForPatientActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}
