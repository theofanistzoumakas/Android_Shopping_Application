package com.example.final_assignment_app1;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShoppingBagActivity extends AppCompatActivity {

    String userFirstName;
    String userLastName;
    TextView textViewTitle;

    TextView textViewTotalPrice;

    StringBuilder stringBuilder;

    SharedPreferences preferences;

    FirebaseDatabase database;
    DatabaseReference reference;

    float currentSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_shopping_bag);
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

        //edit widgets according to the shared preference - settings
        editBackground();

        textViewTitle = findViewById(R.id.textView7);
        textViewTotalPrice = findViewById(R.id.textView19);

        changeTextSize();

        userFirstName = getIntent().getStringExtra("user_Firstname");
        userLastName = getIntent().getStringExtra("user_Lastname");

        //List<App1Products> shoppingBagList = ShoppingBagList.getInstance().getBag();

        //show the shopping list - the shopping list has two parts - the first one is to show the product titles - the second one is to show product codes
        String shoppingBagTitle = ShoppingBagList.getInstance().getBag().stream().map( product -> "--> " + product.getTitle() + " (" + product.getPrice()+"€)").collect(Collectors.joining(System.lineSeparator()));




        //count the total price and show the totla price
        Double totalPrice = ShoppingBagList.getInstance().getBag().stream().mapToDouble(App1Products::getPrice).sum();

        stringBuilder = new StringBuilder();

        stringBuilder.append(getString(R.string.showTotalPrice)).append(totalPrice).append(" €");

        //show text according to if the shopping bag is empty or not
        if(!shoppingBagTitle.isEmpty()) {
            textViewTitle.setText(shoppingBagTitle);
            textViewTotalPrice.setText(stringBuilder.toString());
        }else{
            textViewTitle.setText("");
            textViewTotalPrice.setText(getString(R.string.noProductText));
        }




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
        editBackground();//edit widgets according to the new shared preference - settings
        changeTextSize();
    }

    private void editBackground(){//edit widgets according to the shared preference - settings
        SharedPreferences preferences;
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        String colorString = preferences.getString("backgroundColor","#F0FFFF");
        int color = Color.parseColor(colorString);
        getWindow().getDecorView().setBackgroundColor(color);
    }

    public void changeTextSize(){
        //change text size

        editTextView(textViewTitle);
        editTextView(textViewTotalPrice);

        editButtons(findViewById(R.id.button13));
        editButtons(findViewById(R.id.button14));
        editButtons(findViewById(R.id.button19));


    }

    public void go_to_settings(View view){//go to settings activity
        Intent intent = new Intent(this, MainActivity3.class);
        intent.putExtra("user_Firstname",getIntent().getStringExtra("user_Firstname"));
        intent.putExtra("user_Lastname",getIntent().getStringExtra("user_Lastname"));
        startActivity(intent);
    }

    public void clearShoppingBag(View view){//clear shopping bag
        ShoppingBagList.getInstance().clearList();
        showMessageWithOk(getString(R.string.okText), getString(R.string.shoppingBagIsClearText));

    }

    public void shoppingBagPay(View view){//pay shopping bag - make orders
        //connect to real time database
        database = FirebaseDatabase.getInstance("...");
        reference = database.getReference("App1Orders");

        Map<String,Object> app1Order = new HashMap<>();//make hashmap to create nodes for the real time database

        if (!ShoppingBagList.getInstance().getBag().isEmpty()) {//if shopping bag list is not empty
            for (App1Products product : ShoppingBagList.getInstance().getBag()) {//for each product on shopping bag list
                app1Order.put("FirstName", userFirstName);//order information
                app1Order.put("LastName", userLastName);
                app1Order.put("App1ProductCode", product.App1ProductCode);
                Date timestamp = new Date();
                SimpleDateFormat timestamp_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String date_Time = timestamp_format.format(timestamp);
                app1Order.put("Timestamp", date_Time);

                //insert the nodes that they are the orders
                reference.push().setValue(app1Order).addOnCompleteListener(
                        task -> {
                            if (task.isSuccessful()) {
                                //
                            } else {
                                //
                            }
                        }
                );

                app1Order.clear();//clear the hashmap to create the next single order

            }

            //if it is all good show message and clear the shopping bag
            showMessageWithOk(getString(R.string.successful_shopping_bag_payment), getString(R.string.successfulShoppingBagPayment));
            ShoppingBagList.getInstance().clearList();
        }
        else{//if something went wrong with the inserts to real time database
            showMessageWithOk(getString(R.string.noProductText), getString(R.string.shoppingBagIsClearText));
        }
    }

    //edit widgets' text size (same with MainActivity but with different textsizes)
    public void editButtons(Button button){//edit activity's buttons according to shared preference
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if (textSize != 1f) {
            currentSize = button.getTextSize() / button.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 20f));
            button.setTextSize(currentSize);
        }
    }

    public void editTextView(TextView textView){//edit activity's textViews according to shared preference
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if (textSize != 1f) {
            currentSize = textView.getTextSize() / textView.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 23f));
            textView.setTextSize(currentSize);
        }
    }

    //create alertDialog
    public void showMessageWithOk(String title, String message){
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();//if user clicked ok finish the project
                    }
                })
                .show();


    }


}