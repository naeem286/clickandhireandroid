package com.example.naeemasghar.clickandhire;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;




/**
 * Created by Naeem Asghar on 8/12/2016.
 */
public class FragmentMyPostWork  extends Fragment {
    private static final String TAG = MainActivity.class.getSimpleName();
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_post_work, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        // SqLite database handler
        db = new SQLiteHandler(getActivity());
        HashMap<String, String> user = db.getUserDetails();
        UserId = user.get("id");
        getTaskByUserId();


        wAdapter = new AdapterWork(workList);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerNewsDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(wAdapter);
        registerForContextMenu(recyclerView);

        String cityName=db.getCityById(Integer.toString(spf_City));
        String catName=db.getSubCategoryById(Integer.toString(spf_subCatId));

        recyclerView.addOnItemTouchListener(new ActivityWorkList.RecyclerTouchListener(getActivity().getApplicationContext(), recyclerView, new ActivityWorkList.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                ModelTask work = workList.get(position);
                //Toast.makeText(getApplicationContext(), work.getTitle() + " is selected!", Toast.LENGTH_SHORT).show();
                // Launch Login Activity
                Intent intent = new Intent(getActivity(), ActivityMyWorkDetail.class);
                intent.putExtra("uid", Id);
                intent.putExtra("wid", work.getId());
                intent.putExtra("title", work.getTitle());
                intent.putExtra("detail", work.getDetail());
                intent.putExtra("fee", work.getFees());
                intent.putExtra("city", work.getCity());
                intent.putExtra("awardBid", work.getAwarded_bid());
                intent.putExtra("awardName", work.getAwarded_name());
                intent.putExtra("status", work.getStatus());
                intent.putExtra("date", work.getDate());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

                View menuItemView = getView().findViewById(R.id.recycler_view);
                PopupMenu popupMenu = new PopupMenu(getActivity(), menuItemView);
                popupMenu.getMenuInflater().inflate(R.menu.menu_work, popupMenu.getMenu());
            }
        }));
        return v;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
       // Toast.makeText(getActivity().getApplicationContext(), "My post  work fragment view created", Toast.LENGTH_SHORT).show();
        super.onViewCreated(view, savedInstanceState);
    }

    // Method to make request to server
    private void getTaskByUserId(){
        class GetUserTasks extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(),"Searching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                getTasks(s);
                loading.dismiss();
                //Toast.makeText(getActivity(),s,Toast.LENGTH_LONG).show();
               // Toast.makeText(getActivity(), "Search is done Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("user_id",UserId);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_TASKS_BY_USER_ID ,hashMap);
                return s;
            }
        }

        GetUserTasks cul = new GetUserTasks();
        cul.execute();
    }
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
                Toast.makeText(getActivity(),
                        errorMsg, Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //if (v.getId()==R.id.recycler_view) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            menu.setHeaderTitle("Work Menu");
            String[] menuItems = getResources().getStringArray(R.array.professional_name);
            for (int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        //}
    }
}
