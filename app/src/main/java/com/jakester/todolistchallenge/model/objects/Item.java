package com.jakester.todolistchallenge.model.objects;

/**
 * Created by Jake on 8/2/2017.
 */

public class Item {
    private String mNameString;
    private boolean mCompletedBool;
    private String mNoteString;
    private String mAddedDateString;
    private String mDueDateString;
    private int mPriorityInt;

    //Set to initialize item by name, set other string fields to empty, and set
    //item to lowest priority (2 = low, 1 = medium, 0 = high)
    public Item(String pName){
        this.mNameString = pName;
        this.mCompletedBool = false;
        this.mNoteString = "";
        this.mAddedDateString = "";
        this.mDueDateString = "";
        this.mPriorityInt = 2;
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

    public void setPriority(int pPriority){
        this.mPriorityInt = pPriority;
    }

    public int getPriority(){
        return mPriorityInt;
    }

}


