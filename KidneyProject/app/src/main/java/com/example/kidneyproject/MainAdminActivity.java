package com.example.kidneyproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

public class MainAdminActivity extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userid;
    DatabaseReference reference;
    SharedPreferences prefs;
    String UserType;
    private Toolbar toolbar;
    CardView card ;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);
        reference = FirebaseDatabase.getInstance().getReference("users");
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

    }

    public void ListSugisstion(View view) {
        Intent intent = new Intent(getApplicationContext(), ListSuggestionsActivity.class);

        startActivity(intent);

    }

    public void Logout(View view) {
        new AlertDialog.Builder(MainAdminActivity.this)
                .setTitle("Log out")
                .setMessage("Would you like to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainAdminActivity.this,LoginActivity.class);
                        SharedPreferences settings = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        settings.edit().clear().commit();
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(MainAdminActivity.this)
                .setTitle("Log out")
                .setMessage("Would you like to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainAdminActivity.this,LoginActivity.class);
                        SharedPreferences settings = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        settings.edit().clear().commit();
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }
}
