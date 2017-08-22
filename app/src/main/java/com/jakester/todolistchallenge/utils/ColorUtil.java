package com.jakester.todolistchallenge.utils;

import android.content.Context;


import com.jakester.todolistchallenge.entities.ToDoColor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jake on 8/21/2017.
 */

public class ColorUtil {
    private static ColorUtil mInstance;
    private ArrayList<ToDoColor> mColors;
    private HashMap<String, ToDoColor> mColorsMap;

    public static ColorUtil getInstance(){
        if(mInstance == null){
            mInstance = new ColorUtil();
        }
        return mInstance;
    }

    private ColorUtil(){
        mColors = null;
    }

    public String loadJSONFromAsset(Context mContext) {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("colors.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public void setColors(ArrayList<ToDoColor> pColors){
        this.mColors = pColors;
        mColorsMap = new HashMap<String, ToDoColor>();
        for(ToDoColor color : mColors){
            mColorsMap.put(color.getName(), color);
        }
    }

    public ArrayList<ToDoColor> getColors(){
        return mColors;
    }

    public ToDoColor retrieveColor(String colorName){
        return mColorsMap.get(colorName);
    }
}
