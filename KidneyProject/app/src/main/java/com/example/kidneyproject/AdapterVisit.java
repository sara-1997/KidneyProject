package com.example.kidneyproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidneyproject.Class.Constants;
import com.example.kidneyproject.Class.Notification;
import com.example.kidneyproject.Class.user;
import com.example.kidneyproject.Class.visit;
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

public class AdapterVisit extends RecyclerView.Adapter<AdapterVisit.AdapterVisitHolder>{

    private final String VolunteerId;
    List<visit> Listvisit;
    Context context ;
    String UserType;
    private DatabaseReference mDatabase;

    SharedPreferences prefs;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String PatientId;
    private String VolunteerName;

    public AdapterVisit(List<visit> listRequest, Context context, String userType) {
        this.Listvisit = listRequest;
        this.context=context;
        this.UserType=userType;
        prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        VolunteerId=prefs.getString("userid","2" );
        VolunteerName=prefs.getString("UserName","2" );
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @NonNull
    @Override
    public AdapterVisitHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visit_list, parent, false);
        AdapterVisitHolder adapterVisitHolder = new AdapterVisitHolder(v);
        return adapterVisitHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterVisitHolder holder, final int position) {
        holder.Name.setText(Listvisit.get(position).getPlace());
        holder.Time.setText(Listvisit.get(position).getHour() + ":" + Listvisit.get(position).getMinut());

        holder.Date.setText(Listvisit.get(position).getYear() + "-" + Listvisit.get(position).getMonth() + "-" +
              Listvisit.get(position).getDay());
        holder.Statues.setText(Listvisit.get(position).getStatues());


        if (UserType.equals("Volunteer")) {

            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("NameActivity","List");
                    intent.putExtra("Longitude",Listvisit.get(position).getLongitude());
                    intent.putExtra("Latitude",Listvisit.get(position).getLatitude());
                   context.startActivity(intent);
                }
            });
            if(Listvisit.get(position).getStatues().equals("Wait")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                        builder.setTitle("Options");
                        String[] options = {"View Profile Patient", "Accept Visit Request Patient"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(context, ProfileChatActivity.class);
                                        intent.putExtra("Activity", "ManageVisit");
                                        SharedPreferences.Editor editor;

                                        editor = prefs.edit();
                                        editor.putString("ReciveId", Listvisit.get(position).getPatientId());
                                        editor.commit();
                                        context.startActivity(intent);

                                        break;
                                    case 1:
                                        PatientId = Listvisit.get(position).getPatientId();

                                        AcceptVisit(Listvisit.get(position).getKeyVisit(), "Accept", Listvisit.get(position).getVolunteerId());
                                        break;
                                    case 2:
                                        break;


                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
            if(Listvisit.get(position).getStatues().equals("Accept"))
            {
                holder.Photo.setImageResource(R.drawable.accept);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                        builder.setTitle("Options");
                        String[] options = {"Go to Chat", "View Profile"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(context,ChatActivity.class);
                                        intent.putExtra("Activity","ManageVisit");

                                        SharedPreferences.Editor editor;

                                        editor = prefs.edit();
                                        editor.putString("ReciveId",Listvisit.get(position).getPatientId() );
                                        editor.commit();
                                        context.startActivity(intent);

                                        break;
                                    case 1:
                                         intent = new Intent(context, ProfileChatActivity.class);
                                        intent.putExtra("Activity","ManageVisit");
                                        editor = prefs.edit();
                                        editor.putString("ReciveId",Listvisit.get(position).getPatientId() );

                                        editor.commit();
                                        context.startActivity(intent);

                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
        }
        else
        {

            if(Listvisit.get(position).getStatues().equals("Accept"))
        {
            holder.Photo.setImageResource(R.drawable.accept);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                    builder.setTitle("Options");
                    String[] options = {"Go to Chat", "View Profile"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent intent = new Intent(context,ChatActivity.class);
                                    intent.putExtra("Activity","ManageVisit");

                                    SharedPreferences.Editor editor;

                                    editor = prefs.edit();
                                    editor.putString("ReciveId",Listvisit.get(position).getVolunteerId() );
                                    editor.commit();
                                    context.startActivity(intent);

                                    break;
                                case 1:
                                    intent = new Intent(context, ProfileChatActivity.class);
                                    intent.putExtra("Activity","ManageVisit");

                                    editor = prefs.edit();
                                    editor.putString("ReciveId",Listvisit.get(position).getVolunteerId() );
                                    editor.commit();
                                    context.startActivity(intent);

                                    break;
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
            if(Listvisit.get(position).getStatues().equals("Wait"))
            {
                holder.linearLayout.setVisibility(View.GONE);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                        builder.setTitle("Options");
                        String[] options = {"Update Information Visit" , "Delete Visit"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(context, AddVisitRequestActivity.class);
                                        intent.putExtra("Activity","List");
                                        intent.putExtra("Key",Listvisit.get(position).getKeyVisit());

                                        intent.putExtra("VisitDate",Listvisit.get(position).getYear() + "-" + Listvisit.get(position).getMonth() + "-" +
                                                Listvisit.get(position).getDay());
                                        intent.putExtra("VisitTime",Listvisit.get(position).getHour() + ":" + Listvisit.get(position).getMinut());
                                        intent.putExtra("HospitalName",Listvisit.get(position).getPlace());
                                        intent.putExtra("Description",Listvisit.get(position).getDescription());
                                        context.startActivity(intent);

                                        break;
                                    case 1:
                                        DatabaseReference updateData = FirebaseDatabase.getInstance()
                                                .getReference("RequestVisit")
                                                .child(Listvisit.get(position).getKeyVisit());
                                        updateData.removeValue();
                                        Toast.makeText(context,"Delete Request  Successfully",Toast.LENGTH_LONG).show();
                                        context.startActivity(new Intent(context,ListVisitPatientActivity.class));

                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }


        }
    }

    private void AcceptVisit(String key, String accept, String volunteerId) {
        DatabaseReference updateData = FirebaseDatabase.getInstance()
                .getReference("RequestVisit")
                .child(key);
        updateData.child("statues").setValue(accept);

        updateData.child("volunteerId").setValue(VolunteerId);
        DatabaseReference reference;

        reference = FirebaseDatabase.getInstance().getReference("users");

        reference.child(PatientId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                Notification notification = new Notification();
                mDatabase = FirebaseDatabase.getInstance().getReference("Notification");

                notification.setTypeNotification("Visit");
                notification.setMessageNotification("The visit request from volunteer "+VolunteerName+" has been approved");
                notification.setTitleNotification("Approval of your visit request");
                notification.setSenderNotification(VolunteerId);
                notification.setReceiverNotification(PatientId);
                notification.setNameReceiver(VolunteerName);

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

                Toast.makeText(context, "Send Notification Successfully", Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        context.startActivity(new Intent(context,ListVisitActivity.class));
        //send Notification
        //send Notification




    }

    @Override
    public int getItemCount() {
        return Listvisit.size();    }

    public class AdapterVisitHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView Name;
        TextView Date , Time ,Statues ;
        ImageView imageView , Photo;
        LinearLayout linearLayout;

        AdapterVisitHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            Name = (TextView)itemView.findViewById(R.id.name);
            Date = (TextView)itemView.findViewById(R.id.location);
            Time=(TextView)itemView.findViewById(R.id.time);
            Statues=(TextView)itemView.findViewById(R.id.Statues);
            linearLayout=(LinearLayout) itemView.findViewById(R.id.direction) ;
            imageView=(ImageView)itemView.findViewById(R.id.location2);
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
