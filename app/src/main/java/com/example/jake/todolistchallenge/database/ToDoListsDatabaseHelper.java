package com.example.jake.todolistchallenge.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Jake on 7/23/2017.
 */

public class ToDoListsDatabaseHelper extends SQLiteAssetHelper {
    public ToDoListsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
}
