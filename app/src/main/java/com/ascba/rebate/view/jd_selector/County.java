package com.ascba.rebate.view.jd_selector;

import java.util.List;

public class County {
    private int id;
    private String name;
    private List<Street> street;

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

    public List<Street> getStreet() {
        return street;
    }

    public void setStreet(List<Street> street) {
        this.street = street;
    }
}
