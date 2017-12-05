package com.ascba.rebate.view.jd_selector;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class City {
    private int id;
    private String name;
    @SerializedName("district")
    private List<County> county;

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

    public List<County> getCounty() {
        return county;
    }

    public void setCounty(List<County> county) {
        this.county = county;
    }
}
