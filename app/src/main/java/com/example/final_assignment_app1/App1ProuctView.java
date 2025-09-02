package com.example.final_assignment_app1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowInsetsController;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.text.HtmlCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

public class App1ProuctView extends AppCompatActivity {

    String title;
    String description;
    String productImage;
    String store;
    String code;
    String date;
    String price;

    float currentSize;

    SharedPreferences preferences;

    StringBuilder stringBuilder;


    TextView titleView;
    TextView descriptionView;
    TextView storeView;
    TextView codeView;
    TextView dateView;
    TextView priceView;

    RequestQueue requestQueue;
    ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_app1_prouct_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            changeTopBarAppearance();
        }

        editBackground();

        //sets special handle for the back button to finish the activity and then return back
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                //Toast.makeText(MainActivity2.this, "The activity finished", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        //get product information from intent
        title = getIntent().getStringExtra("productTitle");
        description = getIntent().getStringExtra("productDescription");
        productImage = getIntent().getStringExtra("productImage");
        store = getIntent().getStringExtra("productStore");
        code = getIntent().getStringExtra("productCode");
        date = getIntent().getStringExtra("productDate");
        price = getIntent().getStringExtra("productPrice");


        TextView titleView = findViewById(R.id.textView29);
        TextView descriptionView = findViewById(R.id.textView13);
        imageView = findViewById(R.id.imageView);
        TextView storeView = findViewById(R.id.textView14);
        TextView codeView = findViewById(R.id.textView15);
        TextView dateView = findViewById(R.id.textView16);
        TextView priceView = findViewById(R.id.textView17);
        //load the product photo
        requestQueue = Volley.newRequestQueue(this);

        ImageRequest imageRequest = new ImageRequest(productImage, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, null, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }


        );
        requestQueue.add(imageRequest);





        //show the product's information to widgets - textViews - image
        stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append("> ").append(getString(R.string.TitleText)).append("</b>").append(" ").append(title).append("<br>");
        titleView.setText(HtmlCompat.fromHtml(stringBuilder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append("> ").append(getString(R.string.DescriptionText)).append("</b>").append(" ").append(description).append("<br>");
        descriptionView.setText(HtmlCompat.fromHtml(stringBuilder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append("> ").append(getString(R.string.StoreText)).append("</b>").append(" ").append(store).append("<br>");
        storeView.setText(HtmlCompat.fromHtml(stringBuilder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append("> ").append(getString(R.string.ProductCodeText)).append("</b>").append(" ").append(code).append("<br>");
        codeView.setText(HtmlCompat.fromHtml(stringBuilder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append("> ").append(getString(R.string.ReleaseDateText)).append("</b>").append(" ").append(date).append("<br>");
        dateView.setText(HtmlCompat.fromHtml(stringBuilder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));

        stringBuilder = new StringBuilder();
        stringBuilder.append("<b>").append("> ").append(getString(R.string.PriceText)).append("</b>").append(" ").append(price).append(" €");
        priceView.setText(HtmlCompat.fromHtml(stringBuilder.toString(), HtmlCompat.FROM_HTML_MODE_LEGACY));



        //edit widgets according to shared preferences
        changeTextSize();


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


    public void addToList(View view){//add to shopping bag the selected product
        if(title!=null && description!=null && productImage!=null && store!=null && code!=null && date!=null && price!=null) {
            ShoppingBagList.getInstance().addProductToList(new App1Products(Integer.parseInt(code),description,productImage,
                    Double.parseDouble(price),Integer.parseInt(date),store,title));
            Toast.makeText(App1ProuctView.this, getString(R.string.add_to_shopping_bag_button), Toast.LENGTH_LONG).show();
        }
    }


    public void go_to_settings(View view){//go to settings' Activity
        Intent intent = new Intent(this, MainActivity3.class);
        intent.putExtra("user_Firstname",getIntent().getStringExtra("user_Firstname"));
        intent.putExtra("user_Lastname",getIntent().getStringExtra("user_Lastname"));
        startActivity(intent);
    }

    private void editBackground(){//edit background according to the shared preferences
        SharedPreferences preferences;
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        String colorString = preferences.getString("backgroundColor","#F0FFFF");
        int color = Color.parseColor(colorString);
        getWindow().getDecorView().setBackgroundColor(color);
    }


    public void changeTextSize(){
        //change text size for each widget
        editTextView(findViewById(R.id.textView29));
        editTextView(findViewById(R.id.textView13));
        editTextView(findViewById(R.id.textView14));
        editTextView(findViewById(R.id.textView15));
        editTextView(findViewById(R.id.textView16));
        editTextView(findViewById(R.id.textView17));
        editButtons(findViewById(R.id.button10));
        editButtons(findViewById(R.id.button12));

    }
    //edit widgets' text size (same with MainActivity but with different textsizes)
    public void editButtons(Button button){//edit buttons according to the shared preferences
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if(textSize != 1f) {
            currentSize = button.getTextSize() / button.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 20f));
            button.setTextSize(currentSize);
        }
    }

    private void editTextView(TextView textView){//edit textView according to the shared preferences
        SharedPreferences preferences;
        preferences = getSharedPreferences("MySharedPreferences",MODE_PRIVATE);
        float textSize = preferences.getFloat("TextSizeNumberFloat",1f);
        if(textSize != 1f) {
            currentSize = textView.getTextSize() / textView.getResources().getDisplayMetrics().scaledDensity;
            currentSize = Math.max(10f, Math.min(currentSize * textSize, 21f));
            textView.setTextSize(currentSize);
        }


    }


    public void showImage(View view){

    }

}