package com.ascba.rebate.bean;

/**
 * Created by Administrator on 2018/1/31.
 */
public class ShopTypeDet extends STBase {
    @Override
    public int getSpan() {
        return 1;
    }

    @Override
    public int getItemType() {
        return 1;
    }
    private String name;
    private String image;
    public ShopTypeDet(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
