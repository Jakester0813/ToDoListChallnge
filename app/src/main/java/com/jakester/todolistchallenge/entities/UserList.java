package com.jakester.todolistchallenge.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Jake on 8/2/2017.
 */

public class UserList implements Parcelable {
    private int mIDInt;
    private String mNameString;
    private ArrayList<Item> mCurrentItems;
    private ArrayList<Item> mCompletedItems;
    private boolean mShowCompleted;
    private boolean mUpdated;

    //Set to initialize item by name, set other string fields to empty, and set
    //item to lowest priority (2 = low, 1 = medium, 0 = high)
    public UserList(int pID, String pName){
        this.mIDInt = pID;
        this.mNameString = pName;
        this.mCurrentItems = new ArrayList<Item>();
        this.mCompletedItems = new ArrayList<Item>();
        this.mShowCompleted = false;
        this.mUpdated = false;
    }

    /*These setter methods are used for only modifying fields of this class, as the item is only
     *created after the user sets the name by default.
     */

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mIDInt);
        dest.writeString(mNameString);
        dest.writeList(mCurrentItems);
        dest.writeList(mCompletedItems);
        dest.writeByte((byte) (mShowCompleted ? 1 : 0));
    }

    protected UserList(Parcel in) {
        mIDInt = in.readInt();
        mNameString = in.readString();
        mCurrentItems = new ArrayList<Item>();
        mCurrentItems = in.readArrayList(Item.class.getClassLoader());
        mCompletedItems = new ArrayList<Item>();
        mCompletedItems = in.readArrayList(Item.class.getClassLoader());
        mShowCompleted = in.readByte() != 0;

    }



    public static final Creator<UserList> CREATOR = new Creator<UserList>() {
        @Override
        public UserList createFromParcel(Parcel in) {
            return new UserList(in);
        }

        @Override
        public UserList[] newArray(int size) {
            return new UserList[size];
        }
    };

    public int getID(){
        return mIDInt;
    }

    public void setID(int id) { this.mIDInt = id; }

    public void setName(String pName){ this.mNameString = pName; }
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

    public boolean getShowCompleted(){
        return mShowCompleted;
    }

    public void setShowCompleted(boolean show){
        this.mShowCompleted = show;
    }

    public boolean getUpdated(){
        return mUpdated;
    }

    public void setUpdated(boolean updated){
        this.mUpdated = updated;
    }

}
