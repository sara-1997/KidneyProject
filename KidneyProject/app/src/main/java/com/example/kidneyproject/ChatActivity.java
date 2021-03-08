package com.example.kidneyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kidneyproject.Class.Chat;
import com.example.kidneyproject.Class.Constants;
import com.example.kidneyproject.Class.Notification;
import com.example.kidneyproject.Class.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
String ReciveId;
    DatabaseReference reference;
    TextView textView;
    ImageView imageView;
    EditText textSend;
    Chat chat;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private DatabaseReference mDatabase;
    SharedPreferences prefs;
    List<Chat> Listchat;
    RecyclerView recyclerView ;
    private String UserType;
    private String Userid;
    String ImageUrl;
    private String Token;
    private String UserName;
    String NameActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Intent intent= getIntent();
        NameActivity=intent.getStringExtra("Activity");
        textView=(TextView)findViewById(R.id.title) ;
        imageView= (ImageView) findViewById(R.id.photo) ;
        textSend=(EditText) findViewById(R.id.textsend);
        DatabaseReference data = FirebaseDatabase.getInstance().getReference("users");

        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        ReciveId=prefs.getString("ReciveId","2" );

         data.child(ReciveId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                textView.setText(dataSnapshot.getValue(user.class).getName());
                Token=dataSnapshot.getValue(user.class).getToken();

                ImageUrl=dataSnapshot.getValue(user.class).getImageUrl();

                if( dataSnapshot.getValue(user.class).getImageUrl().equals("Default") &&UserType.equals("Volunteer"))
                    imageView.setImageResource(R.drawable.volunteer);
                    else  if( dataSnapshot.getValue(user.class).getImageUrl().equals("Default") &&UserType.equals("Patient"))
                    imageView.setImageResource(R.drawable.patient);
                    else
                    Glide.with(getApplicationContext())
                            .load(   dataSnapshot.getValue(user.class).getImageUrl())
                            .into(imageView);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        initialfirebase();

        getChat();
    }

    private void getChat() {
        reference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Chat post = postSnapshot.getValue(Chat.class);
                    String Key = postSnapshot.getKey();

                   if(post.getReciverId().equals(Userid) && post.getSendId().equals(ReciveId)
                   || post.getReciverId().equals(ReciveId) && post.getSendId().equals(Userid)
                   ) {
                       Listchat.add(post);


                   }

                }
                AdapterMessage adapter = new AdapterMessage(Listchat , ChatActivity.this ,UserType,ImageUrl);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void initialfirebase() {
        reference = FirebaseDatabase.getInstance().getReference("Chat");
        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        UserType=prefs.getString("UserType","2" );
        Userid=prefs.getString("userid","2" );
        UserName=prefs.getString("UserName","2" );

        Listchat=new ArrayList<>();
        recyclerView=(RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(llm);
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

    public void Send(View view) {
        if (CheckValueText()==false)
        {
            return;
        }
        else
        {
            chat= new Chat ();

            mDatabase = FirebaseDatabase.getInstance().getReference("Chat");
            SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
            chat.setSendId( prefs.getString("userid","2" ));

            chat.setMessage(textSend.getText().toString());

            chat.setReciverId(ReciveId);

            String Request = mDatabase.push().getKey();
            mDatabase.child(Request).setValue(chat);

            getChat();

            reference = FirebaseDatabase.getInstance().getReference("users");

            reference.child(ReciveId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    Notification notification = new Notification();
                    mDatabase = FirebaseDatabase.getInstance().getReference("Notification");

                    notification.setTypeNotification("Chat");
                    notification.setMessageNotification(chat.getMessage());
                    notification.setTitleNotification(UserName);
                    notification.setNameReceiver(UserName);

                    notification.setSenderNotification(Userid);
                    notification.setReceiverNotification(ReciveId);
                    String addNotification = mDatabase.push().getKey();
                    notification.setKey(mDatabase.push().getKey());
                    mDatabase.child(addNotification).setValue(notification);
                    JSONObject jsonObject=new JSONObject();
                    try {
                        jsonObject.put("title",notification.getTitleNotification());
                        jsonObject.put("body",notification.getMessageNotification());
                        jsonObject.put("typeNotification",notification.getTypeNotification());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    sendNotification(dataSnapshot.getValue(user.class).getToken(),jsonObject);

                    Toast.makeText(getApplicationContext(), "Send Notification Successfully", Toast.LENGTH_LONG).show();


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



        }
    }

    private boolean CheckValueText() {
        boolean check = true;

        if (CheckEmpty(textSend)) {
            textSend.setError("Empty Message!");
            check = false;
        }
        return check;
    }

    boolean CheckEmpty(TextView text) {
        CharSequence String = text.getText().toString();
        return TextUtils.isEmpty(String);
    }
    private void sendNotification(final String token, final JSONObject data) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject notification = new JSONObject();
                    notification.put("title", data.getString("title"));
                    notification.put("body", data.getString("body"));

                    JSONObject mainObject = new JSONObject();
                    mainObject.put("to", token);
                    mainObject.put("data", data);
                    mainObject.put("notification", notification);

                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", "key=" + Constants.Key);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    Log.e("sent", mainObject.toString());
                    DataOutputStream dStream = new DataOutputStream(connection.getOutputStream());
                    dStream.writeBytes(mainObject.toString());
                    dStream.flush();
                    dStream.close();

                    String line;
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder responseOutput = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null) {
                        responseOutput.append(line);
                    }
                    bufferedReader.close();
                    Log.e("output", responseOutput.toString());
                } catch (Exception e) {
                    Log.e("output", e.toString());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

}
