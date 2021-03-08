package com.example.kidneyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kidneyproject.Class.Wish;
import com.example.kidneyproject.Class.visit;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListWishForPatientActivity extends AppCompatActivity {
    private DatabaseReference reference;
    String UserId , UserType;
    public static final String MyPREFERENCES = "MyPrefs" ;

    List<Wish> ListWish ;
    RecyclerView recyclerView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wish_for_patient);
        initialfirebase();
        getallWish();

    }

    private void getallWish() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Wish post = null;
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String Key = postSnapshot.getKey();

                    post = postSnapshot.getValue(Wish.class);

                    post.setKey(Key);



                    if(post.getPatientId().equals(UserId))
                    {

                        ListWish.add(post);
                    }


                }
               AdapterWish adapter = new AdapterWish(ListWish , ListWishForPatientActivity.this,UserType);

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initialfirebase() {
        reference = FirebaseDatabase.getInstance().getReference("RequestWish");
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        ListWish=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);

        UserId= prefs.getString("userid","2" );
        UserType=prefs.getString("UserType","2" );
    }

    public void back(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void AddRequestwish(View view) {
        Intent intent = new Intent(this,AddWishActivity.class);
        intent.putExtra("Activity","Add");

        startActivity(intent);

    }
}
