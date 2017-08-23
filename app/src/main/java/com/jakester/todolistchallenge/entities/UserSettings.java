package com.jakester.todolistchallenge.entities;

import android.content.Context;
import android.content.SharedPreferences;

import com.jakester.todolistchallenge.constants.ToDoConstants;


/**
 * Created by Jake on 8/21/2017.
 */

public class UserSettings {
    private static UserSettings mInstance;

    private String mImageName, mLightColor, mBaseColor, mDarkColor;
    private int colorPosition, imagePosition;
    private SharedPreferences mPrefs;

    public static UserSettings getInstance(Context pContext){
        return mInstance != null ? mInstance : new UserSettings(pContext);
    }

    //Creates an object with the settings from Settings Screen to decorate the app
    public UserSettings(Context pContext){
        this.mPrefs = pContext.getSharedPreferences(ToDoConstants.TO_DO_PREFS, Context.MODE_PRIVATE);
        this.mImageName = mPrefs.getString("image_name","beach");
        //gets the default colors to apply to the app right away if the user-selected colors
        //are unavailable (which is blue)
        this.mLightColor = mPrefs.getString("light_color","#FD1824");
        this.mBaseColor = mPrefs.getString("base_color","#AC0009");
        this.mDarkColor = mPrefs.getString("dark_color","#720006");
        this.colorPosition = mPrefs.getInt("color_position",0);
        this.imagePosition = mPrefs.getInt("image_position",0);
    }

    //Sets the user-selected color from Settings
    public void setUserColor(int position, ToDoColor pDatColor){
        this.mLightColor = pDatColor.getLightColor();
        this.mBaseColor = pDatColor.getBaseColor();
        this.mDarkColor = pDatColor.getDarkColor();
        colorPosition = position;
        mPrefs.edit().putString("light_color", pDatColor.getLightColor()).commit();
        mPrefs.edit().putString("base_color", pDatColor.getBaseColor()).commit();
        mPrefs.edit().putString("dark_color", pDatColor.getDarkColor()).commit();
        mPrefs.edit().putInt("color_position", position).commit();
    }

    //This gets called for List Activity for coloring the background of new item field and "Show
    //Completed Items" UI
    public String getLightColor(){
        return mLightColor;
    }

    //This gets called for setting Toolbar color
    public String getBaseColor(){
        return mBaseColor;
    }

    //This gets called for setting Status Bar color
    public String getDarkColor(){
        return mDarkColor;
    }

    public int getColorPosition() {return colorPosition;}

    //Saves Background image specified by player for future selection
    public void setImageName(int position, String image){
        this.mImageName = image;
        this.imagePosition = position;
        mPrefs.edit().putString("image_name", image).commit();
        mPrefs.edit().putInt("image_position", position).commit();
    }

    public String getImageName() {return mImageName;};

    public int getImagePosition() {return imagePosition;}


}
