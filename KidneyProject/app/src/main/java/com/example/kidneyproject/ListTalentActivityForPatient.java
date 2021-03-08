package com.example.kidneyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.kidneyproject.Class.Notification;
import com.example.kidneyproject.Class.talent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListTalentActivityForPatient extends AppCompatActivity {
    private DatabaseReference reference;
    SharedPreferences prefs;

    public static final String MyPREFERENCES = "MyPrefs" ;

    List<talent> ListTalent;
    RecyclerView recyclerView ;
    private String UserType;
    private String Userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listtalent_for_patient);
        initialfirebase();
        getallTalent();
    }

    private void getallTalent() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            private String Desri;

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    talent post = postSnapshot.getValue(talent.class);
                    String Key = postSnapshot.getKey();

                    post.setKey(Key);

                        ListTalent.add(post);


                }
                AdapterTalent adapter = new AdapterTalent(ListTalent , ListTalentActivityForPatient.this ,UserType);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void AddTalent(View view) {
        Intent intent = new Intent(this,AddTalentActivity.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    private void initialfirebase() {

        reference = FirebaseDatabase.getInstance().getReference("Talent");
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        UserType=prefs.getString("UserType","2" );
        Userid=prefs.getString("userid","2" );
        ListTalent=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);




    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
