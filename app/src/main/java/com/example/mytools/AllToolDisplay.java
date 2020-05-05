package com.example.mytools;

public class AllToolDisplay {

    private int id;
    private String name;
    private String imageURL;
    private String date;
    private Boolean availability;


    public AllToolDisplay(int id, String name, String imageURL, String date, Boolean availability) {
        this.id = id;
        this.name = name;
        this.imageURL = imageURL;
        this.date = date;
        this.availability = availability;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }
}

