package com.jakester.todolistchallenge.model.objects;

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
    private ToDoColor mColor;
    private SharedPreferences mPrefs;
    private Context mContext;

    public static UserSettings getInstance(Context pContext){
        return mInstance != null ? mInstance : new UserSettings(pContext);
    }

    public UserSettings(Context pContext){
        this.mContext = pContext;
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

    public void setUserColor(int position, ToDoColor pDatColor){
        this.mColor = pDatColor;
        this.mLightColor = pDatColor.getLightColor();
        this.mBaseColor = pDatColor.getBaseColor();
        this.mDarkColor = pDatColor.getDarkColor();
        colorPosition = position;
        mPrefs.edit().putString("light_color", pDatColor.getLightColor()).commit();
        mPrefs.edit().putString("base_color", pDatColor.getBaseColor()).commit();
        mPrefs.edit().putString("dark_color", pDatColor.getDarkColor()).commit();
        mPrefs.edit().putInt("color_position", position).commit();
    }

    public String getLightColor(){
        return mLightColor;
    }

    public String getBaseColor(){
        return mBaseColor;
    }

    public String getDarkColor(){
        return mDarkColor;
    }

    public int getColorPosition() {return colorPosition;}

    public void setImageName(int position, String image){
        this.mImageName = image;
        this.imagePosition = position;
        mPrefs.edit().putString("image_name", image).commit();
        mPrefs.edit().putInt("image_position", position).commit();
    }

    public String getImageName() {return mImageName;};

    public int getImagePosition() {return imagePosition;}


}
