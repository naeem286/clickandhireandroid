 package com.example.naeemasghar.clickandhire;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityWorkList extends AppCompatActivity {

    private static final String TAG = ActivityWorkList.class.getSimpleName();
    private List<ModelTask> workList = new ArrayList<>();
    private RecyclerView recyclerView;
    private AdapterWork wAdapter;

    //private Button btnSearch;
    //private EditText txtSearchText;

    private EditText txtSearchText;
    private ImageView imvFilter;
    private ImageView btnSearch;
    private TextView txtCatHeading;
    private SQLiteHandler db;
    private String userCity;
    private String Id;
    private String UserId;
    private String SearchCityKey="cityWork";
    private String SearchSubCategoryKey="subCategoryWork";
    private String catId;
    private String catName;
    public int spf_City=0;
    public int spf_subCatId=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_work_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Latest Task");
        // btnSearch=(Button) findViewById(R.id.search_button);
        //txtSearchText=(EditText) findViewById(R.id.search_text);
        txtSearchText=(EditText) findViewById(R.id.search_text);
        imvFilter=(ImageView) findViewById(R.id.edt_filter);
        btnSearch=(ImageView) findViewById(R.id.btn_search);
        txtCatHeading=(TextView)findViewById(R.id.cat_heading);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // get Value for filter from shared Prefrences
        getFilterValues();
        catName= getIntent().getStringExtra("catName");
        catId= getIntent().getStringExtra("catId");
        txtCatHeading.setText("Browse "+catName);
        // SQLite database handler
        db = new SQLiteHandler(this);
        HashMap<String, String> user = db.getUserDetails();
        Id = user.get("id");
        userCity = user.get("city");
        spf_City= Integer.parseInt(userCity);
        getTaskByCategory();


        wAdapter = new AdapterWork(workList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerNewsDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(wAdapter);

        String cityName=db.getCityById(Integer.toString(spf_City));
        String catName=db.getSubCategoryById(Integer.toString(spf_subCatId));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModelTask work = workList.get(position);
                //Toast.makeText(getApplicationContext(), work.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                // Launch Login Activity
                Intent intent = new Intent(ActivityWorkList.this, ActivityWorkDetail.class);
                intent.putExtra("uid", Id);
                intent.putExtra("wid", work.getId());
                intent.putExtra("title", work.getTitle());
                intent.putExtra("detail", work.getDetail());
                intent.putExtra("fee", work.getFees());
                intent.putExtra("userName", work.getName());
                intent.putExtra("city", work.getCity());
                intent.putExtra("awardBid", work.getAwarded_bid());
                intent.putExtra("awardName", work.getAwarded_name());
                intent.putExtra("date", work.getDate());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // Search button Click Event
        btnSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //db.deleteProfessional();
                String title = txtSearchText.getText().toString().trim();
                Log.d(TAG, "On Click Listener is working");
                if (!title.isEmpty()) {
                    if(spf_subCatId==0) {
                        // for all sub categories
                        getTaskByAllSubCat(title, catId, Integer.toString(spf_City));
                    }else{
                        // applying full filer to search.
                        getTaskByTitle(title, catId, Integer.toString(spf_subCatId), Integer.toString(spf_City));
                    }

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(), "Please enter task title First!", Toast.LENGTH_LONG).show();
                }
            }

        });
        // to open filter dialog
        imvFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showChangeLangDialog();
            }

        });


    }

    public void showChangeLangDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_wfilter, null);
        dialogBuilder.setView(dialogView);

        final Spinner spr_city = (Spinner) dialogView.findViewById(R.id.spr_city);
        final Spinner spr_subCategory = (Spinner) dialogView.findViewById(R.id.spr_suCategory);
        spr_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spf_City = position+1;
               // Toast.makeText(getApplicationContext(), " slected city id="+spf_City+" subCat="+spf_subCatId, Toast.LENGTH_LONG).show();
                setFilterValues(spf_City,spf_subCatId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spr_subCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spf_subCatId = position;
                setFilterValues(spf_City,spf_subCatId);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        List<String> cityList = db.getAllCities();
        // Creating adapter for spinner for city
        ArrayAdapter<String> cityDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityList);
        spr_city.setAdapter(cityDataAdapter);

        List<String> subCatList = db.getAllSubCategories(catId);

        // Creating adapter for spinner for subCategories
        ArrayAdapter<String> suCatDataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subCatList);
        spr_subCategory.setAdapter(suCatDataAdapter);

        spr_city.setSelection(spf_City-1);
        spr_subCategory.setSelection(0);

        dialogBuilder.setTitle("Browse Work Filter");
        //dialogBuilder.setMessage("Select Search By:");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //setEditText(title, et_detail.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    public void setFilterValues(int city, int catId)
    {
        SharedPreferences prefCity = this.getSharedPreferences(SearchCityKey, Context.MODE_PRIVATE);
        SharedPreferences prefSubCat = this.getSharedPreferences(SearchSubCategoryKey, Context.MODE_PRIVATE);
        prefCity.edit().putInt(SearchCityKey, city).apply();
        prefSubCat.edit().putInt(SearchSubCategoryKey, catId).apply();
        spf_City = prefCity.getInt(SearchCityKey, 0);
        spf_subCatId = prefSubCat.getInt(SearchSubCategoryKey, 0);
    }
    public void getFilterValues()
    {
        SharedPreferences prefCity = this.getSharedPreferences(SearchCityKey, Context.MODE_PRIVATE);
        SharedPreferences prefSubCat = this.getSharedPreferences(SearchSubCategoryKey, Context.MODE_PRIVATE);
        spf_City = prefCity.getInt(SearchCityKey, 0);
        spf_subCatId = prefSubCat.getInt(SearchSubCategoryKey, 0);
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    // Method to make request to server
    private void getTaskByCategory(){
        class GetUserTasks extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityWorkList.this,"Searching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getTasks(s);
                loading.dismiss();
                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                //Toast.makeText(ActivityWorkList.this, "Search is done Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("catId",catId);
                hashMap.put("city",userCity);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_TASKS_BY_CATEGORIES,hashMap);
                return s;
            }
        }

        GetUserTasks cul = new GetUserTasks();
        cul.execute();
    }
    private void getTaskByTitle(final String title ,final String catId,final String subCatId, final String city){
        class GetUserTasks extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityWorkList.this,"Searching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getTasks(s);
                loading.dismiss();
                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                Toast.makeText(ActivityWorkList.this, "Search is done Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("title",title);
                hashMap.put("catId",catId);
                hashMap.put("subCatId",subCatId);
                hashMap.put("city",city);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_TASKS_BY_TITLE_FILTER,hashMap);
                return s;
            }
        }

        GetUserTasks cul = new GetUserTasks();
        cul.execute();
    }

    private void getTaskByAllSubCat(final String title ,final String catId, final String city){
        class GetUserTasks extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ActivityWorkList.this,"Searching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getTasks(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                Toast.makeText(ActivityWorkList.this, "Search is done Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("title",title);
                hashMap.put("catId",catId);
                hashMap.put("city",city);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_TASKS_BY_TITLE_All_CATEGORIES,hashMap);
                return s;
            }
        }

        GetUserTasks cul = new GetUserTasks();
        cul.execute();
    }
    /**
     * function to Get Data from JSON and store it to DB
     * */
    private void getTasks(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            //Toast.makeText(getApplicationContext(),
            //      json, Toast.LENGTH_LONG).show();
            Log.d(TAG, jObj.toString());
            // Check for error node in json
            if (!error) {
                Log.d(TAG, "In Task List No Error ");
                workList.clear();
                ModelTask obj;
                JSONArray result = jObj.getJSONArray("taskList");
                for(int i = 0; i<result.length(); i++){
                    obj= new ModelTask();
                    JSONObject task1 = result.getJSONObject(i);
                    String id = task1.getString("tid");
                    JSONObject user = task1.getJSONObject("task");
                    String user_id = user.getString("user_id");
                    String cat_id = user.getString("cat_id");
                    String user_name = user.getString("user");
                    String title = user.getString("title");
                    String detail = user.getString("detail");
                    String fees = user.getString("fees");
                    String city = user.getString("city");
                    String awardedBid= user.getString("award_bid");
                    String awardedName= user.getString("award_name");
                    String status = user.getString("status");
                    String created_at = user.getString("created_at");

                    obj.setId(id);
                    obj.setUid(user_id);
                    obj.setCid(cat_id);
                    obj.setName(user_name);
                    obj.setTitle(title);
                    obj.setDetail(detail);
                    obj.setFees(fees);
                    obj.setCity(city);
                    obj.setAwarded_bid(awardedBid);
                    obj.setAwarded_name(awardedName);
                    obj.setStatus(status);
                    obj.setDate(created_at);
                    workList.add(obj);
                    wAdapter.notifyDataSetChanged();

                    Log.d(TAG, "Get User is working");

                }

            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(this,
                        errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}

