package com.example.kidneyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.kidneyproject.Class.Chat;
import com.example.kidneyproject.Class.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ProfileChatActivity extends AppCompatActivity {
    String ReciveId;
    TextView textView;
    ImageView imageView;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private DatabaseReference mDatabase;
    SharedPreferences prefs;
    private String UserType;
    private String Userid;
    String ImageUrl;
    private String Token;
    private String UserName;
    String NameActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_chat);
        Intent intent= getIntent();
        NameActivity=intent.getStringExtra("Activity");
        textView=(TextView)findViewById(R.id.username) ;
        imageView= (ImageView) findViewById(R.id.image) ;
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("users");
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        UserType=prefs.getString("UserType","2" );
        Userid=prefs.getString("userid","2" );
        ReciveId=prefs.getString("ReciveId","2" );

        data.child(ReciveId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                textView.setText(dataSnapshot.getValue(user.class).getName());
                Token=dataSnapshot.getValue(user.class).getToken();

                ImageUrl=dataSnapshot.getValue(user.class).getImageUrl();

                if( dataSnapshot.getValue(user.class).getImageUrl().equals("Default") &&UserType.equals("Volunteer"))
                    imageView.setImageResource(R.drawable.patient);
                else  if( dataSnapshot.getValue(user.class).getImageUrl().equals("Default") &&UserType.equals("Patient"))
                    imageView.setImageResource(R.drawable.volunteer);
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

    public void Chat(View view) {
        Intent intent = new Intent(getApplicationContext(),ChatActivity.class);
        startActivity(intent);

    }

    public void back(View view) {
        if(NameActivity.equals("Notification")) {
            Intent intent = new Intent(getApplicationContext(), ListNotificationActivity.class);
            startActivity(intent);
        }
        else
        if (NameActivity.equals("Consult"))
        {
            Intent intent = new Intent(getApplicationContext(), ListConsultActivity.class);
            startActivity(intent);
        }
        else
        if (NameActivity.equals("Suggestions"))
        {
            Intent intent = new Intent(getApplicationContext(), ListSuggestionsActivity.class);
            startActivity(intent);
        }
        else
        if (NameActivity.equals("ManageVisit") &&UserType.equals("Volunteer"))
        {
            Intent intent = new Intent(getApplicationContext(), ListVisitActivity.class);
            startActivity(intent);
        }
        else
        if (NameActivity.equals("ManageVisit") &&UserType.equals("Patient"))
        {
            Intent intent = new Intent(getApplicationContext(), ListVisitPatientActivity.class);
            startActivity(intent);
        }
        else
        if (NameActivity.equals("Wish") && UserType.equals("Patient"))
        {
            Intent intent = new Intent(getApplicationContext(), ListWishForPatientActivity.class);
            startActivity(intent);
        }
        else
        if (NameActivity.equals("Wish") && UserType.equals("Volunteer"))
        {
            Intent intent = new Intent(getApplicationContext(), ListWishFortVolunteerActivity.class);
            startActivity(intent);
        }


    }

}
