package com.jakester.todolistchallenge.model.objects;

import java.util.ArrayList;

/**
 * Created by Jake on 8/2/2017.
 */

public class List {
    private String mNameString;
    private ArrayList<Item> mCurrentItems;
    private ArrayList<Item> mCompletedItems;

    //Set to initialize item by name, set other string fields to empty, and set
    //item to lowest priority (2 = low, 1 = medium, 0 = high)
    public List(String pName){
        this.mNameString = pName;
        this.mCurrentItems = new ArrayList<Item>();
        this.mCompletedItems = new ArrayList<Item>();
    }

    /*These setter methods are used for only modifying fields of this class, as the item is only
     *created after the user sets the name by default.
     */

    public void setName(String pName){
        this.mNameString = pName;
    }

    public String getName(){
        return mNameString;
    }

    public void addCurrentItem(Item newItem){
        mCurrentItems.add(newItem);
    }

    public void addCompletedItem(Item newItem){
        mCompletedItems.add(newItem);
    }
}
