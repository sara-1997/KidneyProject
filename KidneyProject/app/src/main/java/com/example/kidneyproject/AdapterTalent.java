package com.example.kidneyproject;

import android.content.Context;
import android.content.Intent;
import android.content.LocusId;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kidneyproject.Class.Constants;
import com.example.kidneyproject.Class.Notification;
import com.example.kidneyproject.Class.talent;
import com.example.kidneyproject.Class.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdapterTalent  extends RecyclerView.Adapter<AdapterTalent.Adaptertalentholder>{
    private final String UserId;
    java.util.List<talent> List;
    Context context ;
    String UserType;
    SharedPreferences prefs;
    private DatabaseReference mDatabase;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private String PatientId ,VolunteerName;

    public AdapterTalent(java.util.List<talent> listtalent, Context context, String userType) {
        this.List = listtalent;
        this.context=context;
        this.UserType=userType;
        prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        UserId=prefs.getString("userid","2" );
        VolunteerName=prefs.getString("UserName","2" );

    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @NonNull
    @Override
    public AdapterTalent.Adaptertalentholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_talent_list, parent, false);
        AdapterTalent.Adaptertalentholder adapterHolder = new AdapterTalent.Adaptertalentholder(v);
        return adapterHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull final AdapterTalent.Adaptertalentholder holder, final int position) {

        holder.title.setText(List.get(position).getTitle());

        holder.numberlike.setText(String.valueOf(List.get(position).getNumberlike()));

 if (List.get(position).getNumberlike()>0 &&UserType.equals("Patient"))
 {
     holder.ImageLike.setImageResource(R.drawable.heartlicke);

 }
        Glide.with(context)
                .load(   List.get(position).getImagePatient())
                .into( holder.Photo);
        Glide.with(context)
                .load(   List.get(position).getImageTalent())
                .into( holder.ImageTalent);

        holder.ImageLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!List.get(position).getVolunteerId().equals(UserId) )
                {

                        DatabaseReference updateData;
                        holder.ImageLike.setImageResource(R.drawable.heartlicke);
                        holder.numberlike.setText(String.valueOf(List.get(position).getNumberlike()+1));

                        updateData = FirebaseDatabase.getInstance()
                                .getReference("Talent")
                                .child(List.get(position).getKey());
                        updateData.child("numberlike").setValue(List.get(position).getNumberlike()+1);
                        updateData.child("likemy").setValue("Yes");
                        updateData.child("volunteerId").setValue(UserId);




                        if(!UserId.equals(List.get(position).getPatientId())) {
                            DatabaseReference reference;

                            reference = FirebaseDatabase.getInstance().getReference("users");

                            reference.child(List.get(position).getPatientId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Notification notification = new Notification();
                                    mDatabase = FirebaseDatabase.getInstance().getReference("Notification");

                                    notification.setTypeNotification("Talent");
                                    notification.setMessageNotification(VolunteerName + "likes Talent");
                                    notification.setTitleNotification("Like Talent");
                                    notification.setNameReceiver(VolunteerName);

                                    notification.setSenderNotification(UserId);
                                    notification.setReceiverNotification(List.get(position).getPatientId());
                                    String addNotification = mDatabase.push().getKey();
                                    notification.setKey(mDatabase.push().getKey());
                                    mDatabase.child(addNotification).setValue(notification);
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("title", notification.getTitleNotification());
                                        jsonObject.put("body", notification.getMessageNotification());
                                        jsonObject.put("typeNotification", notification.getTypeNotification());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    sendNotification(dataSnapshot.getValue(user.class).getToken(), jsonObject);

                                    Toast.makeText(context, "Send Notification Successfully", Toast.LENGTH_LONG).show();


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //send Notification
                        }
                    }
                else
                {
                    DatabaseReference updateData;

                    updateData = FirebaseDatabase.getInstance()
                            .getReference("Talent")
                            .child(List.get(position).getKey())
                    ;
                    updateData.child("numberlike").setValue(List.get(position).getNumberlike()-1);
                    holder.numberlike.setText(String.valueOf(List.get(position).getNumberlike()-1));
                    holder.ImageLike.setImageResource(R.drawable.heartnotlike);

                    updateData.child("likemy").setValue("No");
                    updateData.child("volunteerId").setValue("0");

                }

                if (!List.get(position).getPatientId().equals(UserId) )
                {
                    if(!List.get(position).getVolunteerId().equals(UserId) )
                    {

                        DatabaseReference updateData;
                        holder.ImageLike.setImageResource(R.drawable.heartlicke);
                        holder.numberlike.setText(String.valueOf(List.get(position).getNumberlike()+1));

                        updateData = FirebaseDatabase.getInstance()
                                .getReference("Talent")
                                .child(List.get(position).getKey());
                        updateData.child("numberlike").setValue(List.get(position).getNumberlike()+1);
                        updateData.child("likemy").setValue("Yes");
                        updateData.child("volunteerId").setValue(UserId);




                        if(!UserId.equals(List.get(position).getPatientId())) {
                            DatabaseReference reference;

                            reference = FirebaseDatabase.getInstance().getReference("users");

                            reference.child(List.get(position).getPatientId()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    Notification notification = new Notification();
                                    mDatabase = FirebaseDatabase.getInstance().getReference("Notification");

                                    notification.setTypeNotification("Talent");
                                    notification.setMessageNotification(VolunteerName + "likes Talent");
                                    notification.setTitleNotification("Like Talent");
                                    notification.setSenderNotification(UserId);
                                    notification.setReceiverNotification(List.get(position).getPatientId());
                                    String addNotification = mDatabase.push().getKey();
                                    notification.setNameReceiver(VolunteerName);

                                    notification.setKey(mDatabase.push().getKey());
                                    mDatabase.child(addNotification).setValue(notification);
                                    JSONObject jsonObject = new JSONObject();
                                    try {
                                        jsonObject.put("title", notification.getTitleNotification());
                                        jsonObject.put("body", notification.getMessageNotification());
                                        jsonObject.put("typeNotification", notification.getTypeNotification());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                    sendNotification(dataSnapshot.getValue(user.class).getToken(), jsonObject);

                                    Toast.makeText(context, "Send Notification Successfully", Toast.LENGTH_LONG).show();


                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            //send Notification
                        }
                    }
                    else
                    {
                        holder.ImageLike.setImageResource(R.drawable.heartnotlike);
                        DatabaseReference updateData;

                        updateData = FirebaseDatabase.getInstance()
                                .getReference("Talent")
                                .child(List.get(position).getKey())
                        ;
                        updateData.child("numberlike").setValue(List.get(position).getNumberlike()-1);
                        holder.numberlike.setText(String.valueOf(List.get(position).getNumberlike()));

                        updateData.child("likemy").setValue("No");
                        updateData.child("volunteerId").setValue("0");

                    }
                }





            }


        });



    }


    @Override
    public int getItemCount() {
        return List.size();    }

    public class Adaptertalentholder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title,numberlike;
        ImageView ImageLike ,Photo ,ImageTalent;
        Adaptertalentholder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            numberlike = (TextView)itemView.findViewById(R.id.numberlike);
            ImageLike=(ImageView)itemView.findViewById(R.id.Like);
            ImageTalent=(ImageView)itemView.findViewById(R.id.ImageTalent);

            Photo=(ImageView)itemView.findViewById(R.id.photo);

        }
    } {
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


