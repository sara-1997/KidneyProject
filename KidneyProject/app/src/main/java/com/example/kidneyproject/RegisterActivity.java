package com.example.kidneyproject;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.example.kidneyproject.Class.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
public class RegisterActivity extends AppCompatActivity {
    EditText Name, Email, Password, ConfirmPassword, Phone ,Map;
    RadioButton radioButton1, radioButton2;
    String ValueRadioButton = "";
    RadioGroup radioGroup;
    private FirebaseAuth mAuth;
    user User;
    private DatabaseReference mDatabase;
    private ImageView Location;
    TextView textView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Name = (EditText) findViewById(R.id.name);
        Email = (EditText) findViewById(R.id.email);
        Password = (EditText) findViewById(R.id.password);
        ConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        Phone = (EditText) findViewById(R.id.Phone);
        Location = (ImageView) findViewById(R.id.location);

        textView = (TextView) findViewById(R.id.HospitalName);

        radioButton1 = (RadioButton) findViewById(R.id.radio1);
        radioButton2 = (RadioButton) findViewById(R.id.radio2);
        radioGroup = (RadioGroup) findViewById(R.id.groupradio);
        linearLayout = (LinearLayout) findViewById(R.id.linearmap);


        User = new user();
        GetDataCheckbox();
        initialfirebase();
    }

    private void initialfirebase() {
        mAuth= FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }


    private boolean CheckData() {
        boolean check = true;

        if (CheckEmpty(Name)) {
            Name.setError("Enter valid Name to Register!");
            check = false;
        }

        if (CheckEmail(Email) == false) {
            Email.setError("Enter valid Email");
            check = false;
        }

        if (CheckEmpty(Password)) {
            Password.setError("You must enter Password to  Register!");
            check = false;
        }

        if (CheckEmpty(ConfirmPassword)) {
            ConfirmPassword.setError("You must enter Confirm Password to  Register!");
            check = false;
        }

        String pass = Password.getText().toString();
        String pass2 = ConfirmPassword.getText().toString();

        if (!pass.equals(pass2)) {
            ConfirmPassword.setError("Password and confirm password does not match");
            Password.setError("Password and confirm password does not match");

            check = false;
        }
        if (CheckEmpty(Phone)) {
            Phone.setError("You must enter Phone to Register!");
            check = false;
        }
        int selectedId = radioGroup.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this,
                    "No  has been selected",
                    Toast.LENGTH_SHORT)
                    .show();
            check = false;


        } else {

            RadioButton radioButton
                    = (RadioButton) radioGroup
                    .findViewById(selectedId);

            // Now display the value of selected item
            // by the Toast message
            ValueRadioButton = radioButton.getText().toString();

        }

        return check;
    }

    private void GetDataCheckbox() {

        radioGroup.setOnCheckedChangeListener(
                new RadioGroup
                        .OnCheckedChangeListener() {
                    @Override

                    // The flow will come here when
                    // any of the radio buttons in the radioGroup
                    // has been clicked

                    // Check which radio button has been clicked
                    public void onCheckedChanged(RadioGroup group,
                                                 int checkedId) {

                        // Get the selected Radio Button
                        RadioButton
                                radioButton
                                = (RadioButton) group
                                .findViewById(checkedId);
                        if (checkedId==R.id.radio1) {
                            ValueRadioButton = "Volunteer";
                            linearLayout.setVisibility(View.GONE);


                        }
                        if (checkedId==R.id.radio2) {
                            linearLayout.setVisibility(View.VISIBLE);

                            ValueRadioButton = "Patient";

                        }


                    }
                });


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if (Activity.RESULT_OK == resultCode) {
                    textView.setText(data.getStringExtra("HospitalName"));
                    User.setPlace( (data.getStringExtra("HospitalName")));
                    double latitude = data.getDoubleExtra("latitude",0.0);
                    double longitude= data.getDoubleExtra("longitude",0.0);

                    User.setLatitude(latitude);
                    User.setLongitude(longitude);



                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

        if (CheckData() == false) {

            return;

        } else {

            User.setName(Name.getText().toString());
            User.setPassword( Password.getText().toString());
            User.setPhone( Phone.getText().toString());
            User.setEmailAddress( Email.getText().toString());

            User.setTypeuser( ValueRadioButton);
            User.setToken( "Token");
            if(ValueRadioButton.equals("Volunteer"))
                User.setPlace( "Tabuk");
            User.setImageUrl( "Default");



            mAuth.createUserWithEmailAndPassword(User.getEmailAddress(),User.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (!task.isSuccessful()) {
                        try {
                            throw task.getException();
                        }
                        catch (FirebaseAuthInvalidCredentialsException e) {
                            Toast.makeText(getApplicationContext(), "Invalid Password , Week password", Toast.LENGTH_LONG).show();
                        }
                        catch (FirebaseAuthEmailException e){
                            Toast.makeText(getApplicationContext(), "Invalid Email , Error pattern ", Toast.LENGTH_LONG).show();
                        }
                        catch (FirebaseAuthException e){
                            Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e) {
                            e.printStackTrace();

                        }
                        Toast.makeText(getApplicationContext(), "User Already Register By the Same Email", Toast.LENGTH_LONG).show();


                    } else {
                        FirebaseUser mCurrentUser = task.getResult().getUser();
                        String userid = mCurrentUser.getUid();
                        mDatabase.child("users").child(userid).setValue(User);
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            });


        }
    }

    public void Back(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);

    }

    public void GetLocation(View view) {

        if(ValueRadioButton.equals("Patient")) {

            new AlertDialog.Builder(RegisterActivity.this)
                    .setTitle("Locate the hospital")
                    .setMessage("Go to Map")
                    .setPositiveButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "You must locate the hospital", Toast.LENGTH_LONG).show();

                        }
                    })
                    .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(RegisterActivity.this, MapsActivity.class);
                            intent.putExtra("NameActivity","Register");
                            startActivityForResult(intent, 1);

                        }
                    })
                    .show();
        }
        else
            Toast.makeText(getApplicationContext(), "Volunteer", Toast.LENGTH_LONG).show();


    }
}




