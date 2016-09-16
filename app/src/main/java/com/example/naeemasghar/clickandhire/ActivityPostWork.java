package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class ActivityPostWork extends AppCompatActivity {

    private SQLiteHandler db;
    private String Id;
    private String ApiKey;
    Spinner sprCity;
    Spinner sprCategory;
    TextView txtTitle;
    TextView txtDetail;
    EditText txtFees;
    Button btnPost;
    private TextView txtCatHeading;
    private String catId;
    private String subCatId;
    private String catName;
    private String selectedCityId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_post_work);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /// new
        txtCatHeading=(TextView)findViewById(R.id.cat_heading);
        sprCity=(Spinner) findViewById(R.id.city);
        txtTitle=(TextView) findViewById(R.id.title);
        txtDetail=(TextView) findViewById(R.id.detail);
        txtFees=(EditText) findViewById(R.id.fees);
        btnPost=(Button) findViewById(R.id.btnPost);

        catId= getIntent().getStringExtra("catId");
        subCatId= getIntent().getStringExtra("subCatId");
        catName= getIntent().getStringExtra("catName");
        txtCatHeading.setText("Post In "+catName +" Category");
       // Toast.makeText(getApplicationContext(), "Cat id="+catId+ "sub Cat id="+subCatId, Toast.LENGTH_LONG).show();
        // Loading spinner data from database
        loadSpinnerData();
        // SqLite database handler
        db = new SQLiteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        Id = user.get("id");
        ApiKey = user.get("apiKey");
        selectedCityId=user.get("city");
        sprCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //City=sprCityList.getSelectedItem().toString();
                selectedCityId= Integer.toString(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Post Task button Click Event
        btnPost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String title = txtTitle.getText().toString().trim();
                String detail = txtDetail.getText().toString().trim();
                String fees = txtFees.getText().toString().trim();
                String city = sprCity.getSelectedItem().toString();

                String apiKey = ApiKey;


                // Check for empty data in the form
                if (!title.isEmpty() &&!detail.isEmpty() &&!fees.isEmpty()  &&!city.isEmpty()
                        && !Id.isEmpty()&&!ApiKey.isEmpty()) {
                    // login user
                    postUserTask(title,detail,fees, city, Id,ApiKey);
                } else {
                    //  Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the all credentials!", Toast.LENGTH_LONG).show();
                }
            }

        });
    }

    /* Post Task to Sever*/
    private void postUserTask(final String title,final String detail, final String fees, final String city,
                              final String userId,final String apiKey){


        class PostTasks extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityPostWork.this, "Posting..", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("user_id",userId);
                hashMap.put("cat_id",catId);
                hashMap.put("scat_id",subCatId);
                hashMap.put("title",title);
                hashMap.put("detail",detail);
                hashMap.put("fees",fees);
                hashMap.put("city",selectedCityId);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_POST_TASK,hashMap);

                return s;
            }
        }

        PostTasks ue = new PostTasks();
        ue.execute();
    }

    /**
     * Function to load the spinner data from SQLite database
     * */
    private void loadSpinnerData() {
        // database handler
        SQLiteHandler db = new SQLiteHandler(this);

        // Spinner Drop down elements
        List<String> cityList = db.getAllCities();

        // Creating adapter for spinner for city
        ArrayAdapter<String> cityDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);

        // Drop down layout style - list view for cities
        cityDataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner

        sprCity.setAdapter(cityDataAdapter);
    }
}
