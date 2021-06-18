package com.dlnsoft.adroidapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class MainActivity extends AppCompatActivity {
    private EditText usernameInput;
    private EditText emailInput;
    private EditText passwordInput;
    SharedPreferences sharedPreferences;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameInput = findViewById(R.id.userName);
        emailInput = findViewById(R.id.email);
        passwordInput = findViewById(R.id.password);
        login = findViewById(R.id.login);
        sharedPreferences = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        usernameInput.setText(getData("userName"));
        emailInput.setText(getData("email"));
        passwordInput.setText(getData("password"));
    }

    private void signIn() {
        Log.d("FIREBASE", "signIn");

        // 1 - validate display name, email, and password entries
        String userName = usernameInput.getText().toString();
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        Log.d("Password", "Error");
        Log.d("UserName", userName);

        if (userName.isEmpty()) {
            usernameInput.setError("Enter username");
            usernameInput.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            emailInput.setError("Enter email");
            emailInput.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            passwordInput.setError("Enter password");
            passwordInput.requestFocus();
            return;
        }

        // 2 - save valid entries to shared preferences
        saveData("userName", userName);
        saveData("email", email);
        saveData("password", password);

        // 3 - sign into Firebase
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d("FIREBASE", "signIn:onComplete:" + task.isSuccessful());

                    if (task.isSuccessful()) {
                        // update profile. display name is the value entered in UI
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .build();

                        assert user != null;
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        Log.d("FIREBASE", "User profile updated.");
                                        // Go to FirebaseActivity
                                        startActivity(new Intent(MainActivity.this, Firebase.class));
                                    }
                                });

                    } else {
                        Log.d("FIREBASE", "sign-in failed");

                        Toast.makeText(MainActivity.this, "Sign In Failed",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public String getData(String key) {

        return sharedPreferences.getString(key, "");
    }

    public void saveData(String key, String message) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, message);
        editor.apply();
    }

    public void loadLocation(View view) {
        Intent intent = new Intent(this, TrafficCameraMap.class);
        startActivity(intent);
    }

    public void showFoodToast(View view) {
        Toast t = Toast.makeText(this, "Food", Toast.LENGTH_SHORT);
        t.show();
    }

    public void loadMovies(View view) {
        Intent intent = new Intent(this, Movies.class);
        startActivity(intent);
    }

    public void showMusicToast(View view) {
        Toast t = Toast.makeText(this, "Music", Toast.LENGTH_SHORT);
        t.show();
    }

    public void showParksToast(View view) {
        Toast t = Toast.makeText(this, "Parks", Toast.LENGTH_SHORT);
        t.show();
    }

    public void loadTraffic(View view) {
        Intent intent = new Intent(this, TrafficCameras.class);
        startActivity(intent);
    }

    public void login(View view) {
        signIn();
    }
}