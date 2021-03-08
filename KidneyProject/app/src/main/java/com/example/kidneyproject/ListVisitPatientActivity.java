package com.example.kidneyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.kidneyproject.Class.visit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListVisitPatientActivity extends AppCompatActivity {
    private DatabaseReference reference;
    SharedPreferences prefs;
    String UserId , UserType;
    visit visit ;

    public static final String MyPREFERENCES = "MyPrefs" ;

    List<visit> ListRequest ;
    RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_visit_patient);
        initialfirebase();
        getallVisit();


    }

    private void getallVisit() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                visit post = null;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String Key = postSnapshot.getKey();

                    post = postSnapshot.getValue(visit.class);

                    post.setKeyVisit(Key);


                    if(post.getPatientId().equals(UserId) && post.getStatues().equals("Wait"))
                    {

                        ListRequest.add(post);
                    }


                }
                AdapterVisit adapter = new AdapterVisit(ListRequest , ListVisitPatientActivity.this,UserType);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void initialfirebase() {

        reference = FirebaseDatabase.getInstance().getReference("RequestVisit");
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        ListRequest=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);

        UserId= prefs.getString("userid","2" );
        UserType=prefs.getString("UserType","2" );





    }
    public void AddRequestVisit(View view) {
        Intent intent = new Intent(this,AddVisitRequestActivity.class);
        intent.putExtra("Activity","Add");

        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this,MenuVisitActivity.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MenuVisitActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}
