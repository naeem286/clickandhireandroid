package com.example.naeemasghar.clickandhire;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


/**
 * Created by Naeem Asghar on 8/1/2016.
 */
public class FragmentProfessionalDetail extends Fragment {

    private ImageView profileImage;
    private TextView txtName;
    private RatingBar ratingBar;
    private TextView txtCity;
    private TextView txtRating;
    private TextView txtTotalRatings;
    private TextView txtTagLine;
    private TextView txtSummary;
    private TextView txtSpeciality;
    private TextView txtExperience;
    private TextView txtQualification;
    private TextView txtFee;
    private TextView txtPhone;
    private TextView txtEmail;
    private ImageView star1;
    private ImageView star2;
    private ImageView star3;
    private ImageView star4;
    private ImageView star5;
    public ImageLoader imageLoader;
    public FragmentProfessionalDetail() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ProgressDialog loading;
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.professional_fragment_detail, container, false);
        imageLoader = new ImageLoader(getActivity().getApplicationContext());
        //loading = ProgressDialog.show(getActivity(),"Loading...","Wait...",false,false);

        profileImage= (ImageView)v.findViewById(R.id.profile_image);
        txtName= (TextView) v.findViewById(R.id.username);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        txtCity= (TextView) v.findViewById(R.id.city);
        txtRating= (TextView)v.findViewById(R.id.rating);
        txtTotalRatings= (TextView)v.findViewById(R.id.total_rating);
        txtTagLine= (TextView) v.findViewById(R.id.tag_line);
        txtSummary= (TextView)v.findViewById(R.id.description);
        txtSpeciality= (TextView)v.findViewById(R.id.speciality);
        txtExperience= (TextView)v.findViewById(R.id.experience);
        txtFee= (TextView)v.findViewById(R.id.fees);
        txtPhone= (TextView)v.findViewById(R.id.phone);
        txtEmail= (TextView)v.findViewById(R.id.email);


        txtName.setText(getActivity().getIntent().getStringExtra("name"));
        String cid= getActivity().getIntent().getStringExtra("city");
        txtCity.setText(cid+", Pakistan");
        txtTagLine.setText(getActivity().getIntent().getStringExtra("tag_line"));
        txtSummary.setText(getActivity().getIntent().getStringExtra("summary"));
        txtSpeciality.setText(getActivity().getIntent().getStringExtra("speciality"));
        txtExperience.setText(getActivity().getIntent().getStringExtra("experience")+" Years");
        txtFee.setText(getActivity().getIntent().getStringExtra("fee")+" per Hour");
        txtPhone.setText(getActivity().getIntent().getStringExtra("phone"));
        txtEmail.setText(getActivity().getIntent().getStringExtra("email"));
        String rating= getActivity().getIntent().getStringExtra("rating");
        String totalRating= getActivity().getIntent().getStringExtra("totalRating");


        //ratingBar.setRating(Math.round(Float.parseFloat(rating)));
        ratingBar.setRating(Float.parseFloat(rating));
        txtRating.setText(String.valueOf(Math.round(Float.parseFloat(rating))+".0"));
        txtTotalRatings.setText("("+String.valueOf(totalRating)+")");

        String id =getActivity().getIntent().getStringExtra("id");
        String urlForImage = AppConfig.URL_For_Image + id+".jpg";
       /* new DownloadImage(profileImage).execute(id);
        loading.dismiss();*/

        imageLoader.DisplayImage(urlForImage, profileImage);
        return v;
    }



}




