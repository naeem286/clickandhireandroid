package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Created by Naeem Asghar on 6/9/2016.
 */
public class FragmentProfessional extends Fragment {

    private static final String TAG = MainActivity.class.getSimpleName();
    private String SearchByKey="searchBy";
    private String SearchCity="city";
    private RecyclerView recyclerView;
    //private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private EditText txtSearchText;
    private ImageView imvFilter;
    private ImageView btn_search;
    private String userName;
    private JSONArray jsonArray;
    private SQLiteHandler db;
    public List<ModelUser> professionalList;
    AdapterProfessional adapterProfessional;
    public int spf_Id;
    public int spf_City;


    private ArrayList<Professional> arrayList= new ArrayList<Professional>();
    //@Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_professional,container,false);
        View searToolBarView = inflater.inflate(R.layout.tool_bar_search, null);
        txtSearchText=(EditText) v.findViewById(R.id.search_text);
        imvFilter=(ImageView) v.findViewById(R.id.edt_filter);
        btn_search=(ImageView) v.findViewById(R.id.btn_search);
        professionalList = new ArrayList<ModelUser>();
        recyclerView= (RecyclerView) v.findViewById(R.id.recyclerView);

        Log.d(TAG, "View Online List Method send to data Provider ");

        adapterProfessional = new AdapterProfessional(getActivity(),professionalList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterProfessional);
        getFilterValues();
        recyclerView.addOnItemTouchListener(new FragmentDashboard.RecyclerTouchListener(getContext(), recyclerView, new FragmentDashboard.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModelUser professional = professionalList.get(position);
                //Toast.makeText(getActivity(), professional.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                // Launch Login Activity
                Intent intent = new Intent(getActivity(), ActivityProfessionalDetail.class);
                String cityName=db.getCityById(professional.getCity());
                intent.putExtra("ratingBool", "1");
                intent.putExtra("id", professional.getId());
                intent.putExtra("name", professional.getName());
                intent.putExtra("city", cityName);
                intent.putExtra("tag_line", professional.getTagLine());
                intent.putExtra("summary", professional.getDescription());
                intent.putExtra("speciality", professional.getSpeciality());
                intent.putExtra("experience", professional.getExperience());
                intent.putExtra("qualification", professional.getQualification());
                intent.putExtra("fee", professional.getFees());
                intent.putExtra("phone", professional.getPhone());
                intent.putExtra("email", professional.getEmail());
                intent.putExtra("rating", professional.getAvtRating());
                intent.putExtra("totalRating",professional.getTotalRating());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // SQLite database handler
        db = new SQLiteHandler(getActivity());

        // Search button Click Event
        btn_search.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(spf_Id==1) {
                    String textSearch = txtSearchText.getText().toString().trim();
                    Log.d(TAG, "On Click Listener is working");
                    if (!textSearch.isEmpty()) {
                        getUserBySpeciality(textSearch, Integer.toString(spf_City));
                        /*Toast.makeText(getActivity(),
                                "Commming soon professional Speciality First!", Toast.LENGTH_LONG)
                                .show();*/

                    } else {
                        // Prompt user to enter credentials
                        Toast.makeText(getActivity(),
                                "Please enter the Professional Speciality First!", Toast.LENGTH_LONG)
                                .show();
                    }
                } else{
                    String textSearch = txtSearchText.getText().toString().trim();
                    Log.d(TAG, "On Click Listener is working");
                    if (!textSearch.isEmpty()) {
                        getUserByName(textSearch,Integer.toString(spf_City));

                    } else {
                        Toast.makeText(getActivity(),
                                "Please enter the Professional Name First!", Toast.LENGTH_LONG)
                                .show();
                    }

                }
            }

        });
        txtSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        imvFilter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showChangeLangDialog();
            }

        });
        return v;
    }

    public void showChangeLangDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_pfilter, null);
        dialogBuilder.setView(dialogView);

        final Button btn_name = (Button) dialogView.findViewById(R.id.search_name);
        final Button btn_speciality = (Button) dialogView.findViewById(R.id.search_speciality);
        final Spinner spr_city = (Spinner) dialogView.findViewById(R.id.spr_city);
        List<String> cityList = db.getAllCities();
        // Creating adapter for spinner for city
        ArrayAdapter<String> cityDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cityList);
        spr_city.setAdapter(cityDataAdapter);
        spr_city.setSelection(spf_City-1);
        if(spf_Id==0)
        {
            btn_name.setBackgroundResource(R.drawable.button_selected);
            btn_speciality.setBackgroundResource(R.drawable.button_unselected);
            txtSearchText.setHint("Write Name...");
        }else{
            btn_speciality.setBackgroundResource(R.drawable.button_selected);
            btn_name.setBackgroundResource(R.drawable.button_unselected);
            txtSearchText.setHint("Write Speciality...");
        }

        //spr_city.setSelection(4);
        btn_name.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btn_name.setBackgroundResource(R.drawable.button_selected);
                btn_speciality.setBackgroundResource(R.drawable.button_unselected);
                spf_Id=0;
                setFilterValues(spf_Id, spf_City);

            }
        });

        btn_speciality.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                btn_speciality.setBackgroundResource(R.drawable.button_selected);
                btn_name.setBackgroundResource(R.drawable.button_unselected);
                spf_Id = 1;
                setFilterValues(spf_Id, spf_City);

            }

        });

        spr_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spf_City = position + 1;
                setFilterValues(spf_Id, spf_City);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        dialogBuilder.setTitle("Search Professional Filter");
        dialogBuilder.setMessage("Select Search By:");
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

        // SqLite database handler
        //db = new SQLiteHandler(this);
        //loadUserInfo(db);
    }

    public void setFilterValues(int id, int city)
    {
        SharedPreferences prefSearchBy = getActivity().getSharedPreferences(SearchByKey, Context.MODE_PRIVATE);
        SharedPreferences prefCity = getActivity().getSharedPreferences(SearchCity, Context.MODE_PRIVATE);
        prefSearchBy.edit().putInt(SearchByKey, id).apply();
        prefCity.edit().putInt(SearchCity, city).apply();
        if(spf_Id==1)
        {
            txtSearchText.setHint("Write Speciality...");
            //Toast.makeText(getActivity().getApplicationContext(), "Speciality", Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(getActivity().getApplicationContext(), "Name", Toast.LENGTH_LONG).show();
            txtSearchText.setHint("Write Name...");
        }
    }
    public void getFilterValues()
    {
        SharedPreferences prefSearchBy = getActivity().getSharedPreferences(SearchByKey, Context.MODE_PRIVATE);
        SharedPreferences prefCity = getActivity().getSharedPreferences(SearchCity, Context.MODE_PRIVATE);
        spf_Id = prefSearchBy.getInt(SearchByKey, 0);
        spf_City = prefCity.getInt(SearchCity, 0);
        if(spf_Id==1)
        {
            txtSearchText.setHint(" Write Speciality...");
            //Toast.makeText(getActivity().getApplicationContext(), "Speciality", Toast.LENGTH_LONG).show();
        }
        else {
            //Toast.makeText(getActivity().getApplicationContext(), "Name", Toast.LENGTH_LONG).show();
            txtSearchText.setHint(" Write Name...");
        }
        //Toast.makeText(getActivity().getApplicationContext(), "City="+spf_City, Toast.LENGTH_LONG).show();

    }
    //SHOW DATA IN CUSTOMIZE LIST
    /**
     * function to verify login details in mysql db
     * */
    private void getUserByName(final String name, final String cityId){
        class GetUserByName extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Searching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getProfessionals(s);
                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Search is done Successfully", Toast.LENGTH_LONG).show();
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("name",name);
                hashMap.put("city",cityId);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_USER_BY_NAME,hashMap);
                return s;
            }
        }

        GetUserByName cul = new GetUserByName();
        cul.execute();
    }

    /**
     * function to get
     * */
    private void getUserBySpeciality(final String speciality, final String cityId){
        class GetUserBySpeciality extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Searching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getProfessionals(s);
                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Search is done Successfully", Toast.LENGTH_LONG).show();
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("speciality",speciality);
                hashMap.put("city",cityId);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_USER_BY_SPECIALITY,hashMap);
                return s;
            }
        }

        GetUserBySpeciality cul = new GetUserBySpeciality();
        cul.execute();
    }
    /**
     * function to Get Data from JSON and store it to DB
     * */
    private void getProfessionals(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            Log.d(TAG, jObj.toString());
            // Check for error node in json
            if (!error) {
                Log.d(TAG, "In Cities List No Error ");
                professionalList.clear();
                JSONArray result = jObj.getJSONArray("userList");
                for(int i = 0; i<result.length(); i++){
                    ModelUser obj = new ModelUser();

                    JSONObject user1 = result.getJSONObject(i);
                    String id = user1.getString("uid");
                    JSONObject user = user1.getJSONObject("user");
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

                    obj.setId(id);
                    obj.setName(name);
                    obj.setPhone(phone);
                    obj.setEmail(email);
                    obj.setTagLine(tagLine);
                    obj.setFees(fees);
                    obj.setDescription(description);
                    obj.setExperience(experience);
                    obj.setSpeciality(speciality);
                    obj.setCity(city);
                    obj.setProfileImage(profileImage);
                    obj.setApiKey(apiKey);
                    obj.setAvtRating(avgRating);
                    obj.setTotalRating(totalRatings);
                    obj.setCreated_at(created_at);

                    Log.d(TAG, "Get User is working");
                    Log.d(TAG, "before sending to  list" + obj.getId() + obj.getName() + obj.getSpeciality() + obj.getFees());
                    professionalList.add(obj);
                    //Refresh cache directory downloaded images
                    //adapterProfessional.imageLoader.clearCache();
                    adapterProfessional.notifyDataSetChanged();

                }

            } else {
                // Error in login. Get the error message
                String errorMsg = jObj.getString("error_msg");
                Toast.makeText(getActivity(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


