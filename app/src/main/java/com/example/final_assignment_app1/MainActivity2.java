package com.example.final_assignment_app1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity2 extends AppCompatActivity {

    SharedPreferences preferences;

    TextView textView;
    EditText editText;
    Button button;

    float currentSize;


    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);
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
        changeTextSize();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
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
    protected void onResume(){//edit widgets according to new shared preferences
        super.onResume();
        editBackground();
        changeTextSize();
    }


    public void SignUp(View view){

        EditText firstNameText = findViewById(R.id.editTextText2);
        EditText lastNameText = findViewById(R.id.editTextText3);
        EditText emailText = findViewById(R.id.editTextText5);
        EditText passwordText = findViewById(R.id.editTextTextPassword2);

        //id no editText is empty
        if (!emailText.getText().toString().isEmpty()
            && !firstNameText.getText().toString().isEmpty()
            && !lastNameText.getText().toString().isEmpty()
            && !passwordText.getText().toString().isEmpty()){
            //add user-authedication
            auth.createUserWithEmailAndPassword(emailText.getText().toString(),passwordText.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>(){

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if(task.isSuccessful()){
                                user = auth.getCurrentUser();
                                String uid = user.getUid();
                                //add user in real-time database
                                addApp1UserToRDB(uid,firstNameText.getText().toString(),lastNameText.getText().toString(),emailText.getText().toString());
                                //
                                finish();
                            }
                            else{
                                Toast.makeText(MainActivity2.this, getString(R.string.something_went_wrong_message), Toast.LENGTH_LONG).show();

                            }


                        }

                    });


        }else{
            Toast.makeText(MainActivity2.this, getString(R.string.something_went_wrong_message), Toast.LENGTH_LONG).show();
        }


    }

    public void addApp1UserToRDB(String uid,String FirstName,String LastName,String email){
        database = FirebaseDatabase.getInstance("https://androidapp1-559ca-default-rtdb.europe-west1.firebasedatabase.app/");
        reference = database.getReference();
        //new register with hashmap
        //making a node for the database
        Map<String,Object> newUserDetails = new HashMap<>();
        newUserDetails.put("App1UserID",uid);
        newUserDetails.put("FirstName",FirstName);
        newUserDetails.put("LastName",LastName);
        newUserDetails.put("email",email);


        //add new node to the node with all users
        reference.child("App1Users").child(uid).setValue(newUserDetails).addOnCompleteListener(
                task -> {
                    if(task.isSuccessful()){
                        //
                    } else {
                        //
                    }
                }
        );

        //newUser.clear();
        //go to Activity4 with the important information
        Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
        intent.putExtra("user_Firstname",FirstName);
        intent.putExtra("user_Lastname",LastName);
        intent.putExtra("user_Email",email);
        startActivity(intent);
    }



    public void changeTextSize(){
        //change text size for each widget

        editEditText(findViewById(R.id.editTextText2));


        editEditText(findViewById(R.id.editTextText3));


        editEditText(findViewById(R.id.editTextText5));


        editEditText(findViewById(R.id.editTextTextPassword2));


        editButtons(findViewById(R.id.button3));



        editTextView(findViewById(R.id.textView3));


        editTextView(findViewById(R.id.textView5));


        editTextView(findViewById(R.id.textView6));


        /*
        textView = findViewById(R.id.textView7);
        currentSize = textView.getTextSize()/ textView.getResources().getDisplayMetrics().scaledDensity;
        currentSize = Math.max(10f,Math.min(currentSize * number,20f));
        textView.setTextSize(currentSize);*/


        editTextView(findViewById(R.id.textView8));



        editTextView(findViewById(R.id.textView9));


    }

    private void editBackground(){//change background color according to shared preference
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        String colorString = preferences.getString("backgroundColor","#F0FFFF");
        int color = Color.parseColor(colorString);
        getWindow().getDecorView().setBackgroundColor(color);

    }


    //edit widgets' text size (same with MainActivity but with different textsizes)
    public void editButtons(Button button){
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if(textSize != 1f) {
            currentSize = button.getTextSize() / button.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 25f));
            button.setTextSize(currentSize);
        }
    }

    public void editTextView(TextView textView){
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if (textSize != 1f) {
            currentSize = textView.getTextSize() / textView.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 26f));
            textView.setTextSize(currentSize);
        }
    }


    public void editEditText(EditText editText){
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if(textSize != 1f) {
            currentSize = editText.getTextSize() / editText.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 20f));
            editText.setTextSize(currentSize);
        }
    }

    public void go_to_settings(View view){
        Intent intent = new Intent(this, MainActivity3.class);
        String username = getIntent().getStringExtra("user_email");
        intent.putExtra("user_email",username);
        startActivity(intent);
    }

}