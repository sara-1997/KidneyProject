package com.example.kidneyproject;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private Marker myMarker;
    private GoogleMap mMap;
    double latitude;
    double longitude;
    private int PROXIMITY_RADIUS = 100000;
    LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    String placeName = null;
    int i ;
    List<String> placeNameList = new ArrayList<>();

ArrayList<LatLng> LangList;
    SharedPreferences prefs;
    String UserType;
    public static final String MyPREFERENCES = "MyPrefs" ;
    private String userid;
    ArrayList Markers = new ArrayList();
    PolylineOptions polylineOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent=getIntent();

        prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        UserType=prefs.getString("UserType","Patient1");
        userid=prefs.getString("userid","2" );

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        GivePermission();

        getLocation();


    }

    public static boolean isLocationEnabled(Context context) {
        //...............
        return true;
    }

    protected void getLocation() {
        if (isLocationEnabled(this)) {
            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            criteria = new Criteria();
            bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

            //You can still do this if you like, you might get lucky:
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    Activity#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for Activity#requestPermissions for more details.
                    return;
                }
            }
            Location location = locationManager.getLastKnownLocation(bestProvider);
            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();

            }
            else{
                //This is what you need:

                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, 0,locationListenerCurrent );


            }
        }
        else
        {

        }
        //prompt user to enable location....
        //.................

    }

    private void GivePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
        }
    }


    private void AddnearbyHospitsl(String googlePlacesUrl) {

        StringRequest request = new StringRequest(Request.Method.GET, googlePlacesUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response != null) {

                    String lat = null;
                    String lng = null;
                    JSONObject googlePlaceJson= null;
                    LangList=new ArrayList<>();
                    try {
                        googlePlaceJson=new JSONObject(response);

                        for (i=0 ; i<googlePlaceJson.getJSONArray("results").length();i++)
                        {

                            placeName=googlePlaceJson.getJSONArray("results").getJSONObject(i).getString("name");
                            lat =googlePlaceJson.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lat");
                            lng =googlePlaceJson.getJSONArray("results").getJSONObject(i).getJSONObject("geometry").getJSONObject("location").getString("lng");
                            LatLng currentloca = new LatLng(Double.valueOf(lat), Double.valueOf(lng));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentloca, (float) 6.0));

                            placeNameList.add(placeName);
                            LangList.add(currentloca);


                            myMarker= mMap.addMarker(new MarkerOptions().position(currentloca).title(placeName).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                            ;

                            mMap.setOnMarkerClickListener(listener);
                            myMarker.setTag(i);





                        }




                    } catch (JSONException e) {

                        e.printStackTrace();
                       // Toast.makeText(getApplicationContext(), "e"+e, Toast.LENGTH_LONG).show();

                    }

                }
                else {
                   // Toast.makeText(getApplicationContext(),"response"+null,Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                volleyError.printStackTrace();
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet.1..Please check your connection!";
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet.2..Please check your connection!";
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet3...Please check your connection!";
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(getApplicationContext(), volleyError+"error", Toast.LENGTH_LONG).show();

            }

        }){

            /**
             * Passing some request headers
             * */

        };



        RequestQueue rQueue = Volley.newRequestQueue(getApplicationContext());
        rQueue.getCache().clear();

        rQueue.add(request);

            }
    LocationListener locationListenerCurrent=new LocationListener() {


        @Override
        public void onLocationChanged(Location location) {
            latitude=location.getLatitude();
            longitude=location.getLongitude();
            String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
            final LatLng currentloca = new LatLng(latitude, longitude);


            String type = "hospital";
            final StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
            googlePlacesUrl.append("location=" + latitude + "," + longitude);
            googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
            googlePlacesUrl.append("&types=" + type);
            googlePlacesUrl.append("&sensor=true");

            googlePlacesUrl.append("&key=" +"AIzaSyDaaeIQiBL52WB9MXu0qi4UvBE5iqUFqH4");
            Intent intent= getIntent();
            if (intent.getStringExtra("NameActivity").equals("MainActivity") && UserType.equals("Patient"))
            {
                if(!((Activity) MapsActivity.this).isFinishing())
                {
                    //show dialog
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle("Update Hospital Location ")
                            .setMessage("Are You Sure")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mMap.addMarker(new MarkerOptions().position(currentloca).title("Marker in current location"));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentloca, (float) 18.0));
                                    AddnearbyHospitsl(googlePlacesUrl.toString());

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                                    startActivityForResult(intent, 1);

                                }
                            })
                            .show();
                }

            }
            else if(intent.getStringExtra("NameActivity").equals("MainActivity") && UserType.equals("Volunteer"))
            {
                mMap.addMarker(new MarkerOptions().position(currentloca).title("Volunteer Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentloca, (float) 18.0));
                DatabaseReference updateData = FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(userid);
                updateData.child("latitude").setValue(currentloca.latitude);
                updateData.child("longitude").setValue(currentloca.longitude);
                Toast.makeText(getApplicationContext(),"Add Volunteer Location Successfully",Toast.LENGTH_LONG).show();


            }else
            if(intent.getStringExtra("NameActivity").equals("List") && UserType.equals("Volunteer"))
            {
                mMap.addMarker(new MarkerOptions().position(currentloca).title("Volunteer Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentloca, (float) 12));
                LatLng PostionHospital= new LatLng( intent.getDoubleExtra("Latitude",0)
                        ,intent.getDoubleExtra("Longitude",0));
                polylineOptions = new PolylineOptions();
                polylineOptions.color(Color.GREEN);
                polylineOptions.width(5);
                Markers.add(currentloca);
                Markers.add(PostionHospital);
                mMap.addMarker(new MarkerOptions().position(PostionHospital)
                        .title("Hospital Marker ")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PostionHospital, (float)12));
                polylineOptions.addAll(Markers);
                mMap.addPolyline(polylineOptions);

            }
            if(intent.getStringExtra("NameActivity").equals("Register") ) {
                mMap.addMarker(new MarkerOptions().position(currentloca).title("Patient Marker"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentloca, (float) 18.0));
                AddnearbyHospitsl(googlePlacesUrl.toString());
            }


        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



    }


    GoogleMap.OnMarkerClickListener  listener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {

                //handle click here
                int position = (int)(marker.getTag());

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("HospitalName",placeNameList.get(position) );
                intent.putExtra("latitude",LangList.get(position).latitude);
            intent.putExtra("longitude",LangList.get(position).longitude);

            setResult(RESULT_OK, intent);
                finish();


            return false;
        }
    };

}
