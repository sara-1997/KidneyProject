package com.example.kidneyproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListSuggestionsActivity extends AppCompatActivity {
    private DatabaseReference reference;
    SharedPreferences prefs;
    String UserId , UserType;

    public static final String MyPREFERENCES = "MyPrefs" ;

    List<Suggistions> List ;
    RecyclerView recyclerView ;
    Button button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_suggestions);
        button=(Button) findViewById(R.id.button);
        initialfirebase();
        if (UserType.equals("Volunteer"))
            button.setVisibility(View.GONE);
        getallconsult();


    }

    private void getallconsult() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Suggistions post = postSnapshot.getValue(Suggistions.class);
                    String Key = postSnapshot.getKey();

                    post.setKey(Key);

                    List.add(post);


                }
                AdapterSugisttion adapter = new AdapterSugisttion(List , ListSuggestionsActivity.this ,UserType);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void back(View view) {
        Intent intent = new Intent(this,MainAdminActivity.class);
        startActivity(intent);
    }
    private void initialfirebase() {

        reference = FirebaseDatabase.getInstance().getReference("Suggistions");
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
        Intent intent = new Intent(this,MainAdminActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

}
