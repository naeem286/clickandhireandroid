package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 6/12/2016.
 */
public class Professional {

    private String Professional_ID;
    private String Professional_Name;
    private String Professional_Speciality;
    private String Professional_Fees;


    public Professional(String id,String name, String speciality, String fee) {
        this.setProfessional_ID(id);
        this.setProfessional_Name(name);
        this.setProfessional_Speciality(speciality);
        this.setProfessional_Fees(fee);
    }

    public  String getProfessional_ID() {
        return Professional_ID;
    }

    public void setProfessional_ID(String id) {
        this.Professional_ID = id;
    }
    public  String getProfessional_Name() {
        return Professional_Name;
    }

    public void setProfessional_Name(String name) {
        this.Professional_Name = name;
    }
    public String getProfessional_Speciality() {

        return Professional_Speciality;
    }

    public void setProfessional_Speciality(String speciality) {
        this.Professional_Speciality = speciality;
    }

    public String getProfessional_Fees() {
        return Professional_Fees;
    }

    public void setProfessional_Fees(String fees) {
        this.Professional_Fees = fees;
    }
}
