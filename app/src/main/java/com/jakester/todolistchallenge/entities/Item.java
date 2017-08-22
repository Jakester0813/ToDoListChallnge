package com.jakester.todolistchallenge.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Jake on 8/2/2017.
 */

public class Item implements Parcelable {
    private String mNameString;
    private boolean mCompletedBool;
    private String mNoteString;
    private String mAddedDateString;
    private String mDueDateString;
    private String mPriorityString;
    private int mPriorityColor;

    public Item(String pName){
        this.mNameString = pName;
        this.mCompletedBool = false;
        this.mNoteString = "";
        this.mAddedDateString = setAddedDate();
        this.mDueDateString = "";
        this.mPriorityString = "";
        this.mPriorityColor = -1;
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

    public void setCompleted(boolean pCompleted){
        this.mCompletedBool = pCompleted;
    }

    public boolean getCompleted(){
        return mCompletedBool;
    }

    public void setNote(String pNote){
        this.mNoteString = pNote;
    }

    public String getNote(){
        return mNoteString;
    }

    public String setAddedDate(){
        return new SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().getTime()).toString();
    }

    public void setAddedDate(String pDate){
        this.mAddedDateString = pDate;
    }

    public String getAddedDate(){
        return mAddedDateString;
    }

    public void setDueDate(String pDate){
        this.mDueDateString = pDate;
    }

    public String getDueDate(){
        return mDueDateString;
    }

    public void setPriority(String pPriority){
        this.mPriorityString = pPriority;
    }

    public String getPriority(){
        return mPriorityString;
    }

    public void setPriorityColor(int pPriority){
        this.mPriorityColor = pPriority;
    }

    public int getPriorityColor(){
        return mPriorityColor;
    }

    protected Item(Parcel in){
        mNameString = in.readString();
        mCompletedBool = in.readByte() != 0;
        mNoteString = in.readString();
        mAddedDateString = in.readString();
        mDueDateString = in.readString();
        mPriorityString = in.readString();
        mPriorityColor = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mNameString);
        dest.writeByte((byte) (mCompletedBool ? 1 : 0));
        dest.writeString(mNoteString);
        dest.writeString(mAddedDateString);
        dest.writeString(mDueDateString);
        dest.writeString(mPriorityString);
        dest.writeInt(mPriorityColor);
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };
}


