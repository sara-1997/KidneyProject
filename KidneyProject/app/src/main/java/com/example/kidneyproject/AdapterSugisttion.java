package com.example.kidneyproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class AdapterSugisttion extends RecyclerView.Adapter<AdapterSugisttion.AdapterNotholder>{
    private final String UserId;
    java.util.List<Suggistions> List;
    Context context ;
    String UserType;
    SharedPreferences prefs;

    public static final String MyPREFERENCES = "MyPrefs" ;
    private String PatientId;

    public AdapterSugisttion(List<Suggistions> listConsult, Context context, String userType) {
        this.List = listConsult;
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
    public AdapterSugisttion.AdapterNotholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification_list, parent, false);
        AdapterSugisttion.AdapterNotholder adapterHolder = new AdapterSugisttion.AdapterNotholder(v);
        return adapterHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterSugisttion.AdapterNotholder holder, final int position) {
        holder.title.setText(List.get(position).getTitle());
        holder.desText.setText("Description");
        holder.ntextnameme.setText("Title");

        holder.Message.setText(List.get(position).getDescriptipn());
        holder.Photo.setVisibility(View.GONE);
        holder.linearLayout.setVisibility(View.GONE);




              if(UserId.equals(List.get(position).getPatientId()))
         holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));

                builder.setTitle("Options");
                String[] options = {"View Profile","Delete" };
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Intent intent = new Intent(context,ProfileChatActivity.class);
                                SharedPreferences.Editor editor;
                                intent.putExtra("Activity", "Suggestions");

                                editor = prefs.edit();
                                editor.putString("ReciveId",List.get(position).getPatientId() );
                                editor.commit();

                                context.startActivity(intent);

                                break;
                            case 1:
                                DatabaseReference updateData = FirebaseDatabase.getInstance()
                                        .getReference("Suggistions")
                                        .child(List.get(position).getKey());
                                updateData.removeValue();
                                Toast.makeText(context,"Delete Suggestions  Successfully",Toast.LENGTH_LONG).show();
                                context.startActivity(new Intent(context,MainAdminActivity.class));

                                break;



                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        holder.chat.setVisibility(View.GONE);

        holder.chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("Activity", "Suggestions");

                SharedPreferences.Editor editor;

                editor = prefs.edit();
                editor.putString("ReciveId",List.get(position).getPatientId() );
                editor.commit();

                context.startActivity(intent);



            }
        });


    }


    @Override
    public int getItemCount() {
        return List.size();    }

    public class AdapterNotholder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView title , ntextnameme , desText;
        TextView Message , Name ;
        ImageView chat ,Photo;
        LinearLayout linearLayout;
        AdapterNotholder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            title = (TextView)itemView.findViewById(R.id.title);
            Message = (TextView)itemView.findViewById(R.id.message);
            Name=(TextView)itemView.findViewById(R.id.Name);
            ntextnameme=(TextView)itemView.findViewById(R.id.ntextnameme);
            desText=(TextView)itemView.findViewById(R.id.desText);
            linearLayout=(LinearLayout) itemView.findViewById(R.id.Nameline);

            chat=(ImageView)itemView.findViewById(R.id.chat);
            Photo=(ImageView)itemView.findViewById(R.id.photo);

        }
    } {
    }

}
