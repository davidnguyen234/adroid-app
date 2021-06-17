package com.dlnsoft.adroidapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class TrafficCameraMap extends AppCompatActivity implements OnMapReadyCallback {

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private RequestQueue requestQueue;
    private GoogleMap googleMap;
    private ArrayList<CamItem> trafficCamArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_camera_map);
        requestQueue = Volley.newRequestQueue(this);
        trafficCamArrayList = new ArrayList<>();
        getLocationPermission();
    }

    private void showMarkers() {
        for (int i = 0; i < trafficCamArrayList.size(); i++) {
            LatLng latLng = new LatLng(trafficCamArrayList.get(i).getLatitude(), trafficCamArrayList.get(i).getLongitude());
            googleMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(trafficCamArrayList.get(i).getAddress()));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        getTrafficCamData();
        getDeviceLocation();
    }

    private void getTrafficCamData() {
        String URL = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        JSONArray features = response.getJSONArray("Features");
                        for (int i = 0; i < features.length(); i++) {
                            JSONObject featureItem = features.getJSONObject(i);
                            JSONArray pointCoordinates = featureItem.getJSONArray("PointCoordinate");
                            double latitude = pointCoordinates.getDouble(0);
                            double longitude = pointCoordinates.getDouble(1);
                            JSONArray cameras = featureItem.getJSONArray("Cameras");
                            for (int k = 0; k < cameras.length(); k++) {
                                JSONObject cameraItem = cameras.getJSONObject(k);
                                String address = cameraItem.getString("Description");
                                trafficCamArrayList.add(new CamItem(address, longitude, latitude));
                            }
                        }
                        showMarkers();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initMap();
        } else {
            requestPermissions(new String[]{FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initMap();
            } else {
                finish();
            }
        }
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.trafficCamMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(TrafficCameraMap.this);
    }

    private void getDeviceLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    googleMap.setMyLocationEnabled(true);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 12f));
                } else {
                    Log.d("ERROR", "Unable to find location");
                    Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (SecurityException e) {
            Log.e("ERROR", "Security Exception: " + e.getMessage());
        }
    }

}