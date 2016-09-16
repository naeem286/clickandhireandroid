package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;


public class ActivityRegister extends AppCompatActivity {

    //Button btn_Register,btn_Login;
    private static final String TAG = ActivityRegister.class.getSimpleName();
    private Button btnRegister;
    private Button btnLinkToLogin;
    private EditText inputFullName;
    private EditText inputPhone;
    private EditText inputPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_register);

        inputFullName = (EditText) findViewById(R.id.name);
        inputPhone = (EditText) findViewById(R.id.phone);
        inputPassword = (EditText) findViewById(R.id.password);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnLinkToLogin = (Button) findViewById(R.id.btnLinkToLoginScreen);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // Session manager
        session = new SessionManager(getApplicationContext());

        // SQLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Check if user is already logged in or not
        if (session.isLoggedIn()) {
            // User is already logged in. Take him to main activity
            Intent intent = new Intent(ActivityRegister.this,
                    MainActivity.class);
            startActivity(intent);
            finish();
        }

        // Register Button Click event
        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String name = inputFullName.getText().toString().trim();
                String phone = inputPhone.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                if (!name.isEmpty() && !phone.isEmpty() && !password.isEmpty()) {
                    Drawable myDrawable = getResources().getDrawable(R.drawable.profile);
                    Bitmap Image      = ((BitmapDrawable) myDrawable).getBitmap();
                    String imageData = encodeToBase64(Image);
                    registerUser(name, phone, password, imageData);
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter your details!", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        // Link to Login Screen
        btnLinkToLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Function to store user in MySQL database will post params(tag, name,
     * email, password) to register url
     * */

    private void registerUser(final String name, final String phone, final String password,final String profileImage){
        class RegisterUser extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityRegister.this,"Registering...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getUser(s);
                //Toast.makeText(ActivityRegister.this,s,Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> params1 = new HashMap<String,String>();
                params1.put("name", name);
                params1.put("phone", phone);
                params1.put("password", password);
                params1.put("image", profileImage);
                Log.e(TAG, "Registration Parameters: " + name.toString() + "phone=" + phone.toString() + "password=" + password.toString());
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_REGISTER,params1);

                return s;
            }
        }

        RegisterUser cul = new RegisterUser();
        cul.execute();
    }
    /**
     * function to Get Data from JSON and store it to DB
     * */
    private void getUser(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            // Check for error node in json
            if (!error) {
                // user successfully logged in
                // Create login session
               // session.setLogin(true);
                // Now store the user in SQLite
                String uid = jObj.getString("uid");
                JSONObject user = jObj.getJSONObject("user");
                String name = user.getString("name");
                String phone = user.getString("phone");
                String apiKey = user.getString("apiKey");
                String status = user.getString("status");
                String created_at = user.getString("created_at");

                // Inserting row in users table
                // Delete old data
                db.deleteUsers();
                // Inserting row in users table
                db.addNewUser(uid, name, phone, apiKey, status, created_at);


                Toast.makeText(getApplicationContext(), "You are registered successfully. Try login now!", Toast.LENGTH_LONG).show();

                // Launch login activity
                Intent intent = new Intent(ActivityRegister.this, ActivityLogin.class);
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

    /************** Method to convert Image to String*************/
    public static String encodeToBase64(Bitmap image) {
        System.gc();

        if (image == null)return null;

        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT); // min minSdkVersion 8

        return imageEncoded;
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
