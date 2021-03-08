package com.example.kidneyproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kidneyproject.Class.Constants;
import com.example.kidneyproject.Class.Notification;
import com.example.kidneyproject.Class.Wish;
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

public class AdapterWish extends RecyclerView.Adapter<AdapterWish.AdapterWishHolder>{
    private final String VolunteerId;
    List<Wish> List;
    Context context ;
    String UserType;
    SharedPreferences prefs;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String PatientId;
    private DatabaseReference mDatabase;
    private String VolunteerName;

    public AdapterWish(List<Wish> listRequest, Context context, String userType) {
        this.List = listRequest;
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
    public AdapterWish.AdapterWishHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_wish_list, parent, false);
        AdapterWish.AdapterWishHolder adapterHolder = new AdapterWish.AdapterWishHolder(v);
        return adapterHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterWishHolder holder, final int position) {
        holder.title.setText(List.get(position).getWishTitle());
        holder.Place.setText(List.get(position).getPlace());

        holder.Description.setText(List.get(position).getDescription());
        holder.Statues.setText(List.get(position).getStatus());
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("Latitude",List.get(position).getLatitude());
                intent.putExtra("Longitude",List.get(position).getLongitude());


                context.startActivity(intent);

            }
        });

        if (UserType.equals("Volunteer")) {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MapsActivity.class);
                    intent.putExtra("NameActivity","List");
                    intent.putExtra("Longitude",List.get(position).getLongitude());
                    intent.putExtra("Latitude",List.get(position).getLatitude());
                    context.startActivity(intent);
                }
            });
            if(List.get(position).getStatus().equals("Wait"))
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                    builder.setTitle("Options");
                    String[] options = {"View Profile Patient" , "Accept Wish Request Patient" };
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent intent = new Intent(context, ProfileChatActivity.class);
                                    intent.putExtra("Activity","Wish");
                                    intent.putExtra("PatientId",List.get(position).getPatientId());
                                    context.startActivity(intent);

                                    break;
                                case 1:
                                    PatientId=List.get(position).getPatientId();

                                    AcceptWish(List.get(position).getKey(),"Accept",List.get(position).getVolunteerId());
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
            if(List.get(position).getStatus().equals("Accept"))
            {
                holder.photo.setImageResource(R.drawable.acceptwish);

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
                                        intent.putExtra("Activity","Wish");

                                        SharedPreferences.Editor editor;

                                        editor = prefs.edit();
                                        editor.putString("ReciveId",List.get(position).getPatientId() );
                                        editor.commit();
                                        context.startActivity(intent);

                                        break;
                                    case 1:
                                        intent = new Intent(context, ProfileChatActivity.class);
                                        intent.putExtra("Activity","Wish");

                                        editor = prefs.edit();
                                        editor.putString("ReciveId",List.get(position).getPatientId() );
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
            if(List.get(position).getStatus().equals("Accept"))
            {
                holder.photo.setImageResource(R.drawable.acceptwish);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                        builder.setTitle("Options");
                        String[] options = {"Go to Chat", "View Profile","Delete Wish"};
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:
                                        Intent intent = new Intent(context,ChatActivity.class);
                                        intent.putExtra("Activity","Wish");

                                        SharedPreferences.Editor editor;

                                        editor = prefs.edit();
                                        editor.putString("ReciveId",List.get(position).getVolunteerId() );
                                        editor.commit();
                                        context.startActivity(intent);

                                        break;
                                    case 1:
                                        intent = new Intent(context, ProfileChatActivity.class);
                                        intent.putExtra("Activity","Wish");

                                        editor = prefs.edit();
                                        editor.putString("ReciveId",List.get(position).getVolunteerId() );
                                        editor.commit();
                                        context.startActivity(intent);

                                        break;
                                    case 2:
                                        DatabaseReference updateData = FirebaseDatabase.getInstance()
                                                .getReference("RequestWish")
                                                .child(List.get(position).getKey());
                                        updateData.removeValue();
                                        Toast.makeText(context,"Delete Wish Successfully",Toast.LENGTH_LONG).show();
                                        context.startActivity(new Intent(context,ListWishForPatientActivity.class));


                                        break;
                                }
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
            }
            holder.imageView.setVisibility(View.GONE);
            if(List.get(position).getStatus().equals("Wait"))
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                    builder.setTitle("Options");
                    String[] options = {"Update Information Wish" , "Delete Wish"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    Intent intent = new Intent(context, AddWishActivity.class);
                                    intent.putExtra("Activity","List");
                                    intent.putExtra("Key",List.get(position).getKey());

                                    intent.putExtra("Title",List.get(position).getWishTitle());
                                    intent.putExtra("Place",List.get(position).getPlace());
                                    intent.putExtra("Description",List.get(position).getDescription());
                                    context.startActivity(intent);

                                    break;
                                case 1:
                                    DatabaseReference updateData = FirebaseDatabase.getInstance()
                                            .getReference("RequestWish")
                                            .child(List.get(position).getKey());
                                    updateData.removeValue();
                                    Toast.makeText(context,"Delete Wish Successfully",Toast.LENGTH_LONG).show();
                                    context.startActivity(new Intent(context,ListWishForPatientActivity.class));

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

    private void AcceptWish(String key, String accept, String volunteerId) {
        DatabaseReference updateData = FirebaseDatabase.getInstance()
                .getReference("RequestWish")
                .child(key);
        updateData.child("status").setValue(accept);

        updateData.child("volunteerId").setValue(VolunteerId);
         DatabaseReference reference;

        reference = FirebaseDatabase.getInstance().getReference("users");

        reference.child(PatientId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Notification notification = new Notification();
                mDatabase = FirebaseDatabase.getInstance().getReference("Notification");

                notification.setTypeNotification("Wish");
                notification.setMessageNotification("The Wish request from volunteer "+VolunteerName+" has been approved");
                notification.setTitleNotification("Approval of your Wish request");
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
        context.startActivity(new Intent(context,ListWishFortVolunteerActivity.class));

        //send Notification



    }

    @Override
    public int getItemCount() {
        return List.size();    }

    public class AdapterWishHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView Description , Place ,Statues;
        ImageView imageView ,photo;
        AdapterWishHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            Description = (TextView)itemView.findViewById(R.id.Description);
            Place=(TextView)itemView.findViewById(R.id.Place);
            Statues=(TextView)itemView.findViewById(R.id.Statues);
            imageView=(ImageView)itemView.findViewById(R.id.location);
            photo=(ImageView)itemView.findViewById(R.id.photo);



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
