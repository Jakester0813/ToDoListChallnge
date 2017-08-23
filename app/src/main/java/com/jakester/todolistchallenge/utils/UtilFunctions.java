package com.jakester.todolistchallenge.utils;

import android.content.Context;
import android.content.SharedPreferences;


import com.jakester.todolistchallenge.constants.ToDoConstants;

/**
 * Created by Jake on 8/17/2017.
 */

public class UtilFunctions {

    private static UtilFunctions mInstance;
    private SharedPreferences mPrefs;

    public static UtilFunctions getInstance(Context pContext){
        return mInstance != null ? mInstance : new UtilFunctions(pContext);
    }

    private UtilFunctions(Context pContext){
        this.mPrefs = pContext.getSharedPreferences(ToDoConstants.TO_DO_PREFS, Context.MODE_PRIVATE);
    }

    //Sets first app launch to initialize SQL
    public void setFirstAppLaunchCommited(){
        mPrefs.edit().putBoolean("first_launch", true).commit();
    }

    //Checks first app launch to initialize SQL
    public boolean getFirstAppLaunch(){
        return mPrefs.getBoolean("first_launch", false);
    }

    //Sets last row stored in SQL
    public void setLastRowID(int id){
        mPrefs.edit().putInt("last_row_id", id).commit();
    }

    //Gets last row stored in SQL
    public int getLastRowID(){
        return mPrefs.getInt("last_row_id", 0);
    }

}
