package com.example.final_assignment_app1;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    SharedPreferences preferences;

    EditText app1UserEmail;
    EditText password;

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase database;
    DatabaseReference reference;

    TextView textViewa;
    TextView textView;

    float currentSize;

    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        changeTopBarAppearance();


        editBackground();
        changeTextSize();

        //for firebase connection - authentication
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        auth.signOut();

        changeTextSize();

        //create notification channel - important for android 8+

        String channelName = getString(R.string.channel_name);
        NotificationChannel channel = new NotificationChannel(
                    "location_channel",//notification channel is important for Android 8+ to group notifications
                    channelName,//name of the channel - important because the user can see it
                    NotificationManager.IMPORTANCE_HIGH //importance of the notification
        );

        NotificationManager notificationManager = getSystemService(NotificationManager.class); //create notification manager - appears notification on the screen - get the appropriate manager from device
        notificationManager.createNotificationChannel(channel); //create this channel to the device



    }

    //changes the color of the status bar to light - important for dark mode
    private void changeTopBarAppearance() {
        View decoration_View = getWindow().getDecorView();
        WindowInsetsController window_insets_Controller = decoration_View.getWindowInsetsController();
        try{
            window_insets_Controller.setSystemBarsAppearance(WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS, WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS);
        }catch (Exception ex){
            //Log.d("An exception occured.", "This is the following: " + ex);
        }
    }



    @Override
    protected void onResume(){
        super.onResume();
        editBackground();//edit the widgets according to new shared preferences
        changeTextSize();
    }

    public void log_in(View view) {

        app1UserEmail = findViewById(R.id.editTextText);
        password = findViewById(R.id.editTextTextPassword);

        if(!app1UserEmail.getText().toString().isEmpty() && !password.getText().toString().isEmpty()) {
            //authedication
            auth.signInWithEmailAndPassword(app1UserEmail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {//successful log in
                        user = auth.getCurrentUser();

                        // Write a message to the database
                        database = FirebaseDatabase.getInstance("...");
                        reference = database.getReference("App1Users");
                        Query query = reference.orderByChild("App1UserID").equalTo(user.getUid());
                        // Read from the database
                        query.addListenerForSingleValueEvent(new ValueEventListener() {//successful real time database connection
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//find this user from realtime database
                                        App1Users app1User = snapshot.getValue(App1Users.class);
                                        if (app1User != null) {//go to Activity4 with the important user's information
                                            Toast.makeText(MainActivity.this, getString(R.string.successful_log_in), Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(MainActivity.this, MainActivity4.class);
                                            intent.putExtra("user_Firstname", app1User.FirstName);
                                            intent.putExtra("user_Lastname", app1User.LastName);
                                            intent.putExtra("user_Email", app1User.email);
                                            startActivity(intent);
                                        }

                                    }
                                } else {//if something went wrong with the real time database
                                    Toast.makeText(MainActivity.this, getString(R.string.something_went_wrong_message), Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {//if something went wrong with real time database
                                Toast.makeText(MainActivity.this, getString(R.string.something_went_wrong_message), Toast.LENGTH_LONG).show();
                            }
                        });


                    } else {//if the authedication goes wrong
                        Toast.makeText(MainActivity.this, getString(R.string.wrong_email_or_password_message), Toast.LENGTH_LONG).show();

                    }
                }
            });
        }else Toast.makeText(MainActivity.this, getString(R.string.wrong_email_or_password_message), Toast.LENGTH_LONG).show();
    }

        //auth.signOut();}




    private void editBackground(){//change background color according to shared preference
        SharedPreferences preferences;
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        String colorString = preferences.getString("backgroundColor","#F0FFFF");
        int color = Color.parseColor(colorString);
        getWindow().getDecorView().setBackgroundColor(color);
    }
    public void go(View view){// go to sign up Activity
        Intent intent = new Intent(this, MainActivity2.class);
        startActivity(intent);
    }


    public void changeTextSize(){
        //change text size for each widget

        editEditText(findViewById(R.id.editTextText));


        editEditText(findViewById(R.id.editTextTextPassword));


        editButtons(findViewById(R.id.button));

        editButtons(findViewById(R.id.button2));



        editTextView(findViewById(R.id.textView));


        editTextView(findViewById(R.id.textView2));

    }


    //edit widgets' text size
    public void editButtons(Button button){
        //read shared preference and apply changes
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        //if textsize is not 1f then apply changes
        if (textSize != 1f) {
            currentSize = button.getTextSize() / button.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 20f));
            button.setTextSize(currentSize);
        }
    }

    //same but with different range of textsize
    public void editTextView(TextView textView){
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if (textSize != 1f) {
            currentSize = textView.getTextSize() / textView.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 35f));
            textView.setTextSize(currentSize);
        }
    }


    public void editEditText(EditText editText){
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if (textSize != 1f) {
            Log.d("TextSize", "TextSize: " + textSize);
            currentSize = editText.getTextSize() / editText.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 20f));
            editText.setTextSize(currentSize);
        }
    }

    public void go1(View view){// got to settings Activity
        Intent intent = new Intent(this, MainActivity3.class);
        startActivity(intent);
    }


}