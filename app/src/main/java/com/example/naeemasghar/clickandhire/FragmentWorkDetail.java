package com.example.naeemasghar.clickandhire;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Naeem Asghar on 8/1/2016.
 */
public class FragmentWorkDetail extends Fragment {

    public TextView title, date, detail,name;
    public ImageView profile;
    private String Uid,Wid;
    public FragmentWorkDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.work_fragment_detail, container, false);
        title = (TextView)  v.findViewById(R.id.title);
        detail = (TextView) v.findViewById(R.id.detail);
        date = (TextView)   v.findViewById(R.id.date);
        name = (TextView)   v.findViewById(R.id.name);
        profile=(ImageView) v.findViewById(R.id.image);

        //String str = getActivity().getIntent().getStringExtra("myString");
        Uid=getActivity().getIntent().getStringExtra("uid");
        Wid=getActivity().getIntent().getStringExtra("wid");
        title.setText(getActivity().getIntent().getStringExtra("title"));
        detail.setText(getActivity().getIntent().getStringExtra("detail"));
        String dateText= getActivity().getIntent().getStringExtra("date");
        date.setText(dateText.substring(0, Math.min(dateText.length(), 10)));

        name.setText(getActivity().getIntent().getStringExtra("userName"));
       // String urlForImage = AppConfig.URL_For_Image + Uid+".jpg";
        new DownloadImage(profile).execute(Uid);
        return v;
    }

}
