package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class ActivityMyProfile extends AppCompatActivity {

    private SQLiteHandler db;

    private static final String TAG = ActivityMyProfile.class.getSimpleName();
    private static final int SELECT_PICTURE = 1;
    public static final String UPLOAD_IMAGE_KEY = "image";
    public static final String UPLOAD_USER_ID_KEY = "userId";
    public ImageLoader imageLoader;
    private ImageView profileImage;
    private TextView txtName;
    private RatingBar ratingBar;
    private TextView txtCity;
    private TextView txtTagLine;
    private TextView txtSummary;
    private TextView txtSpeciality;
    private TextView txtExperience;
    private TextView txtFee;
    private TextView txtPhone;
    private TextView txtEmail;
    private TextView txtSelectedCity;
    private TextView txtRating;
    private TextView txtTotalRatings;
    private Spinner sprCityList;

    // Variables to store values from Database
    private String Id;
    private String Name;
    private String Phone ;
    private String Email ;
    private String TagLine;
    private String Fees ;
    private String Description ;
    private String Experience;
    private String Speciality;
    private String ApiKey;
    private String AvgRating;
    private String totalRating;
    private String City;
    private String Category;
    private int selectedCityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageLoader = new ImageLoader(this);

        profileImage=(ImageView)findViewById(R.id.profile_image);
        txtName= (TextView)findViewById(R.id.username);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtCity= (TextView)findViewById(R.id.city);
        txtTagLine= (TextView)findViewById(R.id.tag_line);
        txtSummary= (TextView)findViewById(R.id.description);
        txtSpeciality= (TextView)findViewById(R.id.speciality);
        txtExperience= (TextView)findViewById(R.id.experience);
        txtFee= (TextView)findViewById(R.id.fees);
        txtPhone= (TextView)findViewById(R.id.phone);
        txtEmail= (TextView)findViewById(R.id.email);
        txtRating= (TextView)findViewById(R.id.rating);
        txtTotalRatings= (TextView)findViewById(R.id.total_rating);
        sprCityList= (Spinner)findViewById(R.id.cityList);
        sprCityList.setEnabled(false);
        // SqLite database handler
        db = new SQLiteHandler(this);
        // To load data from SQLite
        loadSpinnerData();
        loadUserInfo(db);

        profileImage.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }

        });

    }
    private void setUpdateProfileData()
    {
        String name = txtName.getText().toString().trim();
        String email = txtEmail.getText().toString().trim();
        String tagLing=txtTagLine.getText().toString().trim();
        String fees = txtFee.getText().toString().trim();
        String description = txtSummary.getText().toString().trim();
        String experience = txtExperience.getText().toString().trim();
        String speciality = txtSpeciality.getText().toString().trim();
        String apiKey = ApiKey;

        updateUserProfile(name, email, tagLing, fees, description, experience, speciality,String.valueOf(selectedCityId), apiKey);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ActivityMyProfile.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                if (Build.VERSION.SDK_INT < 19) {
                    String selectedImagePath = getPath(selectedImageUri);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    SetImage(bitmap);
                } else {
                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = this.getContentResolver().openFileDescriptor(selectedImageUri, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        SetImage(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    private void SetImage(Bitmap image) {
        this.profileImage.setImageBitmap(image);
        // upload
        String imageData = encodeTobase64(image);
        uploadImage(imageData);
        saveImageToInternalStorage(image);

    }


    private void loadUserInfo(SQLiteHandler db)
    {
        HashMap<String, String> user = db.getUserDetails();
        Id = user.get("id");
        Name = user.get("name");
        Phone = user.get("phone");
        Email = user.get("email");
        TagLine= user.get("gender");
        Fees = user.get("fees");
        Description = user.get("description");
        Experience = user.get("experience");
        Speciality = user.get("speciality");
        ApiKey = user.get("apiKey");
        City = user.get("city");
        AvgRating = user.get("avgRating");
        totalRating = user.get("totalRatings");
        String profilePhoto=user.get("profileImage");
        selectedCityId= Integer.valueOf(City);
        sprCityList.setSelection(Integer.valueOf(City) - 1);
        txtName.setText(Name);
        String cityName=db.getCityById(City);
        txtCity.setText(cityName+", Pakistan");
        txtPhone.setText(Phone);
        txtEmail.setText(Email);
        txtTagLine.setText(TagLine);
        txtFee.setText(Fees);
        txtSummary.setText(Description);
        txtExperience.setText(Experience);
        txtSpeciality.setText(Speciality);
        profileImage.setImageBitmap(getThumbnail("profile.png"));
        String urlForImage = AppConfig.URL_For_Image + Id+".jpg";

       // imageLoader.DisplayImage(urlForImage, profileImage);

        if(AvgRating.equals("null"))
        {
            ratingBar.setRating(0);
            totalRating="0";
            txtRating.setText("0");
            txtTotalRatings.setText("("+String.valueOf(totalRating)+")");
        }else {


            ratingBar.setRating(Math.round(Float.parseFloat(AvgRating)));
            txtRating.setText(String.valueOf(Math.round(Float.parseFloat(AvgRating))));
            txtTotalRatings.setText("("+String.valueOf(totalRating)+")");
        }

    }
    public Bitmap getThumbnail(String filename) {


        Bitmap thumbnail = null;


        // look in internal storage
        try {
            Context context=this;
            File filePath = context.getFileStreamPath(filename);
            FileInputStream fi = new FileInputStream(filePath);
            thumbnail = BitmapFactory.decodeStream(fi);
        } catch (Exception ex) {
            Log.e("get Image storage", ex.getMessage());
        }
        return thumbnail;


    }


    public void editProfile(View view)
    {
        switch(view.getId()) {
            case R.id.edt_tag_line:
                showChangeLangDialog("Tag line", txtTagLine.getText().toString());
                break;
            case R.id.edt_summary:
                showChangeLangDialog("Summary", txtSummary.getText().toString());
                break;
            case R.id.edt_speciality:
                showChangeLangDialog("Speciality", txtSpeciality.getText().toString());
                break;
            case R.id.edt_experience:
                showChangeLangDialog("Experience", txtExperience.getText().toString());
                break;
            case R.id.edt_fee:
                showChangeLangDialog("Fee", txtFee.getText().toString());
                break;

            case R.id.edt_email:
                showChangeLangDialog("Email", txtEmail.getText().toString());
                break;
            case R.id.edt_city:
                showChangeLangDialogCity("City", City);
                break;
            case R.id.edt_password:
                showChangeLangDialogPassword("Password");
                break;

        }
    }

    public void showChangeLangDialogPassword(final String title) {
        String text="";
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_password, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(title);

        final EditText old_password = (EditText) dialogView.findViewById(R.id.txtOldPassword);
        final EditText new_password = (EditText) dialogView.findViewById(R.id.txtNewPassword);
        final EditText confirm_password = (EditText) dialogView.findViewById(R.id.txtConfirmPassword);

        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                final String oldPassword = old_password.getText().toString().trim();
                final String newPassword = new_password.getText().toString().trim();
                final String confirmPassword = confirm_password.getText().toString().trim();
                if(!newPassword.isEmpty() && !confirmPassword.isEmpty()&& !oldPassword.isEmpty()) {
                    if(newPassword.equals(confirmPassword))
                    {
                         changePassword(oldPassword, newPassword);
                    }else{
                        Toast.makeText(getApplicationContext(), "New password did not match", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Enter All Fields First", Toast.LENGTH_LONG).show();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

        // SqLite database handler
        db = new SQLiteHandler(this);
        loadUserInfo(db);
    }
    public void showChangeLangDialogCity(final String title, final String city) {
        String text="";
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_city_list, null);
        dialogBuilder.setView(dialogView);

        final Spinner spr_city = (Spinner) dialogView.findViewById(R.id.spr_city);

        dialogBuilder.setTitle(title);
        List<String> cityList = db.getAllCities();
        ArrayAdapter<String> cityDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
        spr_city.setAdapter(cityDataAdapter);

        spr_city.setSelection(Integer.parseInt(city) - 1);
        spr_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCityId = position + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                setEditText(title, Integer.toString(selectedCityId));
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

        // SqLite database handler
        db = new SQLiteHandler(this);
        loadUserInfo(db);
    }
    public void showChangeLangDialog(final String title, final String detail) {
        String text="";
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText et_detail = (EditText) dialogView.findViewById(R.id.detail);

        dialogBuilder.setTitle(title);
        et_detail.setText(detail);
        dialogBuilder.setMessage("Enter text or change");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                setEditText(title, et_detail.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

        // SqLite database handler
        db = new SQLiteHandler(this);
        loadUserInfo(db);
    }

    public void setEditText(String title,String detail)
    {
        switch(title)
        {
            case "Tag line":
                txtTagLine.setText(detail);
                setUpdateProfileData();
                break;
            case "Summary":
                txtSummary.setText(detail);
                setUpdateProfileData();
                break;
            case "Speciality":
                txtSpeciality.setText(detail);
                setUpdateProfileData();
                break;
            case "Experience":
                txtExperience.setText(detail);
                setUpdateProfileData();
                break;
            case "Fee":
                txtFee.setText(detail);
                setUpdateProfileData();
                break;
            case "Email":
                txtEmail.setText(detail);
                setUpdateProfileData();
                break;
            case "City":
                //txtEmail.setText(detail);
                setUpdateProfileData();
                break;
        }
    }
    private void changePassword(final String oldPassword,final String newPassword){


        class UpdatePassword extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityMyProfile.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();

            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("old_password",oldPassword);
                hashMap.put("new_password",newPassword);
                hashMap.put("apiKey",ApiKey);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_UPDATE_USER_PASSWORD,hashMap);

                return s;
            }
        }

        UpdatePassword ue = new UpdatePassword();
        ue.execute();
    }
    private void updateUserProfile(final String name,final String email,final String tagLine,
                                   final String fees,final String description,final String experience,final String speciality,
                                   final String city,final String apiKey){


        class UpdateUser extends AsyncTask<Void,Void,String>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityMyProfile.this,"Updating...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                updateSqliteOnResponse(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("name",name);
                hashMap.put("email",email);
                hashMap.put("tagLine",tagLine);
                hashMap.put("fees",fees);
                hashMap.put("description",description);
                hashMap.put("experience",experience);
                hashMap.put("speciality",speciality);
                hashMap.put("city",city);
                hashMap.put("apiKey",apiKey);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_UPDATE,hashMap);

                return s;
            }
        }

        UpdateUser ue = new UpdateUser();
        ue.execute();
    }
    public void updateSqliteOnResponse(String json)
    {
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            if(!error) {
                String name = txtName.getText().toString().trim();
                String email = txtEmail.getText().toString().trim();
                String tagLing = txtTagLine.getText().toString().trim();
                String fees = txtFee.getText().toString().trim();
                String description = txtSummary.getText().toString().trim();
                String experience = txtExperience.getText().toString().trim();
                String speciality = txtSpeciality.getText().toString().trim();
                String city = Integer.toString(selectedCityId );
                String apiKey = ApiKey;
                db.updateUser(name, email, tagLing, fees, description, experience, speciality, city, apiKey);
                loadUserInfo(db);
                Toast.makeText(getApplicationContext(), "Profile is Updated Successfully", Toast.LENGTH_LONG).show();
            }
            else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static String encodeTobase64(Bitmap image) {
        System.gc();

        if (image == null)return null;

        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] b = baos.toByteArray();

        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT); // min minSdkVersion 8

        return imageEncoded;
    }


    public String getPath(Uri uri) {
        if( uri == null ) {
            return null;
        }
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = this.getContentResolver().query(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
    private void uploadImage(String imageData){
        class UploadImage extends AsyncTask<String,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityMyProfile.this, "Uploading...", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Profile is Updated Successfully", Toast.LENGTH_LONG).show();
               // imageLoader.clearCache();
               // setImageInNavHeader();
            }

            @Override
            protected String doInBackground(String... params) {
                String uploadImage = params[0];
                //String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<String,String>();
                data.put("image", uploadImage);
                data.put("userId", Id);

                String result = rh.sendPostRequest(AppConfig.URL_UPLOAD_PICTURE,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(imageData);
    }

    /************ Save Image to Local Storage***************/
    public boolean saveImageToInternalStorage(Bitmap image) {

        try {
            // Use the compress method on the Bitmap object to write image to the OutputStream
            Context context=this;
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

    /************ Load Spinner Data***************/
    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // database handler
        // Spinner Drop down elements
        List<String> cityList = db.getAllCities();

        // Creating adapter for spinner for city
        ArrayAdapter<String> cityDataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, cityList);

        // Drop down layout style - list view for cities
        cityDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sprCityList.setAdapter(cityDataAdapter);
    }
    private void setImageInNavHeader()
    {
        //Initializing NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //navHeader= (RelativeLayout)findViewById(R.id.nav_header);

        View headerview = navigationView.getHeaderView(0);
       // navHeader= (RelativeLayout) headerview.findViewById(R.id.nav_header);
        txtName=(TextView)headerview.findViewById(R.id.username);
        txtEmail=(TextView)headerview.findViewById(R.id.email);


        profileImage=(ImageView) headerview.findViewById(R.id.profile_image);
        profileImage.setImageBitmap(getThumbnail("profile.png"));

    }
}
