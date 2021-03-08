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

import com.bumptech.glide.Glide;
import com.example.kidneyproject.Class.Chat;
import com.example.kidneyproject.Class.Constants;
import com.example.kidneyproject.Class.Notification;
import com.example.kidneyproject.Class.Wish;
import com.example.kidneyproject.Class.user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class AdapterMessage extends RecyclerView.Adapter<AdapterMessage.AdapterMessagehHolder>{
    List<Chat> List;
    public static final int leftside =  0;
    public static final int rightside = 1 ;
    String ImageUrl ;


    Context context ;
    String UserType;
    SharedPreferences prefs;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String UserId;
    FirebaseUser fu;

    public AdapterMessage(List<Chat> listChat, Context context, String userType , String imageUrl) {
        this.List = listChat;
        this.context=context;
        this.UserType=userType;
        ImageUrl=imageUrl;
        prefs = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        UserId=prefs.getString("userid","2" );

    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @NonNull
    @Override
    public AdapterMessage.AdapterMessagehHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==rightside) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatright_list
                    , parent, false);
            AdapterMessage.AdapterMessagehHolder adapterHolder = new AdapterMessage.AdapterMessagehHolder(v);
            return adapterHolder;
        }
        else
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chatleft_list
                    , parent, false);
            AdapterMessage.AdapterMessagehHolder adapterHolder = new AdapterMessage.AdapterMessagehHolder(v);
            return adapterHolder;
        }
    }



    @Override
    public void onBindViewHolder(@NonNull AdapterMessagehHolder holder, final int position) {

        //send Notification
         Chat chat= List.get(position);
         holder.message.setText(List.get(position).getMessage());
        if( ImageUrl.equals("Default") &&UserType.equals("Patient"))
            holder.imageView.setImageResource(R.drawable.patient);
        else  if( ImageUrl.equals("Default") &&UserType.equals("Volunteer"))
            holder.imageView.setImageResource(R.drawable.volunteer);
        else
            holder.message.setText(List.get(position).getMessage());
        Glide.with(context)
                .load(  ImageUrl)
                .into( holder.imageView);




    }

    @Override
    public int getItemCount() {
        return List.size();    }

    public class AdapterMessagehHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView message;

        ImageView imageView;
        AdapterMessagehHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            message = (TextView)itemView.findViewById(R.id.message);
            imageView=(ImageView)itemView.findViewById(R.id.photo);

        }
    }

    @Override
    public int getItemViewType(int position) {
        fu= FirebaseAuth.getInstance().getCurrentUser();
        if(List.get(position).getSendId().equals(fu.getUid()))
        {
            return  rightside;
        }
        else
            return leftside;


    }

    {
    }


}
