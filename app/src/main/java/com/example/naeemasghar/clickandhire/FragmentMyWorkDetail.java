package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.HashMap;

/**
 * Created by Naeem Asghar on 8/12/2016.
 */
public class FragmentMyWorkDetail extends Fragment {
    public EditText etTitle, etDetail, etFee;
    public TextView txtDate;
    public Button btnUpdate;
    private String Uid,Wid,Status;
    public ToggleButton btnStatus;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_my_work_detail, container, false);
        etTitle = (EditText)  v.findViewById(R.id.title);
        etDetail = (EditText) v.findViewById(R.id.detail);
        etFee = (EditText) v.findViewById(R.id.fee);
        txtDate = (TextView)   v.findViewById(R.id.date);
        btnUpdate = (Button)   v.findViewById(R.id.btnUpdate);
        //btnStatus=(ToggleButton)v.findViewById(R.id.btnStatus);
        //String str = getActivity().getIntent().getStringExtra("myString");
        Uid=getActivity().getIntent().getStringExtra("uid");
        Wid=getActivity().getIntent().getStringExtra("wid");
        etTitle.setText(getActivity().getIntent().getStringExtra("title"));
        etDetail.setText(getActivity().getIntent().getStringExtra("detail"));
        etFee.setText(getActivity().getIntent().getStringExtra("fee"));
        txtDate.setText(getActivity().getIntent().getStringExtra("date"));
        Status=getActivity().getIntent().getStringExtra("status");
       // Toast.makeText(getActivity().getApplicationContext(),"Status="+Status, Toast.LENGTH_LONG).show();
       /* if(Status.equals("1")) {
            btnStatus.setChecked(true);
        }else {
            btnStatus.setChecked(false);
        }*/
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String title= etTitle.getText().toString().trim();
                String detail= etDetail.getText().toString().trim();
                String fee= etFee.getText().toString().trim();
                // Check for empty data in the form
                if (!title.isEmpty() && !detail.isEmpty()&& !fee.isEmpty()) {
                    // login user
                    postUserTask(title, detail, fee);

                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getActivity().getApplicationContext(),
                            "Please enter the all credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });

       /* btnStatus.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                /*if (btnStatus.isChecked()) {
                    Status="1";
                    //Toast.makeText(getActivity(),"Activated", Toast.LENGTH_SHORT).show();
                } else {
                    Status="0";
                    //Toast.makeText(getActivity(), "Deactivated", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        return v;
    }
    /* Post Task to Sever*/
    private void postUserTask(final String title,final String detail, final String fees){


        class UpdateTasks extends AsyncTask<Void,Void,String> {
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Posting..", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getActivity().getApplicationContext(), "Task is Updated Successfully", Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("task_id",Wid);
                hashMap.put("title",title);
                hashMap.put("detail",detail);
                hashMap.put("fees", fees);
                RequestHandler rh = new RequestHandler();

                String s = rh.sendPostRequest(AppConfig.URL_TASKS_UPDATE,hashMap);

                return s;
            }
        }

        UpdateTasks ue = new UpdateTasks();
        ue.execute();
    }

}