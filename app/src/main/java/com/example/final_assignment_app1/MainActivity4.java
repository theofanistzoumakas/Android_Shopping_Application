package com.example.final_assignment_app1;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
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
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity4 extends AppCompatActivity implements LocationListener {

    TextView welcome_string;
    float currentSize;

    SharedPreferences preferences;

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference reference;

    ArrayList<App1Products> app1Products;

    LocationManager locationManager;



    private static final int REQUEST_NOTIFICATION_PERMISSION = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main4);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        changeTopBarAppearance();


        auth = FirebaseAuth.getInstance();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        //sets special handle for the back button to finish the activity and then return back
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Toast.makeText(MainActivity2.this, "The activity finished", Toast.LENGTH_LONG).show();
                auth.signOut();
                locationManager.removeUpdates(MainActivity4.this);
                finish();
            }
        });
        welcome_string = findViewById(R.id.textView11);
        String user_email = getIntent().getStringExtra("user_email");

        //notification permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.POST_NOTIFICATIONS},REQUEST_NOTIFICATION_PERMISSION);
        }

        //edit the widgets according to shared preferences
        editBackground();
        changeTextSize();

        //get the first name for the welcome text
        String firstName = getIntent().getStringExtra("user_Firstname");

        welcome_string.setText(getString(R.string.welcome_string)+" "+ firstName);


        database = FirebaseDatabase.getInstance("...");
        reference = database.getReference("App1Products");
        Query query = reference.orderByChild("App1ProductCode");
        reference.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        //long userCount = snapshot.getChildrenCount();

                        app1Products = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){//initialize data snapshot to product class object
                            App1Products product = dataSnapshot.getValue(App1Products.class);
                            //Toast.makeText(MainActivity4.this, product.getClass().getSimpleName().toString(), Toast.LENGTH_LONG).show();
                            app1Products.add(product);//ad the object to the list of the available products
                        }

                        if (!app1Products.isEmpty()){
                            makeButtons(app1Products);//make buttons if the available product list is not empty
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainActivity4.this, "Something went wrong!", Toast.LENGTH_LONG).show();//if something went wrong
                    }
                }

        );


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
    protected void onResume(){//set the current settings according to new shared preferences
        super.onResume();
        editBackground();
        changeTextSize();
    }

    public void go_to_settings(View view){
        Intent intent = new Intent(this, MainActivity3.class);
        String username = getIntent().getStringExtra("user_email");
        intent.putExtra("user_Firstname",getIntent().getStringExtra("user_Firstname"));
        intent.putExtra("user_Lastname",getIntent().getStringExtra("user_Lastname"));
        intent.putExtra("user_email",username);
        startActivity(intent);
    }

    private void editBackground(){
        SharedPreferences preferences;
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        String colorString = preferences.getString("backgroundColor","#F0FFFF");
        int color = Color.parseColor(colorString);
        getWindow().getDecorView().setBackgroundColor(color);


    }
    //edit textViews' text size (same with MainActivity but with different textsizes)
    private void editTextView(TextView textView){
        SharedPreferences preferences;
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if (textSize!=1f) {
            currentSize = textView.getTextSize() / textView.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 25f));
            textView.setTextSize(currentSize);
        }

    }

    private void editSwitch(Switch switch1){
        SharedPreferences preferences;
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if (textSize!=1f) {
            currentSize = switch1.getTextSize() / switch1.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 20f));
            switch1.setTextSize(currentSize);
        }


    }



    //create buttons dynamically
    public void makeButtons(ArrayList<App1Products> app1Products){

        Button mybutton;

        //set the linearlayout to show the product's buttons
        LinearLayout linearLayout =findViewById(R.id.linearLayout);

        for (int i=0;i<app1Products.size();i++){
            mybutton = new Button(this);

            App1Products selectedProduct = app1Products.get(i);
            mybutton.setText(selectedProduct.App1ProductTitle);
            mybutton.setTextColor(Color.WHITE);
            mybutton.setBackground(ContextCompat.getDrawable(this, R.drawable.buttons_format));
            mybutton.setId(View.generateViewId());

            //parameters to show the button
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(0,16,0,0);

            //set the buttons' parameters to the layout
            mybutton.setLayoutParams(params);

            String myButtonText = mybutton.getText().toString();



            //if button is pressed then show the product (method)
            mybutton.setOnClickListener(
                    v -> {
                        showProduct(selectedProduct);
                    }
            );

            //editButtons(mybutton);

            linearLayout.addView(mybutton);

        }

    }


    public void showProduct(App1Products selectedProduct){//show product if a button is pressed

        String code = Integer.toString(selectedProduct.App1ProductCode);
        String description = selectedProduct.App1ProductDescription;
        String productImage = selectedProduct.App1ProductImage;
        String price = Double.toString(selectedProduct.App1ProductPrice);
        String date = Integer.toString(selectedProduct.App1ProductReleaseDate);
        String store = selectedProduct.App1ProductStore;
        String title = selectedProduct.App1ProductTitle;

        //go to activity to see product's information - product's information will be passed to the new activity
        Intent intent = new Intent(MainActivity4.this, App1ProuctView.class);
        if(code!=null && description!=null && productImage!=null && price!=null && date!=null && store!=null && title!=null){
            intent.putExtra("productCode",code);
            intent.putExtra("productDescription",description);
            intent.putExtra("productImage",productImage);
            intent.putExtra("productPrice",price);
            intent.putExtra("productDate",date);
            intent.putExtra("productStore",store);
            intent.putExtra("productTitle",title);
            intent.putExtra("user_Firstname",getIntent().getStringExtra("user_Firstname"));
            intent.putExtra("user_Lastname",getIntent().getStringExtra("user_Lastname"));
        }
        startActivity(intent);
    }



    public void changeTextSize(){
        //change text size for each widget
        editTextView(findViewById(R.id.textView11));
        editTextView(findViewById(R.id.textView20));
        editButtons(findViewById(R.id.button9));
        editButtons(findViewById(R.id.button11));
        editSwitch(findViewById(R.id.switch1));
    }
    //edit buttons' text size (same with MainActivity but with different textsizes)
    public void editButtons(Button button){//edit button according to the textsize on shred preference
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if (textSize!=1f) {
            currentSize = button.getTextSize() / button.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 20f));
            button.setTextSize(currentSize);
        }
    }


    public void showLocation(View view){//location permission
        Switch switch1 = (Switch) view;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},123);
            switch1.setChecked(false);
            return;
        }

        if (switch1.isChecked()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            switch1.setTrackTintList(ContextCompat.getColorStateList(this, R.color.green));
        } else {
            locationManager.removeUpdates(this);
            switch1.setTrackTintList(ContextCompat.getColorStateList(this, R.color.red));
        }

    }

    @Override
    public void onLocationChanged(@NonNull Location location){

        // Custom store location coordinates
        //double customStoreLatitude = 38.128329;
        //double customStoreLongitude = 23.790065;


        double latitudeA = location.getLatitude();//get current devices's location
        double longitudeA = location.getLongitude();


        boolean found = false;
        int notification_counter = 0;

        for (App1Products products : app1Products){//for each available product

            //the location from firebase is in text -> "latitude,longitude"
            String[] storeLocation = products.App1ProductStore.split("[,]");//split this text
            double latitudeB = Double.parseDouble(storeLocation[0]);
            double longitudeB = Double.parseDouble(storeLocation[1]);
            float[] results = new float[1];
            Location.distanceBetween(latitudeA,longitudeA,latitudeB, longitudeB,results);//get the distance
            float distance = results[0];//the distance
            if (distance<=200){//if it is near 200m create notification

                //make the indent if notification appears - the user will tap the notification and they will go to the product view activity
                Intent intent = new Intent(MainActivity4.this, App1ProuctView.class);
                intent.putExtra("productCode",Integer.toString(products.App1ProductCode));
                intent.putExtra("productDescription",products.App1ProductDescription);
                intent.putExtra("productImage",products.App1ProductImage);
                intent.putExtra("productPrice",Double.toString(products.App1ProductPrice));
                intent.putExtra("productDate",Integer.toString(products.App1ProductReleaseDate));
                intent.putExtra("productStore",products.App1ProductStore);
                intent.putExtra("productTitle",products.App1ProductTitle);
                intent.putExtra("user_Firstname",getIntent().getStringExtra("user_Firstname"));
                intent.putExtra("user_Lastname",getIntent().getStringExtra("user_Lastname"));
                //create pending intent - pending intent allows to go to the new activity from notification - flag update current is to update the current pending intent - flag immutable is important for Android 12+
                //context = activity that creates the pending intent, requestCode = unique code for pending intent,intent for the pending intent, flags = flags if there is a previous pending intent
                int requestCode = products.App1ProductCode;
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity4.this,requestCode,intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);


                Notification notification = new NotificationCompat.Builder(MainActivity4.this,"location_channel").setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("📦 " +getString(R.string.location_nearTitle))
                .setContentText(getString(R.string.location_nearText) + " " + products.App1ProductTitle)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent).setAutoCancel(true).build();//set autocancel is cancel the notification if it is tapped
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity4.this);//create a copy from notification manager compat - appears notification on the screen - get the appropriate manager from device

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)!= PackageManager.PERMISSION_GRANTED) {
                    return; //guarantee notification permission
                }

                notification_counter ++;
                notificationManager.notify(notification_counter,notification); //notify user
                found = true;
            }
        }
        if (found){
            locationManager.removeUpdates(this);
            Switch switch1 = findViewById(R.id.switch1);
            switch1.setChecked(false);
            switch1.setTrackTintList(ContextCompat.getColorStateList(this, R.color.red));

        }
    }

    public void go_to_shopping_bag(View view){//go to shopping bag activity - firstname and last name are important for orders
        Intent intent = new Intent(MainActivity4.this, ShoppingBagActivity.class);
        intent.putExtra("user_Firstname",getIntent().getStringExtra("user_Firstname"));
        intent.putExtra("user_Lastname",getIntent().getStringExtra("user_Lastname"));
        startActivity(intent);
    }


}