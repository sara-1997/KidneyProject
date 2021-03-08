package com.example.kidneyproject;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.kidneyproject.Class.Constants;
import com.example.kidneyproject.Class.user;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.example.kidneyproject.Class.Constants.CHANNEL_ID;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {
    public static final String MyPREFERENCES = "MyPrefs" ;
    String userid;
    DatabaseReference reference;
    SharedPreferences prefs;
    String UserType;
    private Toolbar toolbar;
    CardView card ;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         reference = FirebaseDatabase.getInstance().getReference("users");
         prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
         card=( CardView) findViewById(R.id.ListConsult);

        GetTypeUser();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.inflateMenu(R.menu.main_menu);

            toolbar.setOnMenuItemClickListener(this);

        }

        // Get the layouts to use in the custom notification


    }

    public void GetTypeUser()
    {
        userid=prefs.getString("userid","2" );

        reference.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 UserType = dataSnapshot.getValue(user.class).getTypeuser();

               editor = prefs.edit();
                editor.putString("UserType",UserType.trim() );

                editor.putString("UserName",dataSnapshot.getValue(user.class).getName());

                editor.putLong("Longitude", Double.doubleToRawLongBits(dataSnapshot.getValue(user.class).getLongitude()));

                editor.putLong("Latitude", Double.doubleToRawLongBits(dataSnapshot.getValue(user.class).getLatitude()));
                editor.putString("place",dataSnapshot.getValue(user.class).getPlace() );

                editor.commit();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void VisitManage(View view) {

        try {
            TimeUnit.SECONDS.sleep(2);
            if (UserType.equals("Volunteer"))
            {
                Intent intent = new Intent(getApplicationContext(), MenuVisitActivity.class);

                startActivity(intent);

            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), MenuVisitActivity.class);

                startActivity(intent);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }



    }
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Log out")
                .setMessage("Would you like to logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        SharedPreferences settings = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                        settings.edit().clear().commit();
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
             return true;

    }


    public void ListWish(View view) {
        try {
            TimeUnit.SECONDS.sleep(2);
            if (UserType.equals("Volunteer"))
            {
                Intent intent = new Intent(getApplicationContext(), ListWishFortVolunteerActivity.class);

                startActivity(intent);

            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), ListWishForPatientActivity.class);

                startActivity(intent);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_profile:
            {
                Intent intent = new Intent(this,ProfileUserActivity.class);
                userid=prefs.getString("userid","2" );
                intent.putExtra("UserId",userid);

                intent.putExtra("Activity","main");
                startActivity(intent);
            }
            return true;
            case R.id.action_location:
            {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("NameActivity","MainActivity");
                startActivityForResult(intent ,1);

            }
            return true;
            case R.id.action_logout:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Log out")
                        .setMessage("Would you like to logout?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                SharedPreferences settings = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                                settings.edit().clear().commit();
                                try {
                                    FirebaseInstanceId.getInstance().deleteInstanceId();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1: {
                if (Activity.RESULT_OK == resultCode) {
                    //data.getStringExtra("HospitalName"));
                   // double latitude = data.getDoubleExtra("latitude",0.0);
                   // double longitude= data.getDoubleExtra("longitude",0.0);

                    DatabaseReference updateData = FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(userid);
                    updateData.child("place").setValue(data.getStringExtra("HospitalName"));
                    updateData.child("latitude").setValue(data.getDoubleExtra("latitude",0.0));
                    updateData.child("longitude").setValue(data.getDoubleExtra("longitude",0.0));
                    editor = prefs.edit();

                    editor.putLong("Longitude", Double.doubleToRawLongBits(data.getDoubleExtra("longitude",
                            0.0)));

                    editor.putLong("Latitude", Double.doubleToRawLongBits(data.getDoubleExtra("longitude",
                            0.0)));
                    editor.putString("place",data.getStringExtra("HospitalName") );

                    Toast.makeText(getApplicationContext(), "Update Location", Toast.LENGTH_LONG).show();


                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void ListNotification(View view) {
        Intent intent = new Intent(getApplicationContext(), ListNotificationActivity.class);

        startActivity(intent);

    }

    public void ListTalent(View view) {
        try {
            TimeUnit.SECONDS.sleep(2);
            if (UserType.equals("Volunteer"))
            {
                Intent intent = new Intent(getApplicationContext(), ListTalentActivityForVolunteer.class);

                startActivity(intent);

            }
            else
            {
                Intent intent = new Intent(getApplicationContext(), ListTalentActivityForPatient.class);

                startActivity(intent);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void AddSugisstion(View view) {
        Intent intent = new Intent(getApplicationContext(), AddSuggestionsActivity.class);

        startActivity(intent);
    }

    public void ListConsult(View view) {
        Intent intent = new Intent(getApplicationContext(), ListConsultActivity.class);

        startActivity(intent);
    }
}
