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
import java.util.List;

public class ListVisitActivity extends AppCompatActivity {
    private DatabaseReference reference;
    SharedPreferences prefs;
    public static final String MyPREFERENCES = "MyPrefs" ;
    List<visit> ListRequest ;
    RecyclerView recyclerView ;
    private String UserType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lisit_visit);
        initialfirebase();
        getallVisit();





    }

    private void getallVisit() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            private String Desri;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
            visit post = postSnapshot.getValue(visit.class);
                    String Key = postSnapshot.getKey();

                    post.setKeyVisit(Key);

                        if(post.getStatues().equals("Wait"))
                    ListRequest.add(post);


                }
                AdapterVisit adapter = new AdapterVisit(ListRequest , ListVisitActivity.this ,UserType);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void initialfirebase() {

        reference = FirebaseDatabase.getInstance().getReference("RequestVisit");
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        UserType=prefs.getString("UserType","2" );
        ListRequest=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);




    }

    public void AddRequestVisit(View view) {
        Intent intent = new Intent(this,AddVisitRequestActivity.class);
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
