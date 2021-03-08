package com.example.kidneyproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kidneyproject.Class.visit;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

public class AddVisitRequestActivity extends AppCompatActivity {
    TextView VisitDate;
    TextView VisitTime, HospitalName , title;
    EditText Description;
    DatePickerDialog picker;
    Button Button ;
    private DatabaseReference mDatabase;
    SharedPreferences prefs;
    int flag=1 ,flag4=1 ,flag2=1,flag3=1;

    visit Addvisit;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String NameActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_visit_request);
        final Intent intent = getIntent();

        NameActivity= intent.getStringExtra("Activity");
        VisitDate = (TextView) findViewById(R.id.DateVisit);
        title = (TextView) findViewById(R.id.title);
        Button=(Button) findViewById(R.id.button);
        VisitTime = (TextView) findViewById(R.id.time);
        HospitalName = (TextView) findViewById(R.id.HospitalName);
        Description = (EditText) findViewById(R.id.Description);
        initialfirebase();

        if (NameActivity.equals("List"))
        {
            VisitDate.setText(intent.getStringExtra("VisitDate"));
            VisitTime.setText(intent.getStringExtra("VisitTime"));
            HospitalName.setText(intent.getStringExtra("HospitalName"));
            Description.setText(intent.getStringExtra("Description"));
            Button.setText("Update Request");
            Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Update Visit Information
                    DatabaseReference updateData = FirebaseDatabase.getInstance()
                            .getReference("RequestVisit")
                            .child( intent.getStringExtra("Key"));

                    if (flag==0)
                        updateData.child("Place").setValue(Addvisit.getPlace());

                     if (flag2==0)
                     {
                         updateData.child("day").setValue(Addvisit.getDay());
                         updateData.child("year").setValue(Addvisit.getYear());
                         updateData.child("Month").setValue(Addvisit.getMonth());
                     }

                 if (flag3==0)
                     updateData.child("Description").setValue(Description.getText().toString());

                 if(flag4==0)
                 {
                     updateData.child("Hour").setValue(Addvisit.getHour());
                     updateData.child("minut").setValue(Addvisit.getMinut());
                 }

                    Toast.makeText(getApplicationContext(),"Update Visit Request ",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(),ListVisitPatientActivity.class));
                    finish();



                }
            });

        }


    }

    private void initialfirebase() {
        Addvisit = new visit();

        mDatabase = FirebaseDatabase.getInstance().getReference("RequestVisit");
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Addvisit.setPatientId( prefs.getString("userid","2" ));
        Addvisit.setPlace( prefs.getString("place","Tabuk" ));
        HospitalName.setText(Addvisit.getPlace());
        Addvisit.setLongitude( Double.longBitsToDouble(prefs.getLong("Longitude",
                Double.doubleToLongBits(25.1))));
        Addvisit.setLatitude(Double.longBitsToDouble(prefs.getLong("Latitude",
                Double.doubleToLongBits(25.1))));



    }


    public void back(View view) {
        Intent intent = new Intent(getApplicationContext(), ListVisitPatientActivity.class);
        startActivity(intent);
    }

    public void GetDateVisit(View view) {

        final Calendar cldr = Calendar.getInstance();
        final int day = cldr.get(Calendar.DAY_OF_MONTH);
        final int month = cldr.get(Calendar.MONTH);
        int year = cldr.get(Calendar.YEAR);
        picker = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        CharSequence text = "" + dayOfMonth + "-" + "" + monthOfYear + "-" + ""
                                + year;
                        VisitDate.setText(text);
                        Date date = new Date();
                        date.setMonth(monthOfYear);
                        date.setDate(dayOfMonth);
                        date.setYear(year);
                        Addvisit.setDay(dayOfMonth);
                        Addvisit.setMonth(monthOfYear);
                        Addvisit.setYear(year);
                        flag2=0;


                    }
                }, year, month+1, day);

        picker.show();


    }



    public void RequestSave(View view) {
        if (CheckValueText()==false)
        {
            return;
        }
        else
        {

            Addvisit.setDescription(Description.getText().toString());
            Addvisit.setStatues("Wait");
            Addvisit.setVolunteerId("0");


            String RequestVisit = mDatabase.push().getKey();
            mDatabase.child(RequestVisit).setValue(Addvisit);
            Toast.makeText(getApplicationContext(), "Add Successfully request", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), ListVisitPatientActivity.class);
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

        if (CheckEmpty(HospitalName)) {
            HospitalName.setError("Enter valid hospitalName!");
            check = false;
        }


        if (CheckEmpty(VisitDate)) {
            VisitDate.setError("You must enter Visit Date to  Add!");
            check = false;
        }

        if (TextUtils.isEmpty(Description.getText())) {

            Description.setError("You must enter Description  to  Add");
            check = false;
        }
        return check;


    }

    public void GetTime(View view) {
        final Calendar c = Calendar.getInstance();
        final int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);


        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        flag4=0;
                        VisitTime.setText(hourOfDay + ":" + minute);
                        Addvisit.setHour(hourOfDay );
                        Addvisit.setMinut(minute );



                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,ListVisitPatientActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }


}
