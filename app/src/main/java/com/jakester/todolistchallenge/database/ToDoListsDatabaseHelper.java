package com.jakester.todolistchallenge.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.HashSet;

/**
 * Created by Jake on 7/23/2017.
 */

public class ToDoListsDatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "to_do_table.db";
    private static final int DATABASE_VERSION = 1;

    // Defines the table schema.
    public static final String TABLE_NAME = "TO_DO_LISTS_TABLE";
    public static final String KEY_ROW_ID = "KEY_ROW_ID";
    public static final String KEY_LIST_NAME = "KEY_LIST_NAME";
    public static final String KEY_ITEMS = "KEY_ITEMS";

    private static final String[] TABLE_ALL_COLUMNS = {
            TABLE_NAME,
            KEY_ROW_ID,
            KEY_LIST_NAME,
            KEY_ITEMS
    };

    private ToDoListsDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /*public synchronized HashSet<String> getAllLists() {
        HashSet<String> ingredientList = null;
        Cursor cursor = readDatabase();

        // Reads the database for all recipes and adds it to the list of recipes.
        try {
            if (cursor.moveToFirst()) {
                do {
                    if (ingredientList == null) {
                        ingredientList = new HashSet<>();
                    }

                    ingredientList.add(cursor.getString(cursor.getColumnIndex(KEY_REC1)));
                    ingredientList.add(cursor.getString(cursor.getColumnIndex(KEY_REC2)));
                    ingredientList.add(cursor.getString(cursor.getColumnIndex(KEY_REC3)));

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "loadAllIngredients(): An error occurred while attempting to query the database for ingredients: " + e.getLocalizedMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

            if (ingredientList != null) {
                ingredientList.remove(AlchenomiconConstants.NULL_IDENTIFIER); // Removes any NULL ingredients from list.
            }
        }

        return ingredientList;
    }*/

    private Cursor readDatabase() {

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under
        // low disk space scenarios)
        SQLiteDatabase database = getReadableDatabase();

        // Prepares the cursor.
        return database.query(TABLE_NAME, TABLE_ALL_COLUMNS, null, null, null, null, null);
    }
}
