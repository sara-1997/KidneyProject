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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdapterNotification extends RecyclerView.Adapter<AdapterNotification.AdapterNotholder>{
    private final String UserId;
    java.util.List<Notification> List;
    Context context ;
    String UserType;
    SharedPreferences prefs;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private String PatientId;

    public AdapterNotification(List<Notification> listNotification, Context context, String userType) {
        this.List = listNotification;
        this.context=context;
        this.UserType=userType;
        prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        UserId=prefs.getString("userid","2" );
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @NonNull
    @Override
    public AdapterNotification.AdapterNotholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_list, parent, false);
        AdapterNotification.AdapterNotholder adapterHolder = new AdapterNotification.AdapterNotholder(v);
        return adapterHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterNotification.AdapterNotholder holder, final int position) {
        holder.title.setText(List.get(position).getTitleNotification());
        holder.Name.setText(List.get(position).getNameReceiver());

        holder.Message.setText(List.get(position).getMessageNotification());
        if (List.get(position).getTypeNotification().equals("Visit"))
            holder.Photo.setImageResource(R.drawable.accept);
            else
                if (List.get(position).getTypeNotification().equals("Wish"))
                    holder.Photo.setImageResource(R.drawable.acceptwish);

                else
        if (List.get(position).getTypeNotification().equals("Chat"))
            holder.Photo.setImageResource(R.drawable.chat);
           else
        if (List.get(position).getTypeNotification().equals("Talent"))
            holder.Photo.setImageResource(R.drawable.heartlicke);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                builder.setTitle("Options");
                String[] options = {"View Profile" , "Delete Notification" };
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(context, ProfileChatActivity.class);
                                intent.putExtra("Activity", "Notification");
                                SharedPreferences.Editor editor;

                                editor = prefs.edit();
                                editor.putString("ReciveId", List.get(position).getSenderNotification());
                                editor.commit();
                                context.startActivity(intent);

                                break;
                            case 1:
                                DatabaseReference updateData = FirebaseDatabase.getInstance()
                                        .getReference("Notification")
                                        .child(List.get(position).getKey());
                                updateData.removeValue();
                                Toast.makeText(context,"Delete Notification  Successfully",Toast.LENGTH_LONG).show();
                                context.startActivity(new Intent(context,ListNotificationActivity.class));

                                break;



                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("Activity", "Notification");

                SharedPreferences.Editor editor;

                editor = prefs.edit();
                editor.putString("ReciveId",List.get(position).getSenderNotification() );
                editor.commit();

                intent.putExtra("ReciveId",List.get(position).getSenderNotification());

                context.startActivity(intent);



            }
        });


    }


    @Override
    public int getItemCount() {
        return List.size();    }

    public class AdapterNotholder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title;
        TextView Message , Name ;
        ImageView chat ,Photo;
        AdapterNotholder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            Message = (TextView)itemView.findViewById(R.id.message);
            Name=(TextView)itemView.findViewById(R.id.Name);
            chat=(ImageView)itemView.findViewById(R.id.chat);
            Photo=(ImageView)itemView.findViewById(R.id.photo);

        }
    } {
    }

}
