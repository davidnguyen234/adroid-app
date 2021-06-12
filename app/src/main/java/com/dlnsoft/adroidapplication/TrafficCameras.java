package com.dlnsoft.adroidapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TrafficCameras extends AppCompatActivity {

    private ArrayList<Cameras> trafficCamArrayList;
    private RequestQueue requestQueue;
    private TrafficCamAdapter trafficCamAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_cameras);

        trafficCamArrayList = new ArrayList<>();
        trafficCamAdapter = new TrafficCamAdapter(TrafficCameras.this, trafficCamArrayList);
        requestQueue = Volley.newRequestQueue(this);

        RecyclerView recyclerView = findViewById(R.id.trafficCams);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(trafficCamAdapter);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            getTrafficCamData();
        } else {
            Toast toast = Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public void getTrafficCamData() {
        String URL = "https://web6.seattle.gov/Travelers/api/Map/Data?zoomId=13&type=2";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {
                    try {
                        JSONArray features = response.getJSONArray("Features");
                        for (int i = 0; i < features.length(); i++) {
                            JSONObject featureItem = features.getJSONObject(i);
                            JSONArray cameras = featureItem.getJSONArray("Cameras");
                            for (int k = 0; k < cameras.length(); k++) {
                                JSONObject cameraItem = cameras.getJSONObject(k);
                                String address = cameraItem.getString("Description");
                                String imageUrl = cameraItem.getString("ImageUrl");
                                String type = cameraItem.getString("Type");
                                trafficCamArrayList.add(new Cameras(type, address, imageUrl));
                            }
                        }
                        trafficCamAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, Throwable::printStackTrace);
        requestQueue.add(request);
    }


    public static class Cameras {
        private final String trafficCamAddress;
        private final String trafficCamImageUrl;

        public Cameras(String type, String address, String url) {
            trafficCamAddress = address;
            trafficCamImageUrl = checkType(type, url);
        }

        public String checkType(String type, String url) {
            if (type.equals("sdot")) {
                return "https://www.seattle.gov/trafficcams/images/" + url;
            } else {
                return "https://images.wsdot.wa.gov/nw/" + url;
            }
        }


        public String getAddress() {

            return trafficCamAddress;
        }

        public String getUrl() {

            return trafficCamImageUrl;
        }
    }

    public static class TrafficCamAdapter extends RecyclerView.Adapter<TrafficCamAdapter.ViewHolder> {
        private final Context context;
        private final ArrayList<Cameras> camList;

        public TrafficCamAdapter(Context context, ArrayList<Cameras> cameras) {
            this.context = context;
            this.camList = cameras;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.traffic_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Cameras cameras = camList.get(position);
            String URL = cameras.getUrl();
            String address = cameras.getAddress();
            holder.trafficCamAddress.setText(address);
            Picasso.get().load(URL).fit().centerInside().into(holder.trafficCamImage);
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView trafficCamImage;
            public TextView trafficCamAddress;

            public ViewHolder(View itemView) {
                super(itemView);
                trafficCamImage = itemView.findViewById(R.id.trafficCamImage);
                trafficCamAddress = itemView.findViewById(R.id.trafficCamAddress);
            }
        }

        @Override
        public int getItemCount() {
            return camList.size();
        }
    }

}

