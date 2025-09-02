package com.example.final_assignment_app1;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.fonts.FontStyle;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity3 extends AppCompatActivity {

    EditText editTextColor;
    //RelativeLayout layout;
    SharedPreferences preferences;
    Button button;

    TextView textView;

    boolean min = true;

    boolean max = false;

    float currentSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main3);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        changeTopBarAppearance();


        //sets special handle for the back button to finish the activity and then return back
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Toast.makeText(MainActivity2.this, "The activity finished", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        editBackground();

        //read text size number from shared preferences now
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        changeTextSize(textSize);

        //read and show user's firstname and lastname
        textView = findViewById(R.id.textView25);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getIntent().getStringExtra("user_Firstname")).append(" ").append(getIntent().getStringExtra("user_Lastname"));
        textView.setText(stringBuilder.toString());
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



    //set background color if this button is clicked - save to shared preference
    public void setColorMoccasin(View view){

        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFE4B5"));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("backgroundColor","#FFE4B5");
        editor.apply();
    }

    //set background color if this button is clicked - save to shared preference
    public void setColorBeige(View view){

        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#F5F5DC"));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("backgroundColor","#F5F5DC");
        editor.apply();
    }
    //set background color if this button is clicked - save to shared preference
    public void setColorBlanchedAlmond(View view){

        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#FFEBCD"));
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("backgroundColor","#FFEBCD");
        editor.apply();
    }

    //set minimum text size if button is clicked
    public void setTextMinSize(View view){

        changeTextSize(0.5f);
        max = true;
        min = false;
        //float textSize = preferences.getFloat("TextSizeNumberFloat",1f);

        //textSize *= 0.5f;

        //decrease text size 50%
        SharedPreferences.Editor editor = preferences.edit();
        //editor.putBoolean("TextSizeChanged",true);
        editor.putFloat("TextSizeNumberFloat",0.5f);
        editor.apply();
        //Toast.makeText(this, "Invalid Color!", Toast.LENGTH_LONG).show();
    }

    //set maximum textsize if button is clicked
    public void setTextLargeSize(View view){

        changeTextSize(2f);
        min = true;
        max = false;

        //float textSize = preferences.getFloat("TextSizeNumberFloat",1f);

        //textSize *= 2f;

        //increase text size 2*
        SharedPreferences.Editor editor = preferences.edit();
        //editor.putBoolean("TextSizeChanged",true);
        editor.putFloat("TextSizeNumberFloat",2f);
        editor.apply();
        //Toast.makeText(this, "Invalid Color!", Toast.LENGTH_LONG).show();


    }


    public void changeTextSize(float number){
        //change text size for each widget

        editButtons(findViewById(R.id.button5),number);

        editButtons(findViewById(R.id.button6),number);

        editButtons(findViewById(R.id.button7),number);

        editButtons(findViewById(R.id.button15),number);

        editButtons(findViewById(R.id.button16),number);

        editButtons(findViewById(R.id.button18),number);

        editTextView(findViewById(R.id.textView10),number);

        editTextView(findViewById(R.id.textView23),number);

        editTextView(findViewById(R.id.textView24),number);

        editTextView(findViewById(R.id.textView25),number);

    }

    private void editBackground(){

        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        String colorString = preferences.getString("backgroundColor","#F0FFFF");
        int color = Color.parseColor(colorString);
        getWindow().getDecorView().setBackgroundColor(color);

    }

    public void editButtons(Button button, float textSize){

        currentSize = button.getTextSize()/ button.getResources().getDisplayMetrics().scaledDensity;
        //size must be between 10-20
        currentSize = Math.max(10f,Math.min(currentSize * textSize,20f));
        button.setTextSize(currentSize);
    }

    public void editTextView(TextView textView, float textSize){
        currentSize = textView.getTextSize()/ textView.getResources().getDisplayMetrics().scaledDensity;
        currentSize = Math.max(10f,Math.min(currentSize * textSize,24f));
        textView.setTextSize(currentSize);
    }

    public void go_back(View view){
        finish();
    }

}