package com.example.kidneyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.kidneyproject.Class.Notification;
import com.example.kidneyproject.Class.visit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListConsultActivity extends AppCompatActivity {
    private DatabaseReference reference;
    SharedPreferences prefs;
    String UserId , UserType;

    public static final String MyPREFERENCES = "MyPrefs" ;

    List<Consult> List ;
    RecyclerView recyclerView ;
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_consult);
        button=(Button) findViewById(R.id.button);
        initialfirebase();
        if (UserType.equals("Volunteer"))
            button.setVisibility(View.GONE);
        getallconsult();


    }

    private void getallconsult() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            private String Desri;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Consult post = postSnapshot.getValue(Consult.class);
                    String Key = postSnapshot.getKey();

                    post.setKey(Key);

                    List.add(post);


                }
                AdapterConsult adapter = new AdapterConsult(List , ListConsultActivity.this ,UserType);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void back(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    private void initialfirebase() {

        reference = FirebaseDatabase.getInstance().getReference("Consult");
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        List=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);

        UserId= prefs.getString("userid","2" );
        UserType=prefs.getString("UserType","2" );





    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
    public void AddConsult(View view) {
        Intent intent = new Intent(this,AddconsultActivity.class);
        intent.putExtra("Activity","Add");

        startActivity(intent);
    }
}
