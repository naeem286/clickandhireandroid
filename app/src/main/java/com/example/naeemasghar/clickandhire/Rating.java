package com.example.naeemasghar.clickandhire;

/**
 * Created by Naeem Asghar on 6/29/2016.
 */
public class Rating {

    private int image;
    private String name, rating, stars ,date;
    public Rating(int image,String name, String rating,String stars, String date)
    {
        this.setImage(image);
        this.setName(name);
        this.setRating(rating);
        this.setStars(stars);
        this.setDate(date);
    }

    public int getImage()
    {
        return image;
    }
    public void setImage(int image)
    {
        this.image=image;
    }
    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }
    public String getRating()
    {
        return rating;
    }
    public void setRating(String rating)
    {
        this.rating=rating;
    }
    public String getStars()
    {
        return rating;
    }
    public void setStars(String stars)
    {
        this.stars=stars;
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
