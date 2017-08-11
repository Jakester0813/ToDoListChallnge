package com.jakester.todolistchallenge.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jakester.todolistchallenge.model.objects.Item;
import com.jakester.todolistchallenge.model.objects.UserList;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jake on 7/23/2017.
 */

public class ToDoListsDatabaseHelper extends SQLiteAssetHelper {

    private static ToDoListsDatabaseHelper mInstance;

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

    public static synchronized ToDoListsDatabaseHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new ToDoListsDatabaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    private ToDoListsDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ArrayList<UserList> getListsFromDataBase(){
        ArrayList<UserList> lists = null;
        Cursor cursor = readDatabase();
        // Reads the database for all recipes and adds it to the list of recipes.
        try {
            if (cursor.moveToFirst()) {
                do {
                    if (lists == null) {
                        lists = new ArrayList<>();
                    }

                    UserList list = getList(cursor);
                    lists.add(list);

                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("Tag", "An error occurred while trying to get To Do Lists from database: " + e.getLocalizedMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return lists;
    }

    private UserList getList(Cursor cursor) {

        int idInt = cursor.getInt(cursor.getColumnIndex(KEY_ROW_ID));
        String nameString = cursor.getString(cursor.getColumnIndex(KEY_LIST_NAME));

        UserList userList = new UserList(idInt, nameString);

        try {
            userList = parseListItems(userList, cursor);
        }
        catch (JSONException e){
            Log.d("ParseListItems", "Something went wrong on trying to fetch these items");
        }

        return userList;
    }

    private UserList parseListItems(UserList list, Cursor cursor)throws JSONException{

        ArrayList<Item> currentItems = new ArrayList<Item>();
        ArrayList<Item> completedItems = new ArrayList<Item>();

        JSONObject listObject = new JSONObject(cursor.getString(cursor.getColumnIndex(KEY_ITEMS)));
        JSONArray currentArray = listObject.getJSONArray("Current List");
        JSONArray completedArray = listObject.getJSONArray("Completed List");
        JSONObject object = null;
        Item item = null;
        String itemName = null;
        for (int i = 0; i < currentArray.length(); i++){
            object = currentArray.getJSONObject(i);
            itemName = object.getString("Name");
            item = new Item(itemName);
            item.setNote(object.getString("Note"));
            item.setAddedDate(object.getString("Added Date"));
            item.setCompleted(false);
            item.setDueDate(object.getString("Date To Complete"));
            item.setPriority(object.getInt("Priority"));
            currentItems.add(item);
        }
        list.setCurrentItems(currentItems);

        for (int i = 0; i < completedArray.length(); i++){
            object = completedArray.getJSONObject(i);
            itemName = object.getString("Name");
            item = new Item(itemName);
            item.setNote(object.getString("Note"));
            item.setAddedDate(object.getString("Added Date"));
            item.setCompleted(true);
            item.setDueDate(object.getString("Date To Complete"));
            item.setPriority(object.getInt("Priority"));
            completedItems.add(item);
        }
        list.setCompletedItems(completedItems);

        return list;
    }

    // Insert or update a list in the database
    // Since SQLite doesn't support "upsert" we need to fall back on an attempt to UPDATE (in case the
    // user already exists) optionally followed by an INSERT (in case the user does not already exist).
    // Unfortunately, there is a bug with the insertOnConflict method
    // (https://code.google.com/p/android/issues/detail?id=13045) so we need to fall back to the more
    // verbose option of querying for the user's primary key if we did an update.
    public long addOrUpdateList(UserList list) {
        // The database connection is cached so it's not expensive to call getWriteableDatabase() multiple times.
        SQLiteDatabase db = getWritableDatabase();
        long userId = -1;

        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_LIST_NAME, list.getName());
            values.put(KEY_ITEMS, convertListsIntoString(list));

            // First try to update the user in case the user already exists in the database
            // This assumes userNames are unique
            int rows = db.update(TABLE_NAME, values, KEY_LIST_NAME + "= ?", new String[]{list.getName()});

            // Check if update succeeded
            if (rows == 1) {
                // Get the primary key of the user we just updated
                String usersSelectQuery = String.format("SELECT %s FROM %s WHERE %s = ?",
                        KEY_ROW_ID , TABLE_NAME, KEY_LIST_NAME);
                Cursor cursor = db.rawQuery(usersSelectQuery, new String[]{String.valueOf(list.getName())});
                try {
                    if (cursor.moveToFirst()) {
                        userId = cursor.getInt(0);
                        db.setTransactionSuccessful();
                    }
                } finally {
                    if (cursor != null && !cursor.isClosed()) {
                        cursor.close();
                    }
                }
            } else {
                // this list did not already exist, so insert new list
                userId = db.insertOrThrow(TABLE_NAME, null, values);
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            Log.d("DB Error", "Error while trying to add or update user");
        } finally {
            db.endTransaction();
        }
        return userId;
    }

    private String convertListsIntoString(UserList list) throws JSONException{
        JSONObject listObject = new JSONObject();
        JSONArray currentItemsArray = new JSONArray();
        JSONArray doneItemsArray = new JSONArray();
        JSONObject itemObject;

        ArrayList<Item> current = list.getCurrentItems();
        ArrayList<Item> completed = list.getCompletedItems();
        StringBuilder string;
        for(Item item : current){
            itemObject = new JSONObject();
            itemObject.put("Name", item.getName());
            itemObject.put("Note", item.getNote());
            itemObject.put("Added Date", item.getAddedDate());
            itemObject.put("Date To Complete", item.getDueDate());
            itemObject.put("Priority", item.getPriority());
            currentItemsArray.put(itemObject);
        }

        for(Item item : completed){
            itemObject = new JSONObject();
            itemObject.put("Name", item.getName());
            itemObject.put("Note", item.getNote());
            itemObject.put("Added Date", item.getAddedDate());
            itemObject.put("Date To Complete", item.getDueDate());
            itemObject.put("Priority", item.getPriority());
            doneItemsArray.put(itemObject);
        }

        listObject.put("Current List", currentItemsArray);

        listObject.put("Completed List", doneItemsArray);

        return listObject.toString();
    }

    private Cursor readDatabase() {

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under
        // low disk space scenarios)
        SQLiteDatabase database = getReadableDatabase();

        // Prepares the cursor.
        return database.query(TABLE_NAME, TABLE_ALL_COLUMNS, null, null, null, null, null);
    }
}
