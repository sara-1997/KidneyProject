package com.example.kidneyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class MenuVisitActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences prefs;
    private String UserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_visit);
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        UserType= prefs.getString("UserType","2" );

    }

    public void back(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);

        startActivity(intent);
    }

    public void VisitManage(View view) {

        try {
            TimeUnit.SECONDS.sleep(2);
            if (UserType.equals("Volunteer"))
            {
                Intent intent = new Intent(getApplicationContext(), ListVisitActivity.class);

                startActivity(intent);

            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), ListVisitPatientActivity.class);

                startActivity(intent);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void ListAccept(View view) {

        Intent intent = new Intent(getApplicationContext(), ListAcceptActivity.class);

        startActivity(intent);
    }
}
