package com.jakester.todolistchallenge.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;


import com.jakester.todolistchallenge.entities.ToDoColor;
import com.jakester.todolistchallenge.entities.UserSettings;

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

    public static ColorUtil getInstance(){
        if(mInstance == null){
            mInstance = new ColorUtil();
        }
        return mInstance;
    }

    private ColorUtil(){
        mColors = null;
    }

    //Obtains the json file containing all of the colors to decorate the app
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

    //Sets colors from colors.json
    public void setColors(ArrayList<ToDoColor> pColors){
        this.mColors = pColors;
    }

    //Gets colors to store for Settings
    public ArrayList<ToDoColor> getColors(){
        return mColors;
    }

    //Sets the status bar color
    public void setStatusBarColor(Activity activity){
        String color = UserSettings.getInstance(activity).getDarkColor();
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor(color));
    }
}
