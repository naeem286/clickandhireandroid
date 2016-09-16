package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 8/1/2016.
 */
public class ModelSubCategory {
    public String subCatId="";
    public String catId="";
    public String catName="";

    public String getSubCatId() {
        return subCatId;
    }
    public void setSubCatId(String id)
    {
        this.subCatId = id;
    }
    public String getId() {
        return catId;
    }


    public void setId(String id) {
        this.catId = id;
    }

    public String getName() {
        return catName;
    }

    public void setName(String name) {
        this.catName = name;
    }

}
