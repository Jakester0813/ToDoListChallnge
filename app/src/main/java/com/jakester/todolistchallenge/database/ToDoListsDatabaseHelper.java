package com.jakester.todolistchallenge.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.jakester.todolistchallenge.database.ToDoListContract;
import com.jakester.todolistchallenge.entities.Item;
import com.jakester.todolistchallenge.entities.UserList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.jakester.todolistchallenge.database.ToDoListContract.ListEntry.*;

/**
 * Created by Jake on 7/23/2017.
 */

public class ToDoListsDatabaseHelper extends SQLiteOpenHelper {

    private static ToDoListsDatabaseHelper mInstance;

    private static final String DATABASE_NAME = "to_do_table.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ROW_ID + " INTEGER PRIMARY KEY," +
                    KEY_LIST_NAME + " TEXT," +
                    KEY_ITEMS + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

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
        int idInt = cursor.getColumnIndexOrThrow(KEY_ROW_ID);
        String nameString = cursor.getString(cursor.getColumnIndex(KEY_LIST_NAME));

        UserList userList = new UserList((int)idInt, nameString);

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
        int id = -1;
        String itemName = null;
        boolean showCompleted;

        list.setID(listObject.getInt("listRowId"));

        list.setShowCompleted(listObject.getBoolean("showCompleted"));

        for (int i = 0; i < currentArray.length(); i++){
            object = currentArray.getJSONObject(i);
            itemName = object.getString("Name");
            item = new Item(itemName);
            item.setNote(object.getString("Note"));
            item.setAddedDate(object.getString("Added Date"));
            item.setCompleted(false);
            item.setDueDate(object.getString("Date To Complete"));
            item.setPriority(object.getString("Priority"));
            item.setPriorityColor(object.getInt("Priority Color"));
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
            item.setPriority(object.getString("Priority"));
            item.setPriorityColor(object.getInt("Priority Color"));
            completedItems.add(item);
        }
        list.setCompletedItems(completedItems);

        return list;
    }

    public long addList(UserList list){
        // Gets the data repository in write mode
        long newRowId = -1;

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
// Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(KEY_ROW_ID, list.getID());
            values.put(KEY_LIST_NAME, list.getName());
            values.put(KEY_ITEMS, convertListsIntoString(list));

// Insert the new row, returning the primary key value of the new row
            newRowId = db.insertOrThrow(TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            Log.d("Error adding list", "See stacktrace");
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }

        return newRowId;
    }

    public void updateList(UserList list){

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
// Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(KEY_ROW_ID, list.getID());
            values.put(KEY_LIST_NAME, list.getName());
            values.put(KEY_ITEMS, convertListsIntoString(list));

// Insert the new row, returning the primary key value of the new row
            int rows = db.update(TABLE_NAME, values, KEY_ROW_ID + "=?", new String[] { String.valueOf(list.getID()) });
            db.setTransactionSuccessful();
        }
        catch (Exception e) {
            Log.d("Error updating list", "See stacktrace");
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void deleteList(UserList datList) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        int num = db.delete(TABLE_NAME, KEY_ROW_ID + " = ?",
                new String[] { String.valueOf(datList.getID()) });
        Log.d("Delete list", Integer.toString(num));
        db.setTransactionSuccessful();
        db.endTransaction();
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
            itemObject.put("Priority Color", item.getPriorityColor());
            currentItemsArray.put(itemObject);
        }

        for(Item item : completed){
            itemObject = new JSONObject();
            itemObject.put("Name", item.getName());
            itemObject.put("Note", item.getNote());
            itemObject.put("Added Date", item.getAddedDate());
            itemObject.put("Date To Complete", item.getDueDate());
            itemObject.put("Priority", item.getPriority());
            itemObject.put("Priority Color", item.getPriorityColor());
            doneItemsArray.put(itemObject);
        }

        listObject.put("listRowId",list.getID());

        listObject.put("showCompleted",list.getShowCompleted());

        listObject.put("Current List", currentItemsArray);

        listObject.put("Completed List", doneItemsArray);

        return listObject.toString();
    }

    private Cursor readDatabase() {

        // "getReadableDatabase()" and "getWriteableDatabase()" return the same object (except under
        // low disk space scenarios)
        SQLiteDatabase database = getReadableDatabase();

        // Prepares the cursor.
        Cursor cursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        Log.d("Count", Integer.toString(cursor.getCount()));
        return cursor;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}
