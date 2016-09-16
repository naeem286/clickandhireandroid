package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Naeem Asghar on 6/9/2016.
 */
public class FragmentDashboard extends Fragment {
    private List<News> newsList1 = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private String selectedCityId;
    //private AdapterNews nAdapter;

    private static final String TAG = MainActivity.class.getSimpleName();
    private SQLiteHandler db;
    private String userId;
    private String City;
    public List<ModelNews> newsList = new ArrayList<ModelNews>();;

    //@Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_dashboard,container,false);

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        //nAdapter = new AdapterNews(newsList);

        // SqLite database handler
        db = new SQLiteHandler(getActivity().getApplicationContext());
        // Fetching user de
        HashMap<String, String> user = db.getUserDetails();

        userId= user.get("id");
        City=user.get("city");
        selectedCityId= user.get("city");
        adapter = new AdapterNews(newsList);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerNewsDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        // get News from sever.
        if(!(newsList.size()>0)) {
            getCityNews(City);
        }
        Toast.makeText(getActivity(), newsList.size() + " is size of list", Toast.LENGTH_SHORT).show();
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.addNews);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postNewsDialog();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModelNews news = newsList.get(position);
                /*Toast.makeText(getActivity(), news.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                // Launch Login Activity
                Intent intent = new Intent(getActivity(), ActivityNewsDetail.class);
                intent.putExtra("title", news.getTitle());
                intent.putExtra("detail", news.getDetail());
                intent.putExtra("date", news.getDate());
                startActivity(intent);*/
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        //prepareMovieData();
        return v;
    }

    public void postNewsDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog_post_news, null);
        dialogBuilder.setView(dialogView);

        final EditText txt_title = (EditText) dialogView.findViewById(R.id.txtTitle);
        final EditText txt_detail = (EditText) dialogView.findViewById(R.id.txtDetail);
        final Spinner spr_city = (Spinner) dialogView.findViewById(R.id.spr_city);

        spr_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCityId = Integer.toString(position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        List<String> cityList = db.getAllCities();
        // Creating adapter for spinner for city
        ArrayAdapter<String> cityDataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cityList);
        spr_city.setAdapter(cityDataAdapter);

        dialogBuilder.setTitle("Post News");
       // dialogBuilder.setMessage("Write Below:");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String title = txt_title.getText().toString().trim();
                String detail = txt_detail.getText().toString().trim();
                if (!title.isEmpty() && !detail.isEmpty() ) {
                    postNews(title, detail);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please enter the all credentials", Toast.LENGTH_LONG)
                            .show();
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
        //db = new SQLiteHandler(this);
        //loadUserInfo(db);
    }
    /* Post News to Sever*/
    private void postNews(final String title, final String detail){


        class PostTasks extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Posting..","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Intent intent = getActivity().getIntent();
                getActivity().finish();
                startActivity(intent);
                // Toast.makeText(getActivity(), s, Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity().getApplicationContext(),
                        "New is Posted Successfully", Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("user_id",userId);
                hashMap.put("title",title);
                hashMap.put("detail",detail);
                hashMap.put("city",selectedCityId);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_POST_NEWS,hashMap);

                return s;
            }
        }

        PostTasks ue = new PostTasks();
        ue.execute();
    }
    // Method to make request to server
    private void getCityNews(final String city){
        class GetCityNews extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Searching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getNews(s);
                loading.dismiss();
                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
               // Toast.makeText(getActivity(), "Latest News are Updated Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("city",city);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_GET_CITY_NEWS,hashMap);
                return s;
            }
        }

        GetCityNews cul = new GetCityNews();
        cul.execute();
    }
    /**
     * function to Get Data from JSON and store it to DB
     * */
    private void getNews(String json){
        try {
            JSONObject jObj = new JSONObject(json);
            boolean error = jObj.getBoolean("error");
            //Toast.makeText(getApplicationContext(),
            //      json, Toast.LENGTH_LONG).show();
            Log.d(TAG, jObj.toString());
            // Check for error node in json
            if (!error) {
                Log.d(TAG, "In News List No Error ");
                newsList.clear();
                JSONArray result = jObj.getJSONArray("newsList");
                for(int i = 0; i<result.length(); i++){
                    ModelNews obj = new ModelNews();
                    JSONObject task1 = result.getJSONObject(i);
                    String id = task1.getString("did");
                    JSONObject user = task1.getJSONObject("news");
                    String user_id = user.getString("user_id");
                    String user_name = user.getString("user");
                    String title = user.getString("title");
                    String detail = user.getString("detail");
                    String city = user.getString("city");
                    String status = user.getString("status");
                    String created_at = user.getString("created_at");

                    obj.setId(id);
                    obj.setUid(user_id);
                    obj.setName(user_name);
                    obj.setTitle(title);
                    obj.setDetail(detail);
                    obj.setCity(city);
                    obj.setStatus(status);
                    obj.setDate(created_at);
                    newsList.add(obj);
                    adapter.notifyDataSetChanged();
                    Log.d(TAG, "Get User is working");
                    //sendNewsToDB(obj);
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
   /* private void prepareMovieData() {
        News movie = new News("Mad Max: Fury Road", "Action & Adventure", "12-10-2015");
        newsList.add(movie);

        movie = new News("Inside Out", "Animation, Kids & Family", "2015");
        newsList.add(movie);

        movie = new News("Star Wars: Episode VII - The Force Awakens", "Action", "2015");
        newsList.add(movie);

        movie = new News("Shaun the Sheep", "Animation", "2015");
        newsList.add(movie);

        movie = new News("The Martian", "Science Fiction & Fantasy", "2015");
        newsList.add(movie);

        movie = new News("Mission: Impossible Rogue Nation", "Action", "2015");
        newsList.add(movie);

        movie = new News("Up", "Animation", "2009");
        newsList.add(movie);

        movie = new News("Star Trek", "Science Fiction", "2009");
        newsList.add(movie);

        movie = new News("The LEGO Movie", "Animation", "2014");
        newsList.add(movie);

        movie = new News("Iron Man", "Action & Adventure", "2008");
        newsList.add(movie);

        movie = new News("Aliens", "Science Fiction", "1986");
        newsList.add(movie);

        movie = new News("Chicken Run", "Animation", "2000");
        newsList.add(movie);

        movie = new News("Back to the Future", "Science Fiction", "1985");
        newsList.add(movie);

        movie = new News("Raiders of the Lost Ark", "Action & Adventure", "1981");
        newsList.add(movie);

        movie = new News("Goldfinger", "Action & Adventure", "1965");
        newsList.add(movie);

        movie = new News("Guardians of the Galaxy", "Science Fiction & Fantasy", "2014");
        newsList.add(movie);

        nAdapter.notifyDataSetChanged();
    }*/

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
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
