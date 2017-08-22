package com.jakester.todolistchallenge.utils;

import android.app.Activity;
import android.graphics.Color;
import android.view.Window;
import android.view.WindowManager;


import com.jakester.todolistchallenge.model.objects.Item;
import com.jakester.todolistchallenge.model.objects.UserList;
import com.jakester.todolistchallenge.model.objects.UserSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jake on 8/17/2017.
 */

public class UtilFunctions {

    private static UtilFunctions mInstance;

    public static UtilFunctions getInstance(){
        return mInstance != null ? mInstance : new UtilFunctions();
    }

    public void setStatusBarColor(Activity activity){
        String color = UserSettings.getInstance(activity).getDarkColor();
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor(color));
    }

    public String convertListsIntoString(UserList list) throws JSONException {
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
            itemObject.put("PriorityColor", item.getPriorityColor());
            currentItemsArray.put(itemObject);
        }

        for(Item item : completed){
            itemObject = new JSONObject();
            itemObject.put("Name", item.getName());
            itemObject.put("Note", item.getNote());
            itemObject.put("Added Date", item.getAddedDate());
            itemObject.put("Date To Complete", item.getDueDate());
            itemObject.put("Priority", item.getPriority());
            itemObject.put("PriorityColor", item.getPriorityColor());
            doneItemsArray.put(itemObject);
        }

        listObject.put("Current List", currentItemsArray);

        listObject.put("Completed List", doneItemsArray);

        return listObject.toString();
    }

    public UserList parseListItems(UserList list, String data)throws JSONException{

        ArrayList<Item> currentItems = new ArrayList<Item>();
        ArrayList<Item> completedItems = new ArrayList<Item>();

        JSONObject listObject = new JSONObject(data);
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
            item.setPriority(object.getString("Priority"));
            item.setPriorityColor(object.getInt("PriorityColor"));
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
            item.setPriorityColor(object.getInt("PriorityColor"));
            completedItems.add(item);
        }
        list.setCompletedItems(completedItems);

        return list;
    }
}
