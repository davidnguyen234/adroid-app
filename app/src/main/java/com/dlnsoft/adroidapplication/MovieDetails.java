package com.dlnsoft.adroidapplication;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        TextView movieTitle = findViewById(R.id.movieTitle);
        TextView movieYear = findViewById(R.id.movieYear);
        TextView movieDirector = findViewById(R.id.movieDirector);
        TextView movieDescription = findViewById(R.id.movieDescription);
        ImageView movieImage = findViewById(R.id.movieImage);



        Bundle bundle = this.getIntent().getExtras();
        String[] movieDetails = bundle != null ? bundle.getStringArray("com.dlnsoft.adroidapplication.MESSAGE") : null;

        if (movieDetails != null) {
            movieTitle.setText(movieDetails[0]);
            movieYear.setText(String.format(" | %s", movieDetails[1]));
            movieDirector.setText(movieDetails[2]);
            movieDescription.setText(movieDetails[4]);
            Picasso.get().load(movieDetails[3]).into(movieImage);
        }
    }

}