package com.jakester.todolistchallenge.database;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.jakester.todolistchallenge.entities.UserList;

import java.util.ArrayList;

/**
 * Created by Jake on 8/22/2017.
 */

public class DatabaseManager {
    /* This class is meant to be the connection between the app and the database helper
     * to help manage calls to update, delete, or add lists to the database
     */

    private static DatabaseManager mInstance;
    private static ToDoListsDatabaseHelper mDatabaseHelper;

    public static synchronized void initializeInstance(ToDoListsDatabaseHelper helper) {
        if (mInstance == null) {
            mInstance = new DatabaseManager();
            mDatabaseHelper = helper;
        }
    }

    public static synchronized DatabaseManager getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName() +
                    " is not initialized, call initialize(..) method first.");
        }
        return mInstance;
    }

    public ArrayList<UserList> getLists(){
        return mDatabaseHelper.getListsFromDataBase();
    }

    public int addList(UserList list){
        return ((int)mDatabaseHelper.addList(list));
    }

    public void updateLists(ArrayList<UserList> lists){
        for(UserList datList: lists){
            if(datList.getUpdated()){
                mDatabaseHelper.updateList(datList);
                datList.setUpdated(false);
            }
        }
    }

    public void updateList(UserList list){
        mDatabaseHelper.updateList(list);
        list.setUpdated(false);
    }

    public void deleteList(UserList list){
        mDatabaseHelper.deleteList(list);
    }

}
