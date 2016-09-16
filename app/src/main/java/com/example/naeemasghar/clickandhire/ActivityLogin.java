package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity {

    private static final String TAG = ActivityLogin.class.getSimpleName();
    private Button btnLogin;
    private Button btnLinkToRegister;
    private EditText inputPhone;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;
    //private GetAllAtLogin getCity;
    private String JSON_STRING;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_login);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputPassword = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Session manager
        session = new SessionManager(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Login button Click Event
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String phone = inputPhone.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();

                // Check for empty data in the form
                if (!phone.isEmpty() && !password.isEmpty()) {
                    // login user
                    checkLogin(phone, password);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        ActivityRegister.class);
                startActivity(i);
                finish();
            }
        });

    }

    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin(final String phone, final String password){
        class CheckUserLogin extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityLogin.this,"Verifying...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getUser(s);
                saveCities(s);
                saveCat(s);
                saveSubCat(s);
                //Toast.makeText(ActivityLogin.this,s,Toast.LENGTH_LONG).show();
                Log.d(TAG, "Login JSON response="+s);
                //Toast.makeText(ActivityLogin.this,"You are Login Successfully",Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("phone",phone);
                hashMap.put("password",password);

                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_LOGIN,hashMap);
                //String path= "http://api.androidhive.info/volley/person_object.json";
                //String s = rh.sendGetRequest(AppConfig.URL_GET_ALL_USERS);

                return s;
            }
        }

        CheckUserLogin cul = new CheckUserLogin();
        cul.execute();
    }
    /**
     * function to Get Data from JSON and store it to DB
     * */
    private void getUser(String json){
        try {

            JSONObject jObj = new JSONObject(json);
            //boolean error = jObj.getBoolean("error");

            JSONArray jObj1 = jObj.getJSONArray("userLogin");
            JSONObject jObj2 = jObj1.getJSONObject(0);
            boolean error = jObj2.getBoolean("error");

            // Check for error node in json
            if (!error) {
                // user successfully logged in, Create login session
                session.setLogin(true);
                // Now store the user in SQLite
                JSONObject user = jObj2.getJSONObject("user");
                    //JSONObject user = userArray.getJSONObject(0);
                    String uid = user.getString("uid");
                    String name = user.getString("name");
                    String phone = user.getString("phone");
                    String email = user.getString("email");
                    String tagLine = user.getString("tagLine");
                    String fees = user.getString("fees");
                    String description = user.getString("description");
                    String experience = user.getString("experience");
                    String speciality = user.getString("speciality");
                    String city = user.getString("city");
                    String profileImage = user.getString("profileImage");
                    String apiKey = user.getString("apiKey");
                    String avgRating = user.getString("avg_rating");
                    String totalRatings = user.getString("ratings");
                    String status = user.getString("status");
                    String created_at = user.getString("created_at");
                    if (avgRating.equals(null)) {
                        avgRating = "0.0";
                    }
                    if (totalRatings.equals(null)) {
                        totalRatings = "0";
                    }

                    // Inserting row in users table
                    db.deleteUsers();
                    db.addUser(uid, name, phone, email, tagLine, "BS SE", fees, description, experience, speciality, city,
                            "1", "test", profileImage, apiKey, avgRating, totalRatings, status, created_at);
                    DownloadImage downloadImage = new DownloadImage();
                    downloadImage.execute(uid);


                // Launch main activity
                Intent intent = new Intent(ActivityLogin.this,
                        MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void saveCities(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            //Toast.makeText(getApplicationContext(),json, Toast.LENGTH_LONG).show()
            Log.d(TAG, jObj.toString());

            JSONArray jObj1 = jObj.getJSONArray("userLogin");
            JSONObject boolObject = jObj1.getJSONObject(0);
            boolean error = boolObject.getBoolean("error");
            JSONObject cityListObject = jObj1.getJSONObject(1);
            //JSONObject user = cityListObject.getJSONObject("user");
            // Check for error node in json
            if (!error) {
                db.deleteCity();
                Log.d(TAG, "In Cities List No Error ");
                ModelCity obj = new ModelCity();
                JSONArray result = cityListObject.getJSONArray("cityList");
                for(int i = 0; i<result.length(); i++){

                    JSONObject jo = result.getJSONObject(i);
                    String id = jo.getString("id");
                    String name = jo.getString("city");
                    obj.cityId = jo.getString("id");
                    obj.cityName = jo.getString("city");
                    obj.setId(jo.getString("id"));
                    obj.setName(jo.getString("city"));
                    //obj.setPhone(TAG_PHONE_MOBILE);
                    sendCityToDB(obj);
                }

            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Insert data to DB

    public void sendCityToDB(ModelCity obj)
    {

        db.addCity(obj.getId(), obj.getName());
    }



    /*************************Get All Categories***************/

    private void saveCat(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            JSONArray jObj1 = jObj.getJSONArray("userLogin");
            JSONObject boolObject = jObj1.getJSONObject(0);
            boolean error = boolObject.getBoolean("error");
            JSONObject catListObject = jObj1.getJSONObject(2);

            Log.d(TAG, jObj.toString());
            // Check for error node in json
            if (!error) {
                db.deleteCategory();
                Log.d(TAG, "In Category List No Error ");
                ModelCategory obj = new ModelCategory();
                JSONArray result = catListObject.getJSONArray("categoryList");
                for(int i = 0; i<result.length(); i++){

                    JSONObject jo = result.getJSONObject(i);
                    String id = jo.getString("cid");
                    String name = jo.getString("category");
                   // String icon = jo.getString("icon");
                    obj.catId = jo.getString("cid");
                    obj.catName = jo.getString("category");
                    obj.catIcon = "test";
                    //obj.catIcon = jo.getString("icon");
                    obj.setId(jo.getString("cid"));
                    obj.setName(jo.getString("category"));
                    obj.setIcon("test");
                    //obj.setPhone(TAG_PHONE_MOBILE);
                    sendCatToDB(obj);
                }

            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    // Insert data to DB
    public void sendCatToDB(ModelCategory obj)
    {

        db.addCategory(obj.getId(), obj.getName(), obj.getIcon());
    }

    /*************************Get All  Sub Categories***************/

    private void saveSubCat(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            JSONArray jObj1 = jObj.getJSONArray("userLogin");
            JSONObject boolObject = jObj1.getJSONObject(0);
            boolean error = boolObject.getBoolean("error");
            JSONObject catListObject = jObj1.getJSONObject(3);

            Log.d(TAG, jObj.toString());
            // Check for error node in json
            if (!error) {
                db.deleteSubCategory();
                Log.d(TAG, "In Category List No Error ");
                ModelSubCategory obj = new ModelSubCategory();
                JSONArray result = catListObject.getJSONArray("SubCategoryList");
                for(int i = 0; i<result.length(); i++){

                    JSONObject jo = result.getJSONObject(i);
                    String scid = jo.getString("scid");
                    String cid = jo.getString("cid");
                    String name = jo.getString("category");

                    obj.setSubCatId(scid);
                    obj.setId(cid);
                    obj.setName(name);

                    sendSubCatToDB(obj);
                }

            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getApplicationContext(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    // Insert data to DB
    public void sendSubCatToDB(ModelSubCategory obj)
    {

        db.addSubCategory(obj.getSubCatId(), obj.getId(), obj.getName());
    }



    ///***********************Get Image and save to Internal Storage********************//

    public class DownloadImage  extends AsyncTask<String, Void, Bitmap> {
        //ProgressDialog loading;
        private ImageView imgView;
        /*public DownloadImage(ImageView imageView)
        {
            this.imgView = imageView;

        }*/

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //loading = ProgressDialog.show(ViewImage.this, "Uploading...", null, true, true);
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            super.onPostExecute(b);
            //this.imgView.setImageBitmap(b);
            saveImageToInternalStorage(b);
            //loading.dismiss();
            //saveImageToInternalStorage(b);
            //imageView.setImageBitmap(b);

        }

        @Override
        protected Bitmap doInBackground(String... params) {
            String id = params[0];
            //String add = "http://10.0.3.2/httpurltest/getImage.php?id="+id;
            String add = AppConfig.URL_For_Image + id+".jpg";
            URL url = null;
            Bitmap image = null;
            try {
                url = new URL(add);
                image = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return image;
        }

    }
    /************ Save Image to Local Storage***************/
    public boolean saveImageToInternalStorage(Bitmap image) {

        try {
            // Use the compress method on the Bitmap object to write image to the OutputStream
            Context context=ActivityLogin.this;
            FileOutputStream fos = context.openFileOutput("profile.png", Context.MODE_PRIVATE);

            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
            Log.d(TAG, "Image is saved to local storage ");
            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}


