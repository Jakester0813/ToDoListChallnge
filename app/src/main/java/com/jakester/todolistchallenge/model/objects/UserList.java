package com.jakester.todolistchallenge.model.objects;

import java.util.ArrayList;

/**
 * Created by Jake on 8/2/2017.
 */

public class UserList {
    private int mIDInt;
    private String mNameString;
    private ArrayList<Item> mCurrentItems;
    private ArrayList<Item> mCompletedItems;

    //Set to initialize item by name, set other string fields to empty, and set
    //item to lowest priority (2 = low, 1 = medium, 0 = high)
    public UserList(int pID, String pName){
        this.mIDInt = pID;
        this.mNameString = pName;
        this.mCurrentItems = new ArrayList<Item>();
        this.mCompletedItems = new ArrayList<Item>();
    }

    /*These setter methods are used for only modifying fields of this class, as the item is only
     *created after the user sets the name by default.
     */

    public int getID(){
        return mIDInt;
    }

    public void setName(String pName){
        this.mNameString = pName;
    }

    public String getName(){
        return mNameString;
    }

    public void addCurrentItem(Item newItem){
        mCurrentItems.add(newItem);
    }

    public ArrayList<Item> getCurrentItems(){
        return mCurrentItems;
    }

    public void setCurrentItems(ArrayList<Item> pItems) {
        if(this.mCurrentItems.size() > 0) this.mCurrentItems.clear();
        this.mCurrentItems.addAll(pItems);
    }

    public void addCompletedItem(Item newItem){
        mCompletedItems.add(newItem);
    }

    public ArrayList<Item> getCompletedItems(){
        return mCompletedItems;
    }

    public void setCompletedItems(ArrayList<Item> pItems) {
        if(this.mCompletedItems.size() > 0) this.mCompletedItems.clear();
        this.mCompletedItems.addAll(pItems);
    }
}
