package com.example.kidneyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kidneyproject.Class.Wish;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddconsultActivity extends AppCompatActivity {
    EditText Titleconsult , Description ;
    private DatabaseReference mDatabase;
    SharedPreferences prefs;
    Consult Add;
    android.widget.Button Button ;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String NameActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addconsult);
        Titleconsult =(EditText) findViewById(R.id.Wishtitle);
        Description =(EditText) findViewById(R.id.Description);
        initialfirebase();

    }

    private void initialfirebase() {
        Add = new Consult();

        mDatabase = FirebaseDatabase.getInstance().getReference("Consult");
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        Add.setPatientId((prefs.getString("userid","2" )));

    }
    public void back(View view) {
        Intent intent = new Intent(this,ListConsultActivity.class);
        startActivity(intent);

    }
    public void Addconsult(View view) {
        if (CheckValueText() == false) {
            return;
        } else {


            Add.setDescriptipn(Description.getText().toString());
            Add.setTitle(Titleconsult.getText().toString());




            String Request = mDatabase.push().getKey();
            mDatabase.child(Request).setValue(Add);
            Toast.makeText(getApplicationContext(), "Add Successfully Consult", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(getApplicationContext(), ListConsultActivity.class);
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

        if (CheckEmpty(Titleconsult)) {
            Titleconsult.setError("You must enter title  to  Add!");
            check = false;
        }


        if (CheckEmpty(Description)) {
            Description.setError("You must enter Description  to  Add!");
            check = false;
        }


        return check;


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,ListConsultActivity.class);
        startActivity(intent);

        super.onBackPressed();
    }

}
