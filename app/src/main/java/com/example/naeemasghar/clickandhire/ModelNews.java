package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 5/28/2016.
 */
public class ModelNews {
    public String id="";
    public String uid="";
    public String name = "";
    public String title = "";
    public String detail = "";
    public String city="";
    public String status="";
    public String date = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUid() {
        return uid;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title=title;
    }
    public String getDetail()
    {
        return detail;
    }
    public void setDetail(String detail)
    {
        this.detail=detail;
    }

    public void setCity(String city)
    {
        this.city=city;
    }
    public String getCity()
    {
        return city;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status=status;
    }
    public String getDate()
    {
        return date;
    }
    public void setDate(String date)
    {
        this.date=date;
    }
}
