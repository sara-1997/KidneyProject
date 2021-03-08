package com.example.kidneyproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kidneyproject.Class.Constants;
import com.example.kidneyproject.Class.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    user User;
    private DatabaseReference mDatabase;
    EditText Email, Password;
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        initialfirebase();
        ///sendNotification("",new JSONObject());
    }

    private void initialfirebase() {
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    public void SignIn(View view) {
        if (CheckData() == false) {

            return;

        } else {

            if(Email.getText().toString().trim().equals("saraadmin@gmail.com")&&
                    Password.getText().toString().trim().equals("sara123"))
            {
                Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getApplicationContext(), MainAdminActivity.class);
                startActivity(intent);
                finish();
            }
            else

            mAuth.signInWithEmailAndPassword(Email.getText().toString().trim(), Password.getText().toString().trim()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                public void onComplete(@NonNull final Task<AuthResult> task) {

                    if (!task.isSuccessful()) {

                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_LONG).show();



                    } else {
                        Toast.makeText(getApplicationContext(), "Login Successfully", Toast.LENGTH_LONG).show();

                        FirebaseUser mCurrentUser = task.getResult().getUser();
                        final String userid = mCurrentUser.getUid();
                        final SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("userid",userid );
                        editor.commit();

                        final String token = sharedpreferences.getString("token", "");

                        Log.e("NEW_INACTIVITY_TOKEN", token);

                            FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LoginActivity.this, new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    String newToken = FirebaseInstanceId.getInstance().getToken();

                                    Log.e("newToken", newToken);
                                    DatabaseReference updateData = FirebaseDatabase.getInstance()
                                            .getReference("users")
                                            .child(userid);
                                    updateData.child("token").setValue(newToken);
                                    if (token!=null){
                                         updateData = FirebaseDatabase.getInstance()
                                                .getReference("users")
                                                .child(userid);
                                        updateData.child("token").setValue(newToken);

                                    }

                                }
                            });


                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            });
        }


        }

    private boolean CheckData() {
        boolean check = true;


        if (CheckEmail(Email) == false) {
            Email.setError("Enter valid Email");
            check = false;
        }

        if (CheckEmpty(Password)) {
            Password.setError("You must enter Password to  Log in !");
            check = false;
        }
        return check;


    }
    boolean CheckEmail(EditText text) {

        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean CheckEmpty(EditText text) {
        CharSequence String = text.getText().toString();
        return TextUtils.isEmpty(String);
    }

    public void Register(View view) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
       finish();
        super.onBackPressed();
    }

}
