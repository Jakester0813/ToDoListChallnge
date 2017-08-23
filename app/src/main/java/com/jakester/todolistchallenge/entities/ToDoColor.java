package com.jakester.todolistchallenge.entities;

/**
 * Created by Jake on 8/21/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ToDoColor {

    //POJO class for mapping color values from colors.json file

    @SerializedName("name")
    @Expose
    private String mName;
    @SerializedName("light_color")
    @Expose
    private String mLightColor;
    @SerializedName("base_color")
    @Expose
    private String mBaseColor;
    @SerializedName("dark_color")
    @Expose
    private String mDarkColor;

    public ToDoColor(String pName, String pLight, String pBase, String pDark){
        this.mName = pName;
        this.mLightColor = pLight;
        this.mBaseColor = pBase;
        this.mDarkColor = pDark;
    }

    public String getName(){
        return this.mName;
    }

    public void setName(String pName){
        this.mName = pName;
    }

    public void setLightColor(String pLight){
        this.mLightColor = pLight;
    }

    public String getLightColor(){
        return mLightColor;
    }

    public void setBaseColor(String pBase){
        this.mBaseColor = pBase;
    }

    public String getBaseColor(){
        return mBaseColor;
    }

    public void setDarkColor(String pDark){
        this.mDarkColor = pDark;
    }

    public String getDarkColor(){
        return mDarkColor;
    }
}
